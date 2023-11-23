(ns ratatouille.job
  (:require [com.stuartsierra.component :as component]
            [overtone.at-at :as at-at]
            [ratatouille.controllers.meal :as controllers.meal]
            [taoensso.timbre :as log]))

(defrecord Jobs [config datomic]
  component/Lifecycle
  (start [component]
    (let [pool (at-at/mk-pool)]

      (at-at/interspaced 30000 #(try (controllers.meal/mise-in-place-meal-job! (-> datomic :datomic :connection))
                                     (catch Exception ex
                                       (log/error ex))) pool)

      (merge component {:jobs {:pool pool}})))

  (stop [{:keys [service]}]
    (at-at/stop-and-reset-pool! (:pool service) :stop)))

(defn new-jobs []
  (->Jobs {} {}))