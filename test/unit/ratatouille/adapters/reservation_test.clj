(ns ratatouille.adapters.reservation-test
  (:require [clojure.test :refer [is testing]]
            [fixtures.meal]
            [fixtures.reservation]
            [java-time.api :as jt]
            [matcher-combinators.test :refer [match?]]
            [ratatouille.adapters.reservation :as adapters.reservation]
            [schema.test :as s]))

(s/deftest internal-to-datomic-test
  (testing "Given a internal reservation entity we should be able to convert it to datomic model"
    (is (match? {:reservation/created-at inst?
                 :reservation/id         fixtures.reservation/reservation-id
                 :reservation/meal-id    fixtures.meal/meal-id
                 :reservation/status     :reservation.status/ready}
                (adapters.reservation/internal->datomic fixtures.reservation/reservation-ready)))

    (is (match? {:reservation/created-at  inst?
                 :reservation/redeemed-at inst?
                 :reservation/id          fixtures.reservation/reservation-id
                 :reservation/meal-id     fixtures.meal/meal-id
                 :reservation/status      :reservation.status/redeemed}
                (adapters.reservation/internal->datomic fixtures.reservation/reservation-redeemed)))))

(s/deftest datomic-to-internal-test
  (testing "Given a datomic entity we can convert it to internal model"
    (is (match? {:reservation/created-at jt/instant?
                 :reservation/id         fixtures.reservation/reservation-id
                 :reservation/meal-id    fixtures.meal/meal-id
                 :reservation/status     :reservation.status/ready}
                (adapters.reservation/datomic->internal fixtures.reservation/reservation-ready-datomic)))

    (is (match? {:reservation/created-at  jt/instant?
                 :reservation/id          fixtures.reservation/reservation-id
                 :reservation/meal-id     fixtures.meal/meal-id
                 :reservation/redeemed-at jt/instant?
                 :reservation/status      :reservation.status/redeemed}
                (adapters.reservation/datomic->internal fixtures.reservation/reservation-redeemed-datomic)))))

(s/deftest internal-to-wire-test
  (testing "Given a internal model entity we can convert it to a external model"
    (is (= {:id      fixtures.reservation/reservation-id
            :meal-id fixtures.meal/meal-id
            :status  :ready}
           (adapters.reservation/internal->wire fixtures.reservation/reservation-ready)))))
