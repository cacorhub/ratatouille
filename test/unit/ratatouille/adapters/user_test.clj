(ns ratatouille.adapters.user-test
  (:require [clojure.test :refer [is testing]]
            [fixtures.user]
            [matcher-combinators.matchers :as matchers]
            [matcher-combinators.test :refer [match?]]
            [ratatouille.adapters.user :as adapters.user]
            [schema.test :as s]))

(s/deftest wire->internal-test
  (testing "GIVEN a wire user map WHEN we internalize the entity THEN the map should be in the internal user entity format"
    (is (match? (matchers/equals {:user/id               uuid?
                                  :user/cpf              "03547589002"
                                  :user/name             "Manuel Gomes"
                                  :user/telegram-chat-id "123456789"
                                  :user/status           :user.status/pending-activation})
                (adapters.user/wire->internal fixtures.user/wire-user)))))

(s/deftest internal->wire-test
  (testing "GIVEN a internal user entity WHEN we externalize the entity THEN the map should be in the external user entity format"
    (is (match? (matchers/equals {:id               uuid?
                                  :cpf              "03547589002"
                                  :name             "Manuel Gomes"
                                  :telegram-chat-id "123456789"
                                  :status           :pending-activation})
                (adapters.user/internal->wire fixtures.user/user)))))
