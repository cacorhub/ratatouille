(ns ratatouille.diplomat.telegram.consumer
  (:require [clojure.java.io :as io]
            [datomic.client.api :as dl]
            [io.pedestal.interceptor :as interceptor]
            [common-clj.component.telegram.diplomat.http-client :as component.telegram.diplomat.http-client]
            [ratatouille.adapters.subscription :as adapters.subscription]
            [ratatouille.controllers.menu :as controllers.menu]
            [ratatouille.controllers.subscription :as controllers.subscription]
            [common-clj.error.core :as error]
            [ratatouille.db.datomic.user :as database.user]
            [ratatouille.adapters.password :as adapters.password]
            [ratatouille.controllers.password :as controllers.password]))

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

(defn subscribe-to-bot!
  [{{:update/keys [chat-id] :as update} :update
    {:keys [config datomic]}            :components}]
  (-> (adapters.subscription/wire->subscription chat-id)
      (controllers.subscription/bot-subscription! update (:connection datomic) config)))

(defn generate-one-time-password!
  [{{:update/keys [chat-id] :as update} :update
    {:keys [config datomic]}            :components}]
  (let [user (database.user/lookup-by-telegram-chat-id (str chat-id) (dl/db (:connection datomic)))
        password (adapters.password/wire->internal user update)]
    (controllers.password/generate-one-time-password! password config (:connection datomic))))

(def consumers
  {:interceptors [admin-interceptor]
   :bot-command  {:gerar_senha        {:interceptors [:admin-user]
                                       :handler      generate-one-time-password!}
                  :atualizar-cardapio {:interceptors [:admin-user]
                                       :handler      upsert-menu!}
                  :start              {:handler subscribe-to-bot!}}})
