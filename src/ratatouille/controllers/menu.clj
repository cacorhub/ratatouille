(ns ratatouille.controllers.menu
  (:require [clojure.java.io :as io]
            [morse.api :as morse-api]
            [schema.core :as s]
            [ratatouille.db.datomic.subscription :as database.subscription]))

(s/defn notify-menu-update!
  [config
   datomic-db]
  (doseq [{:subscription/keys [chat-id]} (database.subscription/lookup-all datomic-db)]
    (morse-api/send-photo (-> config :telegram :token) chat-id
                          {:caption "O cardápio foi atualizado."}
                          (io/as-file "resources/menu.jpg"))))
