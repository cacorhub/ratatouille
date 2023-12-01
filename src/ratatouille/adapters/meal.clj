(ns ratatouille.adapters.meal
  (:require [java-time.api :as jt]
            [ratatouille.models.meal :as models.meal]
            [ratatouille.wire.datomic.meal :as wire.datomic.meal]
            [schema.core :as s]))

(s/defn internal->datomic :- wire.datomic.meal/Meal
  [{:meal/keys [reference-date created-at] :as meal} :- models.meal/Meal]
  (assoc meal :meal/reference-date (str reference-date)
         :meal/created-at (jt/java-date created-at)))

(s/defn datomic->internal :- models.meal/Meal
  [{:meal/keys [reference-date created-at] :as meal} :- wire.datomic.meal/Meal]
  (assoc meal :meal/reference-date (jt/local-date reference-date)
         :meal/created-at (jt/instant created-at)))
