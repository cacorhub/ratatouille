(ns ratatouille.adapters.reservation
  (:require
    [common-clj.keyword.core :as common-keyword]
    [java-time.api :as jt]
    [plumbing.core :as plumbing]
    [ratatouille.models.reservation :as models.reservation]
    [ratatouille.wire.datomic.reservation :as wire.datomic.reservation]
    [ratatouille.wire.out.reservation :as wire.out.reservation]
    [schema.core :as s]))

(s/defn internal->datomic :- wire.datomic.reservation/Reservation
  [{:reservation/keys [created-at redeemed-at] :as reservation} :- models.reservation/Reservation]
  (plumbing/assoc-when reservation :reservation/created-at (jt/java-date created-at)
                       :reservation/redeemed-at (when redeemed-at
                                                  (jt/java-date redeemed-at))))

(s/defn datomic->internal :- models.reservation/Reservation
  [{:reservation/keys [created-at redeemed-at] :as reservation} :- wire.datomic.reservation/Reservation]
  (plumbing/assoc-when reservation :reservation/created-at (jt/instant created-at)
                       :reservation/redeemed-at (when redeemed-at
                                                  (jt/instant redeemed-at))))

(s/defn internal->wire :- wire.out.reservation/Reservation
  [{:reservation/keys [id meal-id status]} :- models.reservation/Reservation]
  {:id      id
   :meal-id meal-id
   :status  (common-keyword/un-namespaced status)})
