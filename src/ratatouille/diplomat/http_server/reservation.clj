(ns ratatouille.diplomat.http-server.reservation
  (:require [schema.core :as s]))

(s/defn create!
  [{{:keys [challenge-id]}              :path-params
    {:keys [datomic telegram-producer]} :components}]
  {:status 200
   :body   {}})