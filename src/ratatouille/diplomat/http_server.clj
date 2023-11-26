(ns ratatouille.diplomat.http-server
  (:require
   [common-clj.component.prometheus :as component.prometheus]
   [ratatouille.diplomat.http-server.user :as diplomat.http-server.user]
   [ratatouille.interceptors.rate-limiter :as interceptors.rate-limiter]
   [ratatouille.interceptors.user :as interceptors.user]))

(def routes [["/api/users" :post [interceptors.rate-limiter/rate-limit-four-per-min-based-on-ip-interceptor
                                  interceptors.user/cpf-validation-interceptor
                                  interceptors.user/already-taken-cpf-check-interceptor
                                  diplomat.http-server.user/create!] :route-name :create-user]
             ["/metrics" :get [component.prometheus/metrics] :route-name :prometheus-metrics]])
