(ns ratatouille.adapters.reservation
  (:require [java-time.api :as jt]
            [plumbing.core :as plumbing]
            [ratatouille.models.reservation :as models.reservation]
            [ratatouille.wire.datomic.reservation :as wire.datomic.reservation]
            [schema.core :as s]))

(s/defn internal->datomic :- wire.datomic.reservation/Reservation
  [{:reservation/keys [created-at] :as reservation} :- models.reservation/Reservation]
  (assoc reservation :reservation/created-at (jt/java-date created-at)))

(s/defn datomic->internal :- models.reservation/Reservation
  [{:reservation/keys [created-at redeemed-at] :as reservation} :- wire.datomic.reservation/Reservation]
  (plumbing/assoc-when reservation :reservation/created-at (jt/instant created-at)
                       :reservation/redeemed-at (when redeemed-at
                                                 (jt/instant redeemed-at))))