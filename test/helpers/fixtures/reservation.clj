(ns fixtures.reservation
  (:require [fixtures.meal]
            [java-time.api :as jt]))

(def reservation-id (random-uuid))
(def created-at (jt/instant))
(def redeemed-at (jt/instant))

(def reservation-ready
  {:reservation/id         reservation-id
   :reservation/meal-id    fixtures.meal/meal-id
   :reservation/created-at created-at
   :reservation/status     :reservation.status/ready})

(def reservation-redeemed
  (assoc reservation-ready :reservation/redeemed-at redeemed-at
                           :reservation/status :reservation.status/redeemed))

(def reservation-ready-datomic
  (assoc reservation-ready :reservation/created-at (jt/java-date created-at)))

(def reservation-redeemed-datomic
  (assoc reservation-ready-datomic :reservation/redeemed-at (jt/java-date redeemed-at)
                                   :reservation/status :reservation.status/redeemed))
