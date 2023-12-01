(ns ratatouille.adapters.reservation-test
  (:require [clojure.test :refer :all]
            [matcher-combinators.test :refer [match?]]
            [ratatouille.adapters.reservation :as adapters.reservation]
            [schema.test :as s]
            [fixtures.meal]
            [fixtures.reservation]))

(s/deftest internal-to-datomic-test
  (testing "Given a internal reservation entity we should be able to convert it to datomic model"
    (is (match? {:reservation/created-at inst?
            :reservation/id         fixtures.reservation/reservation-id
            :reservation/meal-id    fixtures.meal/meal-id
            :reservation/status     :reservation.status/ready}
           (adapters.reservation/internal->datomic fixtures.reservation/reservation-ready)))))
