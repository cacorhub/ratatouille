(ns ratatouille.controllers.user
  (:require
   [datomic.client.api :as dl]
   [ratatouille.db.datomic.user :as database.user]
   [ratatouille.diplomat.telegram.producer :as diplomat.telegram.producer]
   [ratatouille.models.user :as models.user]
   [schema.core :as s]))

(s/defn create! :- models.user/User
  [user :- models.user/User
   datomic-connection
   telegram-producer]
  (database.user/insert! user datomic-connection)
  (diplomat.telegram.producer/notify-user-creation! (:user/telegram-chat-id user) telegram-producer)
  user)

(s/defn activate!
  [user-telegram-chat-id :- s/Str
   chat-id :- s/Str
   datomic-connection
   telegram-producer]
  (let [{:user/keys [id status] :as user} (database.user/lookup-by-telegram-chat-id user-telegram-chat-id (dl/db datomic-connection))]
    (when (= status :user.status/pending-activation)
      (database.user/activate! id datomic-connection))
    (if user
      (do (diplomat.telegram.producer/notify-user-activation! chat-id telegram-producer)
          (diplomat.telegram.producer/notify-user-activation! (:user/telegram-chat-id user) telegram-producer))
      (diplomat.telegram.producer/notify-user-not-found! chat-id telegram-producer))))
