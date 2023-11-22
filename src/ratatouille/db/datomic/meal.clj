(ns ratatouille.db.datomic.meal
  (:require [datomic.client.api :as dl]
            [ratatouille.models.meal :as models.meal]
            [schema.core :as s]
            [ratatouille.adapters.meal :as adapters.meal]))

(s/defn insert!
  [meal :- models.meal/Meal
   datomic-connection]
  (dl/transact datomic-connection {:tx-data [(adapters.meal/internal->datomic meal)]}))