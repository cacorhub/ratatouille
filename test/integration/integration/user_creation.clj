(ns integration.user-creation
  (:require [clojure.test :refer :all]
            [com.stuartsierra.component :as component]
            [ratatouille.components :as components]
            [common-clj.component.helper.core :as component.helper]
            [integration.aux.http :as aux.http]
            [schema.test :as s]
            [matcher-combinators.test :refer [match?]]
            [mockfn.macros :as mfn]
            [morse.api :as morse-api]
            [mockfn.matchers]
            [clj-uuid]
            [fixtures.user]))

(s/deftest user-creation
  (let [system (component/start components/system-test)
        service-fn (-> (component.helper/get-component-content :service system)
                       :io.pedestal.http/service-fn)]
    (testing "That we can create a user"
      (is (match? {:status 200
                   :body   {:user {:id               clj-uuid/uuid-string?
                                   :cpf              "035.475.890-02"
                                   :name             "Manuel Gomes"
                                   :telegram-chat-id "123456789"
                                   :status           "pending-activation"}}}
                  (mfn/verifying [(morse-api/send-text (mockfn.matchers/any) (mockfn.matchers/any) (mockfn.matchers/any)) :result (mockfn.matchers/exactly 1)]
                                 (aux.http/create-user! fixtures.user/wire-user service-fn)))))
    (component/stop system)))

