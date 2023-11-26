(ns fixtures.meal
  (:require [clj-time.core :as t]
            [clojure.test :refer :all]
            [java-time.api :as jt])
  (:import (java.util Date)))

(def meal-id (random-uuid))
(def reference-date (jt/local-date 1998 12 26))
(def created-at (jt/instant))

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
