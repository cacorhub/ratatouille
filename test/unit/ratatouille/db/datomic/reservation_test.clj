(ns ratatouille.db.datomic.reservation-test
  (:require [clojure.test :refer :all]
            [common-clj.component.datomic :as component.datomic]
            [datomic.client.api :as dl]
            [java-time.api :as jt]
            [ratatouille.db.datomic.config :as datomic.config]
            [ratatouille.db.datomic.reservation :as database.reservation]
            [fixtures.reservation]
            [matcher-combinators.test :refer [match?]]
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
