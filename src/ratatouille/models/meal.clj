(ns ratatouille.models.meal
  (:require [schema.core :as s])
  (:import (java.time LocalDate LocalDateTime)))

(def types #{:meal.type/lunch :meal.type/dinner})
(def Type (apply s/enum types))

(def meal-skeleton
  {:meal/id             s/Uuid
   :meal/reference-date LocalDate
   :meal/type           Type
   :meal/created-at     LocalDateTime})

(s/defschema Meal meal-skeleton)