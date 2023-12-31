(ns ratatouille.interceptors.reservation
  (:require [common-clj.error.core :as common-error]
            [common-clj.keyword.core :as common-keyword]
            [datomic.client.api :as dl]
            [io.pedestal.interceptor :as pedestal.interceptor]
            [java-time.api :as jt]
            [ratatouille.db.datomic.meal :as database.meal]
            [ratatouille.db.datomic.reservation :as database.reservation]
            [ratatouille.diplomat.telegram.producer :as diplomat.telegram.producer]
            [ratatouille.models.meal :as models.meal]
            [schema.core :as s]))

(s/defn ^:private over-limit-reservations-check-interceptor
  [meal-type :- models.meal/Type]
  (pedestal.interceptor/interceptor {:name  :over-limit-reservations-lunch-check-interceptor
                                     :enter (fn [{{:update/keys [chat-id]}                   :update
                                                  {:keys [config datomic telegram-producer]} :components :as context}]
                                              (let [meal-type' (common-keyword/un-namespaced meal-type)
                                                    reservation-max-limit (-> (:meal config) meal-type' :reservation-max-limit)
                                                    {meal-id :meal/id} (database.meal/by-reference-date-with-type (jt/local-date) meal-type (-> datomic :connection dl/db))
                                                    reservations (database.reservation/by-meal meal-id (-> datomic :connection dl/db))]
                                                (if (>= (count reservations) reservation-max-limit)
                                                  (diplomat.telegram.producer/notify-reservations-over-limit chat-id telegram-producer)
                                                  context)))}))

(def over-limit-reservations-lunch-check-interceptor
  (over-limit-reservations-check-interceptor :meal.type/lunch))

(def over-limit-reservations-dinner-check-interceptor
  (over-limit-reservations-check-interceptor :meal.type/dinner))

(def authorization-interceptor
  (pedestal.interceptor/interceptor {:name  ::authorization-interceptor-interceptor
                                     :enter (fn [{{{:keys [config]} :components
                                                   headers          :headers} :request :as context}]
                                              (if (not= (:reservation-redeem-password config) (get headers "authorization"))
                                                (common-error/http-friendly-exception 403
                                                                                      "not-authorized"
                                                                                      "You are not authorized to use this endpoint"
                                                                                      {})
                                                context))}))
