(ns ratatouille.db.datomic.user
  (:require [datomic.client.api :as dl]
            [schema.core :as s]
            [ratatouille.models.user :as models.user]))

(s/defn insert!
  [user :- models.user/User
   datomic-connection]
  (dl/transact datomic-connection {:tx-data [user]}))
