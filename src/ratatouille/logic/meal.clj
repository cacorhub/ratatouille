(ns ratatouille.logic.meal
  (:require [clj-time.core :as t]
            [ratatouille.models.meal :as models.meal]
            [schema.core :as s])
  (:import (java.time LocalDate)))

(s/defn ->meal :- models.meal/Meal
  [reference-date :- LocalDate
   type :- models.meal/Type]
  {:meal/id             (random-uuid)
   :meal/reference-date reference-date
   :meal/created-at     (t/now)
   :meal/type           type})
