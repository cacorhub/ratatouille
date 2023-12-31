(ns ratatouille.components
  (:require [com.stuartsierra.component :as component]
            [common-clj.component.config :as component.config]
            [common-clj.component.datomic :as component.datomic]
            [common-clj.component.http-client :as component.http-client]
            [common-clj.component.prometheus :as component.prometheus]
            [common-clj.component.rate-limiter :as component.rate-limiter]
            [common-clj.component.routes :as component.routes]
            [common-clj.component.service :as component.service]
            [common-clj.component.telegram.consumer :as component.telegram.consumer]
            [common-clj.component.telegram.producer :as component.telegram.producer]
            [ratatouille.db.datomic.config :as datomic.config]
            [ratatouille.diplomat.http-server :as diplomat.http-server]
            [ratatouille.diplomat.prometheus :as diplomat.prometheus]
            [ratatouille.diplomat.telegram.consumer :as diplomat.telegram.consumer]
            [ratatouille.interceptors.rate-limiter :as interceptors.rate-limiter]
            [ratatouille.job :as job]))

(def system
  (component/system-map
   :config (component.config/new-config "resources/config.edn" :prod :edn)
   :datomic (component/using (component.datomic/new-datomic-local datomic.config/schemas) [:config])
   :jobs (component/using (job/new-jobs) [:config :datomic])
   :http-client (component/using (component.http-client/new-http-client) [:config])
   :telegram-producer (component/using (component.telegram.producer/new-telegram-producer) [:config])
   :telegram-consumer (component/using (component.telegram.consumer/new-telegram-consumer diplomat.telegram.consumer/consumers) [:config :http-client :datomic :telegram-producer])
   :routes (component/using (component.routes/new-routes diplomat.http-server/routes) [:config])
   :rate-limiter (component.rate-limiter/new-rate-limiter interceptors.rate-limiter/rate-limiters-definition)
   :prometheus (component/using (component.prometheus/new-prometheus diplomat.prometheus/metrics-definition) [:config])
   :service (component/using (component.service/new-service) [:routes :config :datomic :rate-limiter :prometheus :telegram-producer])))

(defn start-system! []
  (component/start system))

(def system-test
  (component/system-map
   :config (component.config/new-config "resources/config.example.edn" :test :edn)
   :datomic (component/using (component.datomic/new-datomic-local datomic.config/schemas) [:config])
   :jobs (component/using (job/new-jobs) [:config :datomic])
   :http-client (component/using (component.http-client/new-http-client) [:config])
   :telegram-producer (component/using (component.telegram.producer/new-telegram-producer) [:config])
   :telegram-consumer (component/using (component.telegram.consumer/new-mock-telegram-consumer diplomat.telegram.consumer/consumers) [:config :http-client :datomic :telegram-producer])
   :routes (component/using (component.routes/new-routes diplomat.http-server/routes) [:config])
   :rate-limiter (component.rate-limiter/new-rate-limiter interceptors.rate-limiter/rate-limiters-definition)
   :prometheus (component/using (component.prometheus/new-prometheus diplomat.prometheus/metrics-definition) [:config])
   :service (component/using (component.service/new-service) [:routes :config :datomic :rate-limiter :prometheus :telegram-producer])))
