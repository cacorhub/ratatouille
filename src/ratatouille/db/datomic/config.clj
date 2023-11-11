(ns ratatouille.db.datomic.config
  (:require [ratatouille.wire.datomic.subscription :as wire.datomic.subscription]
            [ratatouille.wire.datomic.user :as wire.datomic.user]))

(def schemas (concat []
                     wire.datomic.subscription/subscription-skeleton
                     wire.datomic.user/user-skeleton))
