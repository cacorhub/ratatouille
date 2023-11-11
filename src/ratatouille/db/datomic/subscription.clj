(ns ratatouille.db.datomic.subscription
  (:require [datomic.client.api :as dl]
            [schema.core :as s]
            [ratatouille.models.subscription :as models.subscription]))

(s/defn insert!
  [subscription :- models.subscription/Subscription
   datomic-connection]
  (dl/transact datomic-connection {:tx-data [subscription]}))

(s/defn lookup :- (s/maybe models.subscription/Subscription)
  [id :- s/Uuid
   datomic-db]
  (some-> (dl/q '[:find (pull ?subscription [*])
                  :in $ ?subscription-id
                  :where [?subscription :subscription/id ?subscription-id]] datomic-db id)
          ffirst
          (dissoc :db/id)))

(s/defn lookup-all :- [models.subscription/Subscription]
  [datomic-db]
  (some-> (dl/q '[:find (pull ?subscription [*])
                  :in $
                  :where [?subscription :subscription/id _]] datomic-db)
          (->> (mapv first))
          (->> (mapv #(dissoc % :db/id)))))
