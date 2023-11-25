(ns ratatouille.db.datomic.user
  (:require [datomic.client.api :as dl]
            [schema.core :as s]
            [ratatouille.models.user :as models.user]))

(s/defn insert!
  [user :- models.user/User
   datomic-connection]
  (dl/transact datomic-connection {:tx-data [user]}))

(s/defn lookup-by-telegram-chat-id :- (s/maybe models.user/User)
  [telegram-chat-id :- s/Str
   datomic-database]
  (some-> (dl/q '[:find (pull ?user [*])
                  :in $ ?telegram-chat-id
                  :where [?user :user/telegram-chat-id ?telegram-chat-id]] datomic-database telegram-chat-id)
          ffirst
          (dissoc :db/id)))

(s/defn lookup-by-cpf :- (s/maybe models.user/User)
  [cpf :- s/Str
   datomic-database]
  (some-> (dl/q '[:find (pull ?user [*])
                  :in $ ?cpf
                  :where [?user :user/cpf ?cpf]] datomic-database cpf)
          ffirst
          (dissoc :db/id)))

(s/defn activate!
  [user-id :- s/Uuid
   datomic-connection]
  (dl/transact datomic-connection {:tx-data [{:user/id     user-id
                                              :user/status :user.status/active}]}))
