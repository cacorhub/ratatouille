(ns fixtures.user)

(def user-id (random-uuid))
(def cpf "03547589002")
(def full-name "Manuel Gomes")
(def telegram-chat-id "123456789")

(def user
  {:user/id               user-id
   :user/cpf              cpf
   :user/name             full-name
   :user/telegram-chat-id telegram-chat-id
   :user/status           :user.status/pending-activation})

(def wire-user
  {:name             full-name
   :cpf              cpf
   :telegram-chat-id telegram-chat-id})