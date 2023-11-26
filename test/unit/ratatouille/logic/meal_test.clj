(ns ratatouille.logic.meal-test
  (:require [clojure.test :refer :all]
            [java-time.api :as jt]
            [ratatouille.logic.meal :as logic.meal]
            [matcher-combinators.test :refer [match?]]
            [schema.test :as s]))

(s/deftest ->meal-test
  (testing "Given a reference-date and type we can create a meal entity"
    (is (match? {:meal/created-at     jt/instant?
                 :meal/id             uuid?
                 :meal/reference-date (jt/local-date 1998 12 26)
                 :meal/type           :meal.type/lunch}
                (logic.meal/->meal (jt/local-date 1998 12 26) :meal.type/lunch)))

    (is (match? {:meal/created-at     jt/instant?
                 :meal/id             uuid?
                 :meal/reference-date (jt/local-date 2023 12 26)
                 :meal/type           :meal.type/dinner}
                (logic.meal/->meal (jt/local-date 2023 12 26) :meal.type/dinner)))))
