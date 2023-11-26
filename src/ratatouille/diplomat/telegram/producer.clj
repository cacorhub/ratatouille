(ns ratatouille.diplomat.telegram.producer
  (:require
   [common-clj.component.telegram.producer :as component.telegram.producer]
   [morse.api :as morse-api]
   [schema.core :as s])
  (:import
   (java.io File)))

(s/defn notify-user-creation!
  [telegram-chat-id :- s/Str
   telegram-producer]
  (component.telegram.producer/send-text! {:chat-id telegram-chat-id
                                           :text    (str "Conta criada com sucesso, esse é o seu código de ativação: " telegram-chat-id)}
                                          telegram-producer))

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

(s/defn notify-lunch-reservation-outside-time-window!
  [telegram-chat-id :- s/Str
   {:keys [telegram]}]
  (morse-api/send-text (:token telegram)
                       telegram-chat-id
                       "Reserva não concluida. Reservas de almoço só podem ser realizadas de 06:00 até 10:00 da manhã."))

(s/defn notify-dinner-reservation-outside-time-window!
  [telegram-chat-id :- s/Str
   {:keys [telegram]}]
  (morse-api/send-text (:token telegram)
                       telegram-chat-id
                       "Reserva não concluida. Reservas de janta só podem ser realizadas de 12:00 até 16:00 da tarde."))

(s/defn notify-reservation-qr-code!
  [telegram-chat-id :- s/Str
   qr-code-file :- File
   {:keys [telegram]}]
  (morse-api/send-photo (:token telegram) telegram-chat-id
                        {:caption "Ficha de reserva"}
                        qr-code-file))

(s/defn notify-reservations-over-limit
  [telegram-chat-id :- s/Str
   {:keys [telegram]}]
  (morse-api/send-text (:token telegram)
                       telegram-chat-id
                       "Reserva não concluida. Atingimos o limite máximo diário de reservas."))
