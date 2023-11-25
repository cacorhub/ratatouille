(ns ratatouille.db.datomic.reservation
  (:require [datomic.client.api :as dl]
            [ratatouille.models.reservation :as models.reservation]
            [schema.core :as s]))

(s/defn insert!
  [reservation :- models.reservation/Reservation
   datomic-connection]
  (dl/transact datomic-connection {:tx-data [reservation]}))

(s/defn by-meal :- (s/maybe models.reservation/Reservation)
  [meal-id :- s/Uuid
   datomic-db]
  (some-> (dl/q '[:find (pull ?reservation [*])
                  :in $ ?meal-id
                  :where [?reservation :reservation/meal-id ?meal-id]
                  [?meal :meal/type ?type]] datomic-db meal-id)
          ffirst
          (dissoc :db/id)))