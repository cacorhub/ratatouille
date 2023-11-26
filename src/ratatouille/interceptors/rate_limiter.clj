(ns ratatouille.interceptors.rate-limiter
  (:require
   [clj-rate-limiter.core :as r]
   [common-clj.error.core :as common-error]
   [iapetos.core :as prometheus]
   [io.pedestal.interceptor :as pedestal.interceptor]))

(def rate-limiters-definition
  {:four-requests-per-min (r/rate-limiter-factory :memory
                                                  :interval 60000
                                                  :max-in-interval 4)})

(def rate-limit-four-per-min-based-on-ip-interceptor
  (pedestal.interceptor/interceptor
   {:name  ::rate-limit-four-per-min-based-on-ip-interceptor
    :enter (fn [{{:keys [components remote-addr]} :request :as context}]
             (let [{:keys [rate-limiter prometheus]} components]
               (when-not (r/allow? (get @rate-limiter :four-requests-per-min) remote-addr)
                 (prometheus/inc (:registry prometheus) :ratatouille/rate-limiter {:route-name (-> context :route :route-name)})
                 (common-error/http-friendly-exception 429
                                                       "too-many-requests"
                                                       "Too Many Requests"
                                                       {:error :too-many-requests})))
             context)}))
