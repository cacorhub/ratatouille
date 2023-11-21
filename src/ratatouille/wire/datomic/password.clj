(ns ratatouille.wire.datomic.password)

(def password-skeleton
  [{:db/ident       :password/id
    :db/valueType   :db.type/uuid
    :db/cardinality :db.cardinality/one
    :db/unique      :db.unique/identity
    :db/doc         "Password ID"}
   {:db/ident       :password/user-id
    :db/valueType   :db.type/uuid
    :db/cardinality :db.cardinality/one
    :db/unique      :db.unique/identity
    :db/doc         "Password User's ID"}
   {:db/ident       :password/one-time-password
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc         "One time password"}
   {:db/ident       :password/chat-id
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc         "Telegram Chat ID"}
   {:db/ident       :password/message-id
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc         "Telegram Message ID"}
   {:db/ident       :password/used-at
    :db/valueType   :db.type/instant
    :db/cardinality :db.cardinality/one
    :db/doc         "When the Password was used"}
   {:db/ident       :password/created-at
    :db/valueType   :db.type/instant
    :db/cardinality :db.cardinality/one
    :db/doc         "When the Password was generated"}
   {:db/ident       :password/status
    :db/valueType   :db.type/keyword
    :db/cardinality :db.cardinality/one
    :db/doc         "When the Password was used"}])