(ns ratatouille.interceptors.reservation
  (:require [datomic.client.api :as dl]
            [io.pedestal.interceptor :as pedestal.interceptor]
            [java-time.api :as jt]
            [morse.api :as morse-api]
            [ratatouille.db.datomic.meal :as database.meal]
            [ratatouille.db.datomic.reservation :as database.reservation]))

(def over-limit-reservations-lunch-check-interceptor
  (pedestal.interceptor/interceptor {:name  :over-limit-reservations-lunch-check-interceptor
                                     :enter (fn [{{:update/keys [chat-id]} :update
                                                  {:keys [config datomic]} :components :as context}]
                                              (let [{meal-id :meal/id} (database.meal/by-reference-date-with-type (jt/local-date) :meal.type/lunch (-> datomic :connection dl/db))
                                                    reservations (database.reservation/by-meal meal-id (-> datomic :connection dl/db))]
                                                (if (>= (count reservations) 250)
                                                  (morse-api/send-text (-> (:telegram config) :token)
                                                                       chat-id
                                                                       "Reserva não concluida. Atingimos o limite de reservas.")
                                                  context)))}))

(def over-limit-reservations-dinner-check-interceptor
  (pedestal.interceptor/interceptor {:name  :over-limit-reservations-dinner-check-interceptor
                                     :enter (fn [{{:update/keys [chat-id]} :update
                                                  {:keys [config datomic]} :components :as context}]
                                              (let [{meal-id :meal/id} (database.meal/by-reference-date-with-type (jt/local-date) :meal.type/dinner (-> datomic :connection dl/db))
                                                    reservations (database.reservation/by-meal meal-id (-> datomic :connection dl/db))]
                                                (if (>= (count reservations) 250)
                                                  (morse-api/send-text (-> (:telegram config) :token)
                                                                       chat-id
                                                                       "Reserva não concluida. Atingimos o limite de reservas.")
                                                  context)))}))