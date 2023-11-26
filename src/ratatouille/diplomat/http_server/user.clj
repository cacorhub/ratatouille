(ns ratatouille.diplomat.http-server.user
  (:require
   [ratatouille.adapters.user :as adapters.user]
   [ratatouille.controllers.user :as controllers.user]
   [schema.core :as s]))

(s/defn create!
  [{{:keys [user]}                             :json-params
    {:keys [datomic telegram-producer]} :components}]
  {:status 200
   :body   {:user (-> (adapters.user/wire->internal user)
                      (controllers.user/create! (:connection datomic) telegram-producer)
                      adapters.user/internal->wire)}})
