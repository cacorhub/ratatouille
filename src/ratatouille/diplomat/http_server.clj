(ns ratatouille.diplomat.http-server
  (:require
    [common-clj.component.prometheus :as component.prometheus]
    [ratatouille.diplomat.http-server.user :as diplomat.http-server.user]
    [ratatouille.interceptors.rate-limiter :as interceptors.rate-limiter]
    [ratatouille.diplomat.http-server.reservation :as diplomat.http-server.reservation]
    [ratatouille.interceptors.user :as interceptors.user]
    [ratatouille.interceptors.reservation :as interceptors.reservation]))

(def routes [["/api/users" :post [interceptors.rate-limiter/rate-limit-four-per-min-based-on-ip-interceptor
                                  interceptors.user/cpf-validation-interceptor
                                  interceptors.user/already-taken-cpf-check-interceptor
                                  diplomat.http-server.user/create!] :route-name :create-user]
             ["/api/reservations/:reservation-id" :put [interceptors.reservation/authorization-interceptor
                                                        diplomat.http-server.reservation/redeem!] :route-name :redeem-reservation]
             ["/metrics" :get [component.prometheus/metrics] :route-name :prometheus-metrics]])
