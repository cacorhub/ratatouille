(ns ratatouille.controllers.meal
  (:require [clj-time.core :as t]
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
    (when-not today-lunch-meal
      (-> (logic.meal/->meal reference-date :meal.type/lunch)
          (database.meal/insert! datomic-connection))
      (log/info ::lunch-meal-added-to-database))
    (when-not today-dinner-meal
      (-> (logic.meal/->meal reference-date :meal.type/dinner)
          (database.meal/insert! datomic-connection))
      (log/info ::dinner-meal-added-to-database))))