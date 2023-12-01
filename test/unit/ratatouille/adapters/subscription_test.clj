(ns ratatouille.adapters.subscription-test
  (:require [clojure.test :refer [is testing]]
            [matcher-combinators.test :refer [match?]]
            [ratatouille.adapters.subscription :as adapters.subscription]
            [schema.test :as s]))

(s/deftest wire->subscription-test
  (testing "Given a Telegram chat id we can create a internal subscription entity"
    (is (match? {:subscription/chat-id    "123456789"
                 :subscription/created-at inst?
                 :subscription/id         #uuid "25f9e794-323b-3538-85f5-181f1b624d0b"}
                (adapters.subscription/wire->subscription "123456789")))))
