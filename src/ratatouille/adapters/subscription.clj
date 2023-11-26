(ns ratatouille.adapters.subscription
  (:require
   [ratatouille.models.subscription :as models.subscription]
   [schema.core :as s])
  (:import
   (java.util Date UUID)))

(s/defn wire->subscription :- models.subscription/Subscription
  [chat-id :- s/Str]
  {:subscription/id         (UUID/nameUUIDFromBytes (.getBytes (str chat-id)))
   :subscription/chat-id    (str chat-id)
   :subscription/created-at (Date.)})
