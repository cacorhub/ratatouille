(ns ratatouille.db.datomic.user-test
  (:require
   [clojure.test :refer [is testing]]
   [common-clj.component.datomic :as component.datomic]
   [datomic.client.api :as dl]
   [fixtures.user]
   [matcher-combinators.test :refer [match?]]
   [ratatouille.db.datomic.config :as datomic.config]
   [ratatouille.db.datomic.user :as database.user]
   [schema.test :as s]))

(s/deftest lookup-by-telegram-chat-id-test
  (let [mocked-datomic (component.datomic/mocked-datomic-local datomic.config/schemas)]
    (database.user/insert! fixtures.user/user mocked-datomic)
    (testing "that we can query a user entity by telegram chat id"
      (is (match? fixtures.user/user
                  (database.user/lookup-by-telegram-chat-id fixtures.user/telegram-chat-id (dl/db mocked-datomic)))))))

(s/deftest lookup-by-cpf-test
  (let [mocked-datomic (component.datomic/mocked-datomic-local datomic.config/schemas)]
    (database.user/insert! fixtures.user/user mocked-datomic)
    (testing "that we can query a user entity by cpf"
      (is (match? fixtures.user/user
                  (database.user/lookup-by-cpf fixtures.user/cpf (dl/db mocked-datomic)))))))

(s/deftest activate!-test
  (let [mocked-datomic (component.datomic/mocked-datomic-local datomic.config/schemas)]
    (database.user/insert! fixtures.user/user mocked-datomic)
    (database.user/activate! fixtures.user/user-id mocked-datomic)
    (testing "Given a user with activation pending, we should be able to mark the user as activated"
      (is (match? {:user/id     fixtures.user/user-id
                   :user/status :user.status/active}
                  (database.user/lookup-by-cpf fixtures.user/cpf (dl/db mocked-datomic)))))))
