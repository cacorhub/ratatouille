(ns ratatouille.diplomat.prometheus
  (:require [iapetos.core :as prometheus]))

(def metrics-definition
  [(prometheus/counter :ratatouille/rate-limiter {:labels [:route]})])
