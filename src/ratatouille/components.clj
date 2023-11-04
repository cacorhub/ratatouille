(ns ratatouille.components
  (:require [com.stuartsierra.component :as component]
            [common-clj.component.config :as component.config]
            [common-clj.component.telegram.consumer :as component.telegram.consumer]
            [ratatouille.diplomat.telegram.consumer :as diplomat.telegram.consumer]
            [common-clj.component.datomic :as component.datomic]
            [ratatouille.db.datomic.config :as datomic.config]))

(def system
  (component/system-map
    :config (component.config/new-config "resources/config.edn" :prod :edn)
    :datomic (component/using (component.datomic/new-datomic-local datomic.config/schemas) [:config])
    :telegram-consumer (component/using (component.telegram.consumer/new-telegram-consumer diplomat.telegram.consumer/consumers) [:config])))

(defn start-system! []
  (component/start system))