(ns ratatouille.wire.datomic.meal)

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