(ns ratatouille.models.subscription
  (:require [schema.core :as s])
  (:import (java.util Date)))

(def subscription-skeleton
  {:subscription/id         s/Uuid
   :subscription/chat-id    s/Str
   :subscription/created-at Date})

(s/defschema Subscription
  subscription-skeleton)