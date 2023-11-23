(ns fixtures.meal
  (:require [clj-time.core :as t]
            [clojure.test :refer :all])
  (:import (java.util Date)))

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

(def datomic-created-at (Date.))
(def datomic-reference-date "1998-12-26")

(def datomic-meal-lunch
  {:meal/id             meal-id
   :meal/reference-date datomic-reference-date
   :meal/type           :meal.type/lunch
   :meal/created-at     datomic-created-at})

(def datomic-meal-dinner
  (assoc datomic-meal-lunch :meal/type :meal.type/dinner))
