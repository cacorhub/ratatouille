(ns ratatouille.models.meal
  (:require [schema.core :as s])
  (:import (java.time Instant LocalDate)
           (org.joda.time DateTime)))

(def types #{:meal.type/lunch :meal.type/dinner})
(def Type (apply s/enum types))

(def meal-skeleton
  {:meal/id             s/Uuid
   :meal/reference-date LocalDate
   :meal/type           Type
   :meal/created-at     Instant})

(s/defschema Meal meal-skeleton)
