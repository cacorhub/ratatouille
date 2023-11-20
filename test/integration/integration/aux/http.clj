(ns integration.aux.http
  (:require [io.pedestal.test :as test]
            [cheshire.core :as json]
            [iapetos.core :as prometheus]))

(defn create-user!
  [user
   service-fn]
  (let [{:keys [body status]} (test/response-for service-fn
                                                 :post "/api/users"
                                                 :headers {"Content-Type" "application/json"}
                                                 :body (json/encode {:user user}))]
    {:status status
     :body   (json/decode body true)}))

(defn fetch-metrics
  [token
   service-fn]
  (let [{:keys [body status]} (test/response-for service-fn
                                                 :get "/metrics"
                                                 :headers {"Authorization" (str "Bearer " token)})]
    {:status status
     :body   body}))