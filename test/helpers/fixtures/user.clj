(ns fixtures.user
  (:require [clojure.test :refer :all]))

(def user-id (random-uuid))
(def cpf "03547589002")
(def name "Manuel Gomes")
(def telegram-chat-id "123456789")

(def user
  {:user/id               user-id
   :user/cpf              cpf
   :user/name             name
   :user/telegram-chat-id telegram-chat-id
   :user/status           :user.status/pending-activation})

(def wire-user
  {:name             name
   :cpf              cpf
   :telegram-chat-id telegram-chat-id})