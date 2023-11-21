(ns ratatouille.db.datomic.config
  (:require [ratatouille.wire.datomic.subscription :as wire.datomic.subscription]
            [ratatouille.wire.datomic.user :as wire.datomic.user]
            [ratatouille.wire.datomic.password :as wire.datomic.password]))

(def schemas (concat []
                     wire.datomic.subscription/subscription-skeleton
                     wire.datomic.user/user-skeleton
                     wire.datomic.password/password-skeleton))
