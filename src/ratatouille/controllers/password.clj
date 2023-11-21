(ns ratatouille.controllers.password
  (:require [ratatouille.models.password :as models.password]
            [schema.core :as s]
            [ratatouille.db.datomic.password :as database.password]
            [morse.api :as morse-api]))

(s/defn generate-one-time-password!
  [{:password/keys [chat-id one-time-password] :as password} :- models.password/Password
   {:keys [telegram]}
   datomic-connection]
  (let [message-id (-> (morse-api/send-text (:token telegram)
                                            chat-id
                                            (str "Senha de uso único: " one-time-password))
                       :result :message_id str)]
    (database.password/insert! (assoc password :password/message-id message-id) datomic-connection)))