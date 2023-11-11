(ns ratatouille.adapters.user
  (:require [common-clj.keyword.core :as common-keyword]
            [schema.core :as s]
            [ratatouille.models.user :as models.user]
            [ratatouille.wire.in.user :as wire.in.user]
            [ratatouille.wire.out.user :as wire.out.user]))

(s/defn wire->internal :- models.user/User
  [{:keys [cpf name telegram-chat-id]} :- wire.in.user/User]
  {:user/id               (random-uuid)
   :user/cpf              cpf
   :user/name             name
   :user/telegram-chat-id telegram-chat-id
   :user/status           :user.status/pending-activation})

(s/defn internal->wire :- wire.out.user/User
  [{:user/keys [id cpf name telegram-chat-id status]} :- models.user/User]
  {:id               id
   :cpf              cpf
   :name             name
   :telegram-chat-id telegram-chat-id
   :status           (common-keyword/un-namespaced status)})