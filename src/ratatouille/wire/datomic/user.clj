(ns ratatouille.wire.datomic.user)

(def user-skeleton
  [{:db/ident       :user/id
    :db/valueType   :db.type/uuid
    :db/unique      :db.unique/identity
    :db/cardinality :db.cardinality/one
    :db/doc         "User ID"}
   {:db/ident       :user/name
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one
    :db/fulltext    true
    :db/doc         "User Name"}
   {:db/ident       :user/cpf
    :db/valueType   :db.type/string
    :db/unique      :db.unique/identity
    :db/cardinality :db.cardinality/one
    :db/fulltext    true
    :db/doc         "User CPF"}
   {:db/ident       :user/telegram-chat-id
    :db/valueType   :db.type/string
    :db/unique      :db.unique/identity
    :db/cardinality :db.cardinality/one
    :db/fulltext    true
    :db/doc         "Chat ID with telegram bot"}
   {:db/ident       :user/one-time-password-hash
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc         "User one time password hash"}])