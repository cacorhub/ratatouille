(ns ratatouille.controllers.subscription
  (:require
   [common-clj.component.telegram.models.update :as component.telegram.models.update]
   [datomic.client.api :as dl]
   [morse.api :as morse-api]
   [ratatouille.db.datomic.subscription :as database.subscription]
   [ratatouille.db.datomic.user :as database.user]
   [ratatouille.models.subscription :as models.subscription]
   [schema.core :as s]))

(s/defn send-instructions-about-user-creation
  [{:update/keys [chat-id]} :- component.telegram.models.update/Update
   datomic-connection
   {:keys [telegram]}]
  (if-let [{:user/keys [status]} (database.user/lookup-by-telegram-chat-id chat-id (dl/db datomic-connection))]
    (when (= status :user.status/pending-activation)
      (morse-api/send-text (:token telegram)
                           chat-id
                           "A sua conta está com processo de ativação pendente. [Instruções sobre ativação]"))
    (morse-api/send-text (:token telegram)
                         chat-id
                         (str "[Instruções sobre criação da conta de usuário]" "Código para cadastro: " chat-id))))

(s/defn bot-subscription!
  [{:subscription/keys [id] :as subscription} :- models.subscription/Subscription
   {:update/keys [chat-id] :as update} :- component.telegram.models.update/Update
   datomic-connection
   {:keys [telegram] :as config}]
  (if-not (database.subscription/lookup id (dl/db datomic-connection))
    (do (database.subscription/insert! subscription datomic-connection)
        (morse-api/send-text (:token telegram)
                             chat-id
                             "Agora você vai receber uma notificação quando o cadápio for atualizado."))
    (morse-api/send-text (:token telegram)
                         chat-id
                         "Você já está inscrito para ser notificado quando o cadápio for atualizado."))
  (send-instructions-about-user-creation update datomic-connection config))