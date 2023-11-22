(ns fixtures.meal
  (:require [clj-time.core :as t]
            [clojure.test :refer :all]))

(def meal-id (random-uuid))
(def reference-date (t/local-date 1998 12 26))
(def created-at (t/now))

(def meal-lunch
  {:meal/id             meal-id
   :meal/reference-date reference-date
   :meal/type           :meal.type/lunch
   :meal/created-at     created-at})

(def meal-dinner
  (assoc meal-lunch :meal/type :meal.type/dinner))