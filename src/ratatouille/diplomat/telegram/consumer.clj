(ns ratatouille.diplomat.telegram.consumer
  (:require
   [cheshire.core :as json]
   [clojure.java.io :as io]
   [clojure.string :as string]
   [common-clj.component.telegram.diplomat.http-client :as component.telegram.diplomat.http-client]
   [common-clj.error.core :as error]
   [datomic.client.api :as dl]
   [io.pedestal.interceptor :as interceptor]
   [java-time.api :as jt]
   [morse.api :as morse-api]
   [ratatouille.adapters.subscription :as adapters.subscription]
   [ratatouille.controllers.menu :as controllers.menu]
   [ratatouille.controllers.reservation :as controllers.reservation]
   [ratatouille.controllers.subscription :as controllers.subscription]
   [ratatouille.controllers.user :as controllers.user]
   [ratatouille.interceptors.reservation :as interceptors.reservation]
   [ratatouille.interceptors.user :as interceptors.user]))

(def admin-interceptor
  (interceptor/interceptor {:name  :admin-user
                            :enter (fn [{{:update/keys [chat-id]} :update
                                         {:keys [config]}         :components :as context}]
                                     (let [admin-user-ids (-> config :telegram :admin-user-ids)]
                                       (if (admin-user-ids (str chat-id))
                                         context
                                         (error/http-friendly-exception 430 "forbidden" "you are not admin" {:chat-id chat-id}))))}))

(defn upsert-menu!
  [{{:update/keys [file-id]}             :update
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

(defn activate-user!
  [{{:update/keys [message chat-id]}    :update
    {:keys [telegram-producer datomic]} :components}]
  (let [args (-> (string/split message #" ") rest)
        user-telegram-chat-id (-> args first Integer/parseInt)]
    (controllers.user/activate! (str user-telegram-chat-id) chat-id (:connection datomic) telegram-producer)))

(defn menu
  [{{:update/keys [chat-id]} :update
    {:keys [config]}         :components}]
  (morse-api/send-text (-> (:telegram config) :token)
                       chat-id
                       {:reply_markup (json/generate-string {:inline_keyboard [[{:text "Reservar Almoço" :callback_data "/reserve_lunch"}]
                                                                               [{:text "Reservar Janta" :callback_data "/reserve_dinner"}]]})}
                       "==Opções=="))

(defn reserve-lunch!
  [{{:update/keys [chat-id]} :update
    {:keys [config datomic telegram-producer]} :components}]
  (controllers.reservation/reserve-lunch! chat-id (jt/local-date-time) (:connection datomic) telegram-producer config))

(defn reserve-dinner!
  [{{:update/keys [chat-id]} :update
    {:keys [config datomic telegram-producer]} :components}]
  (controllers.reservation/reserve-dinner! chat-id (jt/local-date-time) (:connection datomic) telegram-producer config))

(def consumers
  {:bot-command {:atualizar_menu  {:interceptors [admin-interceptor]
                                   :handler      upsert-menu!}
                 :ativar_cadastro {:interceptors [admin-interceptor]
                                   :handler      activate-user!}
                 :reserve_lunch   {:interceptors [interceptors.user/active-user-check-interceptor
                                                  interceptors.reservation/over-limit-reservations-lunch-check-interceptor]
                                   :handler      reserve-lunch!}
                 :reserve_dinner  {:interceptors [interceptors.user/active-user-check-interceptor
                                                  interceptors.reservation/over-limit-reservations-dinner-check-interceptor]
                                   :handler      reserve-dinner!}
                 :menu            {:handler menu}
                 :start           {:handler subscribe-to-bot!}}})
