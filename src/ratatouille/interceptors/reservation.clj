(ns ratatouille.interceptors.reservation
  (:require [common-clj.keyword.core :as common-keyword]
            [datomic.client.api :as dl]
            [io.pedestal.interceptor :as pedestal.interceptor]
            [java-time.api :as jt]
            [ratatouille.db.datomic.meal :as database.meal]
            [ratatouille.db.datomic.reservation :as database.reservation]
            [ratatouille.models.meal :as models.meal]
            [ratatouille.diplomat.telegram.producer :as diplomat.telegram.producer]
            [schema.core :as s]))

(s/defn ^:private over-limit-reservations-check-interceptor
  [meal-type :- models.meal/Type]
  (pedestal.interceptor/interceptor {:name  :over-limit-reservations-lunch-check-interceptor
                                     :enter (fn [{{:update/keys [chat-id]} :update
                                                  {:keys [config datomic]} :components :as context}]
                                              (let [meal-type' (common-keyword/un-namespaced meal-type)
                                                    reservation-max-limit (-> (:meal config) meal-type' :reservation-max-limit)
                                                    {meal-id :meal/id} (database.meal/by-reference-date-with-type (jt/local-date) meal-type (-> datomic :connection dl/db))
                                                    reservations (database.reservation/by-meal meal-id (-> datomic :connection dl/db))]
                                                (if (>= (count reservations) reservation-max-limit)
                                                  (diplomat.telegram.producer/notify-reservations-over-limit chat-id config)
                                                  context)))}))

(def over-limit-reservations-lunch-check-interceptor
  (over-limit-reservations-check-interceptor :meal.type/lunch))

(def over-limit-reservations-dinner-check-interceptor
  (over-limit-reservations-check-interceptor :meal.type/dinner))
