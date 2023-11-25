(ns ratatouille.adapters.meal
  (:require [clj-time.coerce :as t-coerce]
            [java-time.api :as jt]
            [ratatouille.models.meal :as models.meal]
            [schema.core :as s]
            [ratatouille.wire.datomic.meal :as wire.datomic.meal]))

(s/defn internal->datomic :- wire.datomic.meal/Meal
  [{:meal/keys [reference-date created-at] :as meal} :- models.meal/Meal]
  (assoc meal :meal/reference-date (str reference-date)
              :meal/created-at (t-coerce/to-date created-at)))

(s/defn datomic->internal :- models.meal/Meal
  [{:meal/keys [reference-date created-at] :as meal} :- wire.datomic.meal/Meal]
  (assoc meal :meal/reference-date (jt/local-date reference-date)
              :meal/created-at (jt/instant created-at)))
