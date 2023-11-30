(ns ratatouille.db.datomic.reservation-test
  (:require
   [clojure.test :refer [is testing]]
   [common-clj.component.datomic :as component.datomic]
   [datomic.client.api :as dl]
   [fixtures.meal]
   [fixtures.reservation]
   [java-time.api :as jt]
   [matcher-combinators.test :refer [match?]]
   [ratatouille.db.datomic.config :as datomic.config]
   [ratatouille.db.datomic.reservation :as database.reservation]
   [schema.test :as s]))

(s/deftest redeemed-test
  (let [mocked-datomic (component.datomic/mocked-datomic-local datomic.config/schemas)]
    (testing "Given a reservation in a ready status, we can redeem that"
      (database.reservation/insert! fixtures.reservation/reservation-ready mocked-datomic)
      (database.reservation/redeemed! fixtures.reservation/reservation-id (jt/instant) mocked-datomic)
      (is (match? #:reservation{:created-at  jt/instant?
                                :id          fixtures.reservation/reservation-id
                                :meal-id     fixtures.meal/meal-id
                                :redeemed-at jt/instant?
                                :status      :reservation.status/redeemed}
                  (database.reservation/lookup fixtures.reservation/reservation-id (dl/db mocked-datomic)))))))
