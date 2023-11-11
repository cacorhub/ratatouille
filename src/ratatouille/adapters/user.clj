(ns ratatouille.adapters.user
  (:require [schema.core :as s]
            [ratatouille.models.user :as models.user]
            [ratatouille.wire.in.user :as wire.in.user]))

(s/defn wire->internal :- models.user/User
  [{:keys [cpf name telegram-chat-id]} :- wire.in.user/User]
  {:user/id               (random-uuid)
   :user/cpf              cpf
   :user/name             name
   :user/telegram-chat-id telegram-chat-id})