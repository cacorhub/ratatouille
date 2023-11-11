(ns ratatouille.models.user
  (:require [schema.core :as s]))

(s/defschema User
  {:user/id                                      s/Keyword
   :user/name                                    s/Str
   :user/cpf                                     s/Str
   (s/optional-key :user/telegram-chat-id)       s/Str
   (s/optional-key :user/one-time-password-hash) s/Str})
