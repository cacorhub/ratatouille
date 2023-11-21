(ns ratatouille.db.datomic.password
  (:require [datomic.client.api :as dl]
            [ratatouille.models.password :as models.password]
            [schema.core :as s]))

(s/defn insert!
  [password :- models.password/Password
   datomic-connection]
  (dl/transact datomic-connection {:tx-data [password]}))


(s/defn ready-passwords-by-user-id :- [models.password/Password]
  [user-id :- s/Uuid
   datomic-db]
  (some-> (dl/q '[:find (pull ?password [*])
                  :in $ ?user-id
                  :where [?password :password/user-id ?user-id]
                  [?password :password/status :password.status/ready]] datomic-db user-id)
          (->> (mapv first))
          (->> (mapv #(dissoc % :db/id)))))

(s/defn used!
  [password-id :- s/Uuid
   datomic-connection]
  (dl/transact datomic-connection {:tx-data [{:password/id     password-id
                                              :password/status :password.status/used}]}))

(s/defn set-message-id!
  [password-id :- s/Uuid
   message-id :- s/Str
   datomic-connection]
  (dl/transact datomic-connection {:tx-data [{:password/id         password-id
                                              :password/message-id message-id}]}))