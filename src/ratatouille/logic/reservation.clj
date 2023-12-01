(ns ratatouille.logic.reservation
  (:require [common-clj.keyword.core :as common-keyword]
            [java-time.api :as jt]
            [ratatouille.models.meal :as models.meal]
            [ratatouille.models.reservation :as models.reservation]
            [schema.core :as s])
  (:import (java.time LocalTime)
           (java.util Date)))

(s/defn ->reservation :- models.reservation/Reservation
  [meal-id :- s/Uuid
   user-id :- s/Uuid]
  {:reservation/id         (random-uuid)
   :reservation/user-id    user-id
   :reservation/meal-id    meal-id
   :reservation/created-at (Date.)
   :reservation/status     :reservation.status/ready})

(s/defn with-in-time-window-for-meal-reservation? :- s/Bool
  [meal-type :- models.meal/Type
   now :- LocalTime
   config]
  (let [meal-type' (common-keyword/un-namespaced meal-type)
        begin-time-window (-> (:meal config) meal-type' :reservation-start)
        end-time-window (-> (:meal config) meal-type' :reservation-end)]
    (and (jt/after? now (jt/local-time begin-time-window))
         (jt/before? now (jt/local-time end-time-window)))))

(def with-in-time-window-for-lunch-reservation?
  (partial with-in-time-window-for-meal-reservation? :meal.type/lunch))

(def with-in-time-window-for-dinner-reservation?
  (partial with-in-time-window-for-meal-reservation? :meal.type/dinner))