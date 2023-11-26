(ns ratatouille.controllers.reservation
  (:require [datomic.client.api :as dl]
            [ratatouille.db.datomic.meal :as database.meal]
            [ratatouille.db.datomic.user :as database.user]
            [ratatouille.diplomat.telegram.producer :as diplomat.telegram.producer]
            [java-time.api :as jt]
            [schema.core :as s]
            [ratatouille.logic.reservation :as logic.reservation]
            [ratatouille.db.datomic.reservation :as database.reservation]
            [clj.qrgen])
  (:import (java.time LocalTime)))

(s/defn reserve-lunch!
  [chat-id :- s/Str
   as-of :- LocalTime
   datomic-connection
   config]
  (if (logic.reservation/with-in-time-window-for-lunch-reservation? (jt/local-time as-of))
    (let [{user-id :user/id} (database.user/lookup-by-telegram-chat-id (str chat-id) (dl/db datomic-connection))
          {meal-id :meal/id} (database.meal/by-reference-date-with-type (jt/local-date as-of) :meal.type/lunch (dl/db datomic-connection))
          reservation (if-let [existent-reservation (database.reservation/by-meal-with-user meal-id user-id (dl/db datomic-connection))]
                        existent-reservation
                        (logic.reservation/->reservation meal-id user-id))
          _ (database.reservation/insert! reservation datomic-connection)
          qr-code-file (clj.qrgen/as-file (clj.qrgen/from (str (:reservation/id reservation))) (str "/tmp" (str (:reservation/id reservation)) ".png"))]
      (diplomat.telegram.producer/notify-reservation-qr-code! chat-id qr-code-file config))
    (diplomat.telegram.producer/notify-lunch-reservation-outside-time-window! chat-id config)))

(s/defn reserve-dinner!
  [chat-id :- s/Str
   as-of :- LocalTime
   datomic-connection
   config]
  (if (logic.reservation/with-in-time-window-for-dinner-reservation? (jt/local-time as-of))
    (let [{:user/keys [id]} (database.user/lookup-by-telegram-chat-id (str chat-id) (dl/db datomic-connection))
          {meal-id :meal/id} (database.meal/by-reference-date-with-type (jt/local-date as-of) :meal.type/dinner (dl/db datomic-connection))
          reservation (if-let [existent-reservation (database.reservation/by-meal-with-user meal-id id (dl/db datomic-connection))]
                        existent-reservation
                        (logic.reservation/->reservation meal-id id))
          _ (database.reservation/insert! reservation datomic-connection)
          qr-code-file (clj.qrgen/as-file (clj.qrgen/from (str (:reservation/id reservation))) (str "/tmp" (str (:reservation/id reservation)) ".png"))]
      (diplomat.telegram.producer/notify-reservation-qr-code! chat-id qr-code-file config))
    (diplomat.telegram.producer/notify-dinner-reservation-outside-time-window! chat-id config)))
