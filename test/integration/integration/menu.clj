(ns integration.menu
  (:require
   [clojure.test :refer :all]))

#_(s/deftest user-creation-already-taken-cpf
    (let [system (component/start components/system-test)]
      (testing "That we can't create an user given a CPF that is already taken"

        #_(component.telegram.consumer/insert-incoming-update! {:update_id 56789
                                                                :message   {:text     "/wring-command param1 param2"
                                                                            :entities [{:type "bot_command"}]
                                                                            :chat     {:id 123456789}}} telegram-consumer)

        (is (match? {:status 200}
                    (aux.http/create-user! fixtures.user/wire-user service-fn)))
        (is (match? {:status 400
                     :body   {:error   "cpf-already-taken"
                              :message "The CPF provided is already in use"
                              :detail  {:cpf "03547589002"}}}
                    (aux.http/create-user! fixtures.user/wire-user service-fn))))
      (component/stop system)))