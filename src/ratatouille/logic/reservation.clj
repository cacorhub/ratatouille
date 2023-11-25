(ns ratatouille.logic.reservation
  (:require [ratatouille.models.meal :as models.meal]
            [schema.core :as s]
            [ratatouille.models.reservation :as models.reservation])
  (:import (java.util Date)))

(s/defn ->reservation :- models.reservation/Reservation
  [meal-id :- s/Uuid
   user-id :- s/Uuid]
  {:reservation/id         (random-uuid)
   :reservation/user-id    user-id
   :reservation/meal-id    meal-id
   :reservation/created-at (Date.)
   :reservation/status     :reservation.status/ready})