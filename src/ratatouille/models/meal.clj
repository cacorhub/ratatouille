(ns ratatouille.models.meal
  (:require [schema.core :as s])
  (:import (org.joda.time DateTime LocalDate)))

(def types #{:meal.type/lunch :meal.type/dinner})
(def Type (apply s/enum types))

(def meal-skeleton
  {:meal/id             s/Uuid
   :meal/reference-date LocalDate
   :meal/type           Type
   :meal/created-at     DateTime})

(s/defschema Meal meal-skeleton)
