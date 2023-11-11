(ns ratatouille.adapters.user-test
  (:require [clojure.test :refer :all]
            [schema.test :as s]
            [ratatouille.adapters.user :as adapters.user]
            [matcher-combinators.test :refer [match?]]
            [fixtures.user]))

(s/deftest wire->internal-test
  (testing "GIVEN a wire user map WHEN we internalize the entity THEN the map should be in the internal user entity format"
    (is (match? {:user/id               uuid?
                 :user/cpf              "035.475.890-02"
                 :user/name             "Manuel Gomes"
                 :user/telegram-chat-id "123456789"}
                (adapters.user/wire->internal fixtures.user/wire-user)))))

(s/deftest internal->wire-test
  (testing "GIVEN a internal user entity WHEN we externalize the entity THEN the map should be in the external user entity format"
    (is (match? {:id               uuid?
                 :cpf              "035.475.890-02"
                 :name             "Manuel Gomes"
                 :telegram-chat-id "123456789"}
                (adapters.user/internal->wire fixtures.user/user)))))
