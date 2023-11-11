(ns fixtures.user
  (:require [clojure.test :refer :all]))

(def user-id (random-uuid))
(def cpf "035.475.890-02")
(def name "Manuel Gomes")
(def telegram-chat-id "123456789")

(def user
  {:user/id               user-id
   :user/cpf              cpf
   :user/name             name
   :user/telegram-chat-id telegram-chat-id})

(def wire-user
  {:name             name
   :cpf              cpf
   :telegram-chat-id telegram-chat-id})