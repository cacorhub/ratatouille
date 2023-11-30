(ns ratatouille.db.datomic.reservation
  (:require
   [datomic.client.api :as dl]
   [java-time.api :as jt]
   [ratatouille.adapters.reservation :as adapters.reservation]
   [ratatouille.models.reservation :as models.reservation]
   [schema.core :as s])
  (:import
   (java.time Instant)))

(s/defn insert!
  [reservation :- models.reservation/Reservation
   datomic-connection]
  (dl/transact datomic-connection {:tx-data [(adapters.reservation/internal->datomic reservation)]}))

(s/defn by-meal-with-user :- (s/maybe models.reservation/Reservation)
  [meal-id :- s/Uuid
   user-id :- s/Uuid
   datomic-db]
  (some-> (dl/q '[:find (pull ?reservation [*])
                  :in $ ?meal-id ?user-id
                  :where [?reservation :reservation/meal-id ?meal-id]
                  [?reservation :reservation/user-id ?user-id]] datomic-db meal-id user-id)
          ffirst
          (dissoc :db/id)))

(s/defn by-meal :- (s/maybe models.reservation/Reservation)
  [meal-id :- s/Uuid
   datomic-db]
  (some-> (dl/q '[:find (pull ?reservation [*])
                  :in $ ?meal-id
                  :where [?reservation :reservation/meal-id ?meal-id]] datomic-db meal-id)
          ffirst
          (dissoc :db/id)))

(s/defn redeemed!
  [reservation-id :- s/Uuid
   as-of :- Instant
   datomic-connection]
  (dl/transact datomic-connection {:tx-data [{:reservation/id          reservation-id
                                              :reservation/redeemed-at (jt/java-date as-of)
                                              :reservation/status      :reservation.status/redeemed}]}))

(s/defn lookup :- (s/maybe models.reservation/Reservation)
  [id :- s/Uuid
   datomic-db]
  (some-> (dl/q '[:find (pull ?reservation [*])
                  :in $ ?reservation-id
                  :where [?reservation :reservation/id ?reservation-id]] datomic-db id)
          ffirst
          (dissoc :db/id)
          adapters.reservation/datomic->internal))