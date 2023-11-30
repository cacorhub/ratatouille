(ns ratatouille.diplomat.http-server.reservation
  (:require
   [ratatouille.adapters.reservation :as adapters.reservation]
   [ratatouille.controllers.reservation :as controllers.reservation]
   [schema.core :as s])
  (:import
   (java.util UUID)))

(s/defn redeem!
  [{{:keys [reservation-id]} :path-params
    {:keys [datomic]}        :components}]
  {:status 200
   :body   {:reservation (-> (UUID/fromString reservation-id)
                             (controllers.reservation/redeem! (:connection datomic))
                             adapters.reservation/internal->wire)}})