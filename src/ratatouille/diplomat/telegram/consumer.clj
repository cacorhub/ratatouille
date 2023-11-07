(ns ratatouille.diplomat.telegram.consumer
  (:require [clojure.java.io :as io]
            [datomic.client.api :as dl]
            [io.pedestal.interceptor :as interceptor]
            [common-clj.component.telegram.diplomat.http-client :as component.telegram.diplomat.http-client]
            [morse.api :as morse-api]
            [ratatouille.adapters.subscription :as adapters.subscription]
            [ratatouille.db.datomic.subscription :as database.subscription]
            [ratatouille.controllers.menu :as controllers.menu]
            [common-clj.error.core :as error]))

(def admin-interceptor
  (interceptor/interceptor {:name  :admin-user
                            :enter (fn [{{:update/keys [chat-id]} :update
                                         {:keys [config]}         :components :as context}]
                                     (let [admin-user-ids (-> config :telegram :admin-user-ids)]
                                       (if (admin-user-ids (str chat-id))
                                         context
                                         (error/http-friendly-exception 430 "forbidden" "you are not admin" {:chat-id chat-id}))))}))

(defn upsert-menu!
  [{{:update/keys [message file-id]}     :update
    {:keys [config datomic http-client]} :components}]
  (let [menu-file-output (io/file "resources/menu.jpg")]
    (-> (component.telegram.diplomat.http-client/fetch-telegram-file-path file-id
                                                                          (-> config :telegram :token)
                                                                          http-client)
        (component.telegram.diplomat.http-client/download-telegram-document! (-> config :telegram :token) http-client)
        (io/copy menu-file-output)))
  (controllers.menu/notify-menu-update! config (-> datomic :connection dl/db)))

(defn subscribe-to-menu-updates!
  [{{:update/keys [chat-id]} :update
    {:keys [config datomic]} :components}]
  (let [{:subscription/keys [id] :as subscription} (adapters.subscription/wire->subscription chat-id)]
    (if-not (database.subscription/lookup id (-> datomic :connection dl/db))
      (do (database.subscription/insert! subscription (:connection datomic))
          (morse-api/send-text (-> config :telegram :token)
                               chat-id
                               "Inscrição concluida com sucesso. Agora você vai receber uma notificação quando o cadápio for atualizado."))
      (morse-api/send-text (-> config :telegram :token)
                           chat-id
                           "Você já está inscrito."))))

(def consumers
  {:interceptors [admin-interceptor]
   :bot-command  {:atualizar-cardapio {:interceptors [:admin-user]
                                       :handler      upsert-menu!}
                  :notificar-cardapio {:handler subscribe-to-menu-updates!}}})