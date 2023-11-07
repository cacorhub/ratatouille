(ns ratatouille.db.datomic.config
  (:require [ratatouille.wire.datomic.subscription :as wire.datomic.subscription]))

(def schemas (concat []
                     wire.datomic.subscription/subscription-skeleton))