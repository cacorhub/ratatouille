(ns ratatouille.controllers.user
  (:require [datomic.client.api :as dl]
            [ratatouille.models.user :as models.user]
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

(s/defn activate!
  [user-telegram-chat-id :- s/Str
   chat-id :- s/Str
   datomic-connection
   config]
  (let [{:user/keys [id status] :as user} (database.user/lookup-by-telegram-chat-id user-telegram-chat-id (dl/db datomic-connection))]
    (when (= status :user.status/pending-activation)
      (database.user/activate! id datomic-connection))
    (if user
      (do (diplomat.telegram.producer/notify-user-activation! chat-id config)
          (diplomat.telegram.producer/notify-user-activation! (:user/telegram-chat-id user) config))
      (diplomat.telegram.producer/notify-user-not-found! chat-id config))))
