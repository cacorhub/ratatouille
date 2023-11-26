(ns ratatouille.db.datomic.config
  (:require
   [ratatouille.wire.datomic.meal :as wire.datomic.meal]
   [ratatouille.wire.datomic.reservation :as wire.datomic.reservation]
   [ratatouille.wire.datomic.subscription :as wire.datomic.subscription]
   [ratatouille.wire.datomic.user :as wire.datomic.user]))

(def schemas (concat []
                     wire.datomic.subscription/subscription-skeleton
                     wire.datomic.user/user-skeleton
                     wire.datomic.meal/meal-skeleton
                     wire.datomic.reservation/reservation-skeleton))
