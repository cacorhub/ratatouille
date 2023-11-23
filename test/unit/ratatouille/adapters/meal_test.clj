(ns ratatouille.adapters.meal-test
  (:require [clojure.test :refer :all]
            [fixtures.meal]
            [ratatouille.adapters.meal :as adapters.meal]
            [matcher-combinators.test :refer [match?]]
            [clj-time.types :as t-types]
            [schema.test :as s]))

(s/deftest internal->datomic-test
  (testing "Given a internal meal entity, we can export to the datomic schema"
    (is (match? {:meal/created-at     inst?
                 :meal/id             fixtures.meal/meal-id
                 :meal/reference-date "1998-12-26"
                 :meal/type           :meal.type/lunch}
                (adapters.meal/internal->datomic fixtures.meal/meal-lunch)))
    (is (match? {:meal/created-at     inst?
                 :meal/id             fixtures.meal/meal-id
                 :meal/reference-date "1998-12-26"
                 :meal/type           :meal.type/dinner}
                (adapters.meal/internal->datomic fixtures.meal/meal-dinner)))))

(s/deftest datomic->internal-test
  (testing "Given a datomic meal entity, we can convert it to internal model"
    (is (match? {:meal/id             fixtures.meal/meal-id
                 :meal/reference-date fixtures.meal/reference-date
                 :meal/type           :meal.type/lunch
                 :meal/created-at     t-types/date-time?}
                (adapters.meal/datomic->internal fixtures.meal/datomic-meal-lunch)))
    (is (match? {:meal/id             fixtures.meal/meal-id
                 :meal/reference-date fixtures.meal/reference-date
                 :meal/type           :meal.type/dinner
                 :meal/created-at     t-types/date-time?}
                (adapters.meal/datomic->internal fixtures.meal/datomic-meal-dinner)))))
