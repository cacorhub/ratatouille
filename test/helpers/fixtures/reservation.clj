(ns fixtures.reservation
  (:require [fixtures.meal]
            [java-time.api :as jt]))

(def reservation-id (random-uuid))
(def created-at (jt/instant))

(def reservation-ready
  {:reservation/id         reservation-id
   :reservation/meal-id    fixtures.meal/meal-id
   :reservation/created-at created-at
   :reservation/status     :reservation.status/ready})
