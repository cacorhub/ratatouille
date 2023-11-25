(ns ratatouille.diplomat.telegram.consumer
  (:require [cheshire.core :as json]
            [clojure.java.io :as io]
            [datomic.client.api :as dl]
            [io.pedestal.interceptor :as interceptor]
            [common-clj.component.telegram.diplomat.http-client :as component.telegram.diplomat.http-client]
            [morse.api :as morse-api]
            [ratatouille.adapters.subscription :as adapters.subscription]
            [ratatouille.controllers.menu :as controllers.menu]
            [ratatouille.controllers.subscription :as controllers.subscription]
            [common-clj.error.core :as error]
            [ratatouille.interceptors.user :as interceptors.user]
            [ratatouille.controllers.reservation :as controllers.reservation]))

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

(defn menu
  [{{:update/keys [chat-id] :as update} :update
    {:keys [config datomic]}            :components}]
  (morse-api/send-text (-> (:telegram config) :token)
                       chat-id
                       {:reply_markup (json/generate-string {:inline_keyboard [[{:text "Reservar Almoço" :callback_data "/reserve_lunch"}]
                                                                               [{:text "Reservar Janta" :callback_data "/reserve_dinner"}]]})}
                       "==Opções=="))

(defn reserve-lunch!
  [{{:update/keys [chat-id] :as update} :update
    {:keys [config datomic]}            :components}]
  (controllers.reservation/reserve-lunch! chat-id config))

(def consumers
  {:interceptors [admin-interceptor interceptors.user/active-user-check-interceptor]
   :bot-command  {:update_menu   {:interceptors [:admin-user]
                                  :handler      upsert-menu!}
                  :reserve_lunch {:interceptors [:active-user-check-interceptor]
                                  :handler      reserve-lunch!}
                  :menu          {:handler menu}
                  :start         {:handler subscribe-to-bot!}}})
