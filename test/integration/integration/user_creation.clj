(ns integration.user-creation
  (:require [clj-uuid]
            [clojure.string :as str]
            [clojure.test :refer [is testing]]
            [com.stuartsierra.component :as component]
            [common-clj.component.helper.core :as component.helper]
            [fixtures.user]
            [integration.aux.http :as aux.http]
            [matcher-combinators.test :refer [match?]]
            [mockfn.matchers]
            [ratatouille.components :as components]
            [schema.test :as s]))

(s/deftest user-creation
  (let [system (component/start components/system-test)
        service-fn (-> (component.helper/get-component-content :service system)
                       :io.pedestal.http/service-fn)
        producer (component.helper/get-component-content :telegram-producer system)]

    (testing "That we can create a user"
      (is (match? {:status 200
                   :body   {:user {:id               clj-uuid/uuid-string?
                                   :cpf              "03547589002"
                                   :name             "Manuel Gomes"
                                   :telegram-chat-id "123456789"
                                   :status           "pending-activation"}}}
                  (aux.http/create-user! fixtures.user/wire-user service-fn)))

      (is (= [{:chat-id "123456789"
               :text    "Conta criada com sucesso, esse é o seu código de ativação: 123456789"}]
             @(:produced producer))))
    (component/stop system)))

(s/deftest user-creation-invalid-cpf
  (let [system (component/start components/system-test)
        service-fn (-> (component.helper/get-component-content :service system)
                       :io.pedestal.http/service-fn)]
    (testing "That we can't create an user given a invalid CPF"
      (is (match? {:status 400
                   :body   {:error   "invalid-cpf"
                            :message "The CPF provided is not valid"
                            :detail  {:cpf "9876543210"}}}
                  (aux.http/create-user! (assoc fixtures.user/wire-user :cpf "9876543210") service-fn))))
    (component/stop system)))

(s/deftest user-creation-already-taken-cpf
  (let [system (component/start components/system-test)
        service-fn (-> (component.helper/get-component-content :service system)
                       :io.pedestal.http/service-fn)]
    (testing "That we can't create an user given a CPF that is already taken"
      (is (match? {:status 200}
                  (aux.http/create-user! fixtures.user/wire-user service-fn)))
      (is (match? {:status 400
                   :body   {:error   "cpf-already-taken"
                            :message "The CPF provided is already in use"
                            :detail  {:cpf "03547589002"}}}
                  (aux.http/create-user! fixtures.user/wire-user service-fn))))
    (component/stop system)))

(s/deftest user-creation-rate-limiter
  (let [system (component/start components/system-test)
        service-fn (-> (component.helper/get-component-content :service system)
                       :io.pedestal.http/service-fn)]
    (testing "Given a rate limit of 4 per min the first request in the 1 minute time window should be accepted"
      (is (match? {:status 200
                   :body   {:user {:id               clj-uuid/uuid-string?
                                   :cpf              "03547589002"
                                   :name             "Manuel Gomes"
                                   :telegram-chat-id "123456789"
                                   :status           "pending-activation"}}}
                  (aux.http/create-user! fixtures.user/wire-user service-fn))))

    (testing "Given a rate limit of 4 per min, the second request in the 1 minute time window should be accepted"
      (is (match? {:status 400
                   :body   {:error   "cpf-already-taken"
                            :message "The CPF provided is already in use"
                            :detail  {:cpf "03547589002"}}}
                  (aux.http/create-user! fixtures.user/wire-user service-fn))))

    (testing "Given a rate limit of 4 per min, the third request in the 1 minute time window should be accepted"
      (is (match? {:status 400
                   :body   {:error   "cpf-already-taken"
                            :message "The CPF provided is already in use"
                            :detail  {:cpf "03547589002"}}}
                  (aux.http/create-user! fixtures.user/wire-user service-fn))))

    (testing "Given a rate limit of 4 per min, the fourth request in the 1 minute time window should be accepted"
      (is (match? {:status 400
                   :body   {:error   "cpf-already-taken"
                            :message "The CPF provided is already in use"
                            :detail  {:cpf "03547589002"}}}
                  (aux.http/create-user! fixtures.user/wire-user service-fn))))

    (testing "Given a rate limit of 4 per min, the fifth request in the 1 minute time window should be refused"
      (is (match? {:status 429
                   :body   {:error   "too-many-requests"
                            :message "Too Many Requests"
                            :detail  {:error "too-many-requests"}}}
                  (aux.http/create-user! fixtures.user/wire-user service-fn))))

    (testing "Given a rate limit of 4 per min, the fifth request in the 1 minute time window should be refused and we send proper metrics to prometheus"
      (is (str/includes? (-> (aux.http/fetch-metrics "random-secret" service-fn) :body) "ratatouille_rate_limiter_total{route_name=\":create-user\",} 1.0")))

    (Thread/sleep 60000)

    (testing "After 1 minute, everything is fine"
      (is (match? {:status 400
                   :body   {:error   "cpf-already-taken"
                            :message "The CPF provided is already in use"
                            :detail  {:cpf "03547589002"}}}
                  (aux.http/create-user! fixtures.user/wire-user service-fn))))

    (component/stop system)))
