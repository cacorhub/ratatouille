(ns ratatouille.wire.datomic.subscription)

(def subscription-skeleton
  [{:db/ident       :subscription/id
    :db/valueType   :db.type/uuid
    :db/cardinality :db.cardinality/one
    :db/unique      :db.unique/identity
    :db/doc         "Subscription ID"}
   {:db/ident       :subscription/chat-id
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc         "Telegram Chat ID"}
   {:db/ident       :subscription/created-at
    :db/valueType   :db.type/instant
    :db/cardinality :db.cardinality/one
    :db/doc         "When the subscription was made"}])
