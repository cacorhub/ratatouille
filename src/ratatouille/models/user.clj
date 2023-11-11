(ns ratatouille.models.user
  (:require [schema.core :as s]))

(def user-skeleton
  {:user/id                                      s/Uuid
   :user/name                                    s/Str
   :user/cpf                                     s/Str
   (s/optional-key :user/telegram-chat-id)       s/Str
   (s/optional-key :user/one-time-password-hash) s/Str})

(s/defschema User user-skeleton)
