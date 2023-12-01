(ns ratatouille.models.reservation
  (:require [schema.core :as s])
  (:import (java.time Instant)))

(def statuses #{:reservation.status/ready :reservation.status/redeemed})
(def Status (apply s/enum statuses))

(def reservation-skeleton
  {:reservation/id                           s/Uuid
   :reservation/meal-id                      s/Uuid
   (s/optional-key :reservation/redeemed-at) Instant
   :reservation/created-at                   Instant
   :reservation/status                       Status})

(s/defschema Reservation
  reservation-skeleton)
