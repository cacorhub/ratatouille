(ns ratatouille.db.datomic.meal-test
  (:require [clojure.test :refer :all]
            [clj-time.types :as t-types]
            [common-clj.component.datomic :as component.datomic]
            [datomic.client.api :as dl]
            [ratatouille.db.datomic.config :as datomic.config]
            [ratatouille.db.datomic.meal :as database.meal]
            [matcher-combinators.test :refer [match?]]
            [schema.test :as s]
            [fixtures.meal]))

(s/deftest insert-test
  (let [mocked-datomic (component.datomic/mocked-datomic-local datomic.config/schemas)]
    (testing "Given a internal model meal entity, we can persist it in the database"
      (database.meal/insert! fixtures.meal/meal-lunch mocked-datomic)
      (is (match? {:meal/id             fixtures.meal/meal-id
                   :meal/type           :meal.type/lunch
                   :meal/reference-date fixtures.meal/reference-date
                   :meal/created-at     t-types/date-time?}
                  (database.meal/lookup fixtures.meal/meal-id (dl/db mocked-datomic)))))))
