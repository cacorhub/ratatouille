(ns ratatouille.controllers.user
  (:require [ratatouille.models.user :as models.user]
            [schema.core :as s]
            [ratatouille.db.datomic.user :as database.user]))

(s/defn create!
  [user :- models.user/User
   datomic-connection]
  (database.user/insert! user datomic-connection))