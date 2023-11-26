(ns ratatouille.db.datomic.reservation
  (:require
   [datomic.client.api :as dl]
   [ratatouille.models.reservation :as models.reservation]
   [schema.core :as s]))

(s/defn insert!
  [reservation :- models.reservation/Reservation
   datomic-connection]
  (dl/transact datomic-connection {:tx-data [reservation]}))

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