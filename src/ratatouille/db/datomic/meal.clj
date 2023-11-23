(ns ratatouille.db.datomic.meal
  (:require [datomic.client.api :as dl]
            [ratatouille.models.meal :as models.meal]
            [schema.core :as s]
            [ratatouille.adapters.meal :as adapters.meal])
  (:import (org.joda.time LocalDate)))

(s/defn insert!
  [meal :- models.meal/Meal
   datomic-connection]
  (dl/transact datomic-connection {:tx-data [(adapters.meal/internal->datomic meal)]}))

(s/defn by-reference-date-with-type :- (s/maybe models.meal/Meal)
  [reference-date :- LocalDate
   type :- models.meal/Type
   datomic-db]
  (some-> (dl/q '[:find (pull ?meal [*])
                  :in $ ?reference-date ?type
                  :where [?meal :meal/reference-date ?reference-date]
                         [?meal :meal/type ?type]] datomic-db (str reference-date) type)
          ffirst
          (dissoc :db/id)
          adapters.meal/datomic->internal))

(s/defn lookup :- (s/maybe models.meal/Meal)
  [id :- s/Uuid
   datomic-db]
  (some-> (dl/q '[:find (pull ?meal [*])
                  :in $ ?meal-id
                  :where [?meal :meal/id ?meal-id]] datomic-db id)
          ffirst
          (dissoc :db/id)
          adapters.meal/datomic->internal))