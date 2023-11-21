(ns ratatouille.diplomat.telegram.producer
  (:require [morse.api :as morse-api]
            [schema.core :as s]))

(s/defn notify-user-creation!
  [telegram-chat-id :- s/Str
   {:keys [telegram]}]
  (morse-api/send-text (:token telegram)
                       telegram-chat-id
                       (str "Conta criada com sucesso, esse é o seu código de ativação: " telegram-chat-id)))

(s/defn notify-user-activation!
  [telegram-chat-id :- s/Str
   {:keys [telegram]}]
  (morse-api/send-text (:token telegram)
                       telegram-chat-id
                       "O usuário foi ativado com sucesso."))

(s/defn notify-user-not-found!
  [telegram-chat-id :- s/Str
   {:keys [telegram]}]
  (morse-api/send-text (:token telegram)
                       telegram-chat-id
                       "Usuário não encontrado"))
