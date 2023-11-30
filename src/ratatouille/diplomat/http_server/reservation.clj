(ns ratatouille.diplomat.http-server.reservation
  (:require [ratatouille.adapters.reservation :as adapters.reservation]
            [ratatouille.controllers.reservation :as controllers.reservation]
            [schema.core :as s])
  (:import (java.util UUID)))

(s/defn redeem!
  [{{:keys [reservation-id]}            :path-params
    {:keys [datomic telegram-producer]} :components}]
  {:status 200
   :body   {:reservation (-> (UUID/fromString reservation-id)
                             (controllers.reservation/redeem! (:connection datomic) telegram-producer)
                             adapters.reservation/internal->wire)}})