(ns ratatouille.diplomat.http-server.user
  (:require [schema.core :as s]
            [ratatouille.controllers.user :as controllers.user]
            [ratatouille.adapters.user :as adapters.user]))

(s/defn create!
  [{{:keys [user]}           :json-params
    {:keys [datomic config]} :components}]
  {:status 200
   :body   {:user (-> (adapters.user/wire->internal user)
                      (controllers.user/create! config (:connection datomic))
                      adapters.user/internal->wire)}})