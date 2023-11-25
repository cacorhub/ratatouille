(ns ratatouille.interceptors.user
  (:require [common-clj.error.core :as common-error]
            [datomic.client.api :as dl]
            [io.pedestal.interceptor :as pedestal.interceptor]
            [cadastro-de-pessoa.cpf]
            [morse.api :as morse-api]
            [ratatouille.db.datomic.user :as database.user]))

(def cpf-validation-interceptor
  (pedestal.interceptor/interceptor {:name  ::cpf-validation-interceptor
                                     :enter (fn [{{:keys [json-params]} :request :as context}]
                                              (let [cpf (-> json-params :user :cpf)]
                                                (when-not (cadastro-de-pessoa.cpf/valid? cpf)
                                                  (common-error/http-friendly-exception 400
                                                                                        "invalid-cpf"
                                                                                        "The CPF provided is not valid"
                                                                                        {:cpf cpf})))
                                              context)}))

(def already-taken-cpf-check-interceptor
  (pedestal.interceptor/interceptor {:name  ::already-taken-cpf-check-interceptor
                                     :enter (fn [{{:keys [json-params components]} :request :as context}]
                                              (let [datomic-connection (-> components :datomic :connection)
                                                    cpf (-> json-params :user :cpf)]
                                                (when (database.user/lookup-by-cpf cpf (dl/db datomic-connection))
                                                  (common-error/http-friendly-exception 400
                                                                                        "cpf-already-taken"
                                                                                        "The CPF provided is already in use"
                                                                                        {:cpf cpf})))
                                              context)}))

(def active-user-check-interceptor
  (pedestal.interceptor/interceptor {:name  :active-user-check-interceptor
                                     :enter (fn [{{:update/keys [chat-id]} :update
                                                  {:keys [config datomic]} :components :as context}]
                                              (let [{:user/keys [status]} (database.user/lookup-by-telegram-chat-id (str chat-id) (-> datomic :connection dl/db))]
                                                (if (= status :user.status/pending-activation)
                                                  (morse-api/send-text (-> (:telegram config) :token)
                                                                       chat-id
                                                                       "O cadastro ainda não foi ativado, ative o seu cadastro primeiro.")
                                                  context)))}))
