(ns ratatouille.controllers.user
  (:require [datomic.client.api :as dl]
            [morse.api :as morse-api]
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
   {:keys [telegram]}]
  (let [{:user/keys [id status] :as user} (database.user/lookup-by-telegram-chat-id user-telegram-chat-id (dl/db datomic-connection))]
    (when (= status :user.status/pending-activation)
      (database.user/activate! id datomic-connection))
    (if user
      (do (morse-api/send-text (:token telegram)
                               chat-id
                               (str "O usuário foi ativado com sucesso."))
          (morse-api/send-text (:token telegram)
                               chat-id
                               (str "A sua conta foi ativada.")))
      (morse-api/send-text (:token telegram)
                           chat-id
                           (str "Usuário não encontrado")))))
