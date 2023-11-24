(ns ratatouille.wire.datomic.reservation)

(def reservation-skeleton
  [{:db/ident       :reservation/id
    :db/valueType   :db.type/uuid
    :db/cardinality :db.cardinality/one
    :db/unique      :db.unique/identity
    :db/doc         "Reservation ID"}
   {:db/ident       :reservation/meal-id
    :db/valueType   :db.type/uuid
    :db/cardinality :db.cardinality/one
    :db/doc         "Meal for where this reservation is"}
   {:db/ident       :reservation/redeemed-at
    :db/valueType   :db.type/instant
    :db/cardinality :db.cardinality/one
    :db/doc         "When the reservation was redeemed"}
   {:db/ident       :reservation/created-at
    :db/valueType   :db.type/instant
    :db/cardinality :db.cardinality/one
    :db/doc         "When the reservation was placed"}
   {:db/ident       :reservation/status
    :db/valueType   :db.type/instant
    :db/cardinality :db.cardinality/one
    :db/doc         "Reservation status"}])
