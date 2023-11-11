(ns ratatouille.controllers.user
  (:require [ratatouille.models.user :as models.user]
            [schema.core :as s]
            [ratatouille.db.datomic.user :as database.user]
            [ratatouille.diplomat.telegram.producer :as diplomat.telegram.producer]))

(s/defn create! :- models.user/User
  [user :- models.user/User
   config
   datomic-connection]
  (database.user/insert! user datomic-connection)
  (diplomat.telegram.producer/notify-user-creation! (:user/telegram-chat-id user) config)
  user)