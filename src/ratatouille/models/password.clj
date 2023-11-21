(ns ratatouille.models.password
  (:require [schema.core :as s])
  (:import (java.util Date)))

(def statuses #{:password.status/ready :password.status/used :password.status/expired})
(def Status (apply s/enum statuses))

(def password-skeleton
  {:password/id                          s/Uuid
   :password/user-id                     s/Uuid
   :password/chat-id                     s/Str
   :password/one-time-password           s/Str
   :password/message-id                  s/Str
   (s/optional-key :password/used-at)    Date
   :password/created-at                  Date
   (s/optional-key :password/expired-at) Date
   :password/status                      Status})

(s/defschema Password
  password-skeleton)