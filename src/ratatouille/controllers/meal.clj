(ns ratatouille.controllers.meal
  (:require [clj-time.core :as t]
            [clj-time.predicates :as t-predicates]
            [datomic.client.api :as dl]
            [schema.core :as s]
            [ratatouille.db.datomic.meal :as database.meal]
            [ratatouille.logic.meal :as logic.meal]
            [taoensso.timbre :as log]))

(s/defn mise-in-place-meal-job!
  [datomic-connection]
  (let [reference-date (t/today)
        today-lunch-meal (database.meal/by-reference-date-with-type reference-date :meal.type/lunch (dl/db datomic-connection))
        today-dinner-meal (database.meal/by-reference-date-with-type reference-date :meal.type/dinner (dl/db datomic-connection))]
    (when (and (not today-lunch-meal) (t-predicates/weekday? (t/now)))
      (-> (logic.meal/->meal reference-date :meal.type/lunch)
          (database.meal/insert! datomic-connection))
      (log/info ::lunch-meal-added-to-database))
    (when (and (not today-dinner-meal) (t-predicates/weekday? (t/now)))
      (-> (logic.meal/->meal reference-date :meal.type/dinner)
          (database.meal/insert! datomic-connection))
      (log/info ::dinner-meal-added-to-database))))
