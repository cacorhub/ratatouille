(ns ratatouille.adapters.password
  (:require [clojure.string :as str]
            [ratatouille.models.user :as models.user]
            [schema.core :as s]
            [ratatouille.models.password :as models.password]
            [common-clj.component.telegram.models.update :as component.telegram.models.update])
  (:import (java.util Date)))

(s/defn wire->internal :- models.password/Password
  [{:user/keys [id]} :- models.user/User
   {:update/keys [chat-id]} :- component.telegram.models.update/Update]
  {:password/id                (random-uuid)
   :password/user-id           id
   :password/chat-id           (str chat-id)
   :password/one-time-password (-> (random-uuid) str (str/split #"-") first)
   :password/created-at        (Date.)
   :password/status            :password.status/ready})