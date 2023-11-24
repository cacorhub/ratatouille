(ns ratatouille.models.reservation
  (:require [schema.core :as s])
  (:import (java.util Date)))

(def statuses #{:reservation.status/ready :reservation.status/redeemed})
(def Status (apply s/enum statuses))

(def reservation-skeleton
  {:reservation/id          s/Uuid
   :reservation/meal-id     s/Uuid
   :reservation/redeemed-at Date
   :reservation/created-at  Date
   :reservation/status      Status})

(s/defschema Reservation
  reservation-skeleton)
