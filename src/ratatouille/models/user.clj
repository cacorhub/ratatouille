(ns ratatouille.models.user
  (:require [schema.core :as s]))

(def statuses #{:user.status/pending-activation :user.status/active})

(def Status (apply s/enum statuses))

(def user-skeleton
  {:user/id                                      s/Uuid
   :user/name                                    s/Str
   :user/cpf                                     s/Str
   (s/optional-key :user/telegram-chat-id)       s/Str
   (s/optional-key :user/one-time-password-hash) s/Str
   :user/status                                  Status})

(s/defschema User user-skeleton)
