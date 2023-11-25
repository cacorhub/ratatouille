(ns ratatouille.controllers.reservation
  (:require [ratatouille.diplomat.telegram.producer :as diplomat.telegram.producer]
            [java-time.api :as jt]
            [schema.core :as s])
  (:import (java.time LocalTime)))

(s/defn with-in-time-window-for-lunch-reservation? :- s/Bool
  [now :- LocalTime]
  (and (jt/after? now (jt/local-time 6 0))
       (jt/before? now (jt/local-time 12 0))))

(s/defn reserve-lunch!
  [chat-id :- s/Str
   {:keys [telegram] :as config}]
  (let [time-now (jt/local-time)]
    (when-not (with-in-time-window-for-lunch-reservation? time-now)
      (diplomat.telegram.producer/notify-lunch-reservation-outside-time-window! chat-id config))))