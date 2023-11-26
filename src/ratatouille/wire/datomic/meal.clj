(ns ratatouille.wire.datomic.meal
  (:require [java-time.api :as jt]
            [ratatouille.models.meal :as models.meal]
            [schema-tools.core :as schema-tools]
            [schema.core :as s])
  (:import (java.time Instant)
           (java.util Date)))

(def meal-skeleton
  [{:db/ident       :meal/id
    :db/valueType   :db.type/uuid
    :db/cardinality :db.cardinality/one
    :db/unique      :db.unique/identity
    :db/doc         "Meal ID"}
   {:db/ident       :meal/type
    :db/valueType   :db.type/keyword
    :db/cardinality :db.cardinality/one
    :db/doc         "Meal type"}
   {:db/ident       :meal/reference-date
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc         "Meal reference date"}
   {:db/ident       :meal/created-at
    :db/valueType   :db.type/instant
    :db/cardinality :db.cardinality/one
    :db/doc         "When the meal was created"}])

(s/defschema Meal
  (schema-tools/assoc models.meal/Meal
                      :meal/reference-date s/Str
                      :meal/created-at Date))
