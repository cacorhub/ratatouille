(ns integration.aux.http
  (:require [io.pedestal.test :as test]
            [cheshire.core :as json]))

(defn create-user!
  [user
   service-fn]
  (let [{:keys [body status]} (test/response-for service-fn
                                                 :post "/api/users"
                                                 :headers {"Content-Type" "application/json"}
                                                 :body (json/encode {:user user}))]
    {:status status
     :body   (json/decode body true)}))