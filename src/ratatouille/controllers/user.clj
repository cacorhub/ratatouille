(ns ratatouille.controllers.user
  (:require [datomic.client.api :as dl]
            [morse.api :as morse-api]
            [ratatouille.adapters.user :as adapters.user]
            [ratatouille.models.user :as models.user]
            [schema.core :as s]
            [ratatouille.db.datomic.user :as database.user]
            [ratatouille.diplomat.telegram.producer :as diplomat.telegram.producer]
            [ratatouille.db.datomic.password :as database.password]
            [common-clj.auth.core :as common-auth]
            [common-clj.error.core :as common-error]))

(s/defn create! :- models.user/User
  [user :- models.user/User
   config
   datomic-connection]
  (database.user/insert! user datomic-connection)
  (diplomat.telegram.producer/notify-user-creation! (:user/telegram-chat-id user) config)
  user)

(s/defn authentication! :- s/Str
  [auth
   {:keys [jwt-secret telegram]}
   database-connection]
  (let [{:user/keys [id telegram-chat-id] :as user} (database.user/lookup-by-cpf (:auth/cpf auth) (dl/db database-connection))
        {:password/keys [one-time-password message-id] :as password} (first (database.password/ready-passwords-by-user-id id (dl/db database-connection)))]
    (if (and user (= (:auth/password auth) #p one-time-password))
      (do
        (database.password/used! (:password/id password) database-connection)
        (morse-api/edit-text (:token telegram)
                               telegram-chat-id
                               message-id
                             (str "Senha de uso único: " one-time-password " (esta senha já foi utilizada, gere uma nova na próxima vez que for entrar no sistema)"))
        (-> {:user (adapters.user/internal->wire user)}
            (common-auth/->token jwt-secret)))
      (common-error/http-friendly-exception 403
                                            "invalid-credentials"
                                            "Wrong username or/and password"
                                            "Customer is trying to login using invalid credentials"))))
