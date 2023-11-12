(ns ratatouille.db.datomic.user-test
  (:require [clojure.test :refer :all]
            [common-clj.component.datomic :as component.datomic]
            [datomic.client.api :as dl]
            [ratatouille.db.datomic.config :as datomic.config]
            [ratatouille.db.datomic.user :as database.user]
            [fixtures.user]
            [schema.test :as s]
            [matcher-combinators.test :refer [match?]]))

(s/deftest lookup-by-telegram-chat-id-test
  (let [mocked-datomic (component.datomic/mocked-datomic-local datomic.config/schemas)]
    (database.user/insert! fixtures.user/user mocked-datomic)
    (testing "that we can query a user entity by telegram chat id"
      (is (match? fixtures.user/user
                  (database.user/lookup-by-telegram-chat-id fixtures.user/telegram-chat-id (dl/db mocked-datomic)))))))