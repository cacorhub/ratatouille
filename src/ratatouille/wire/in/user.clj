(ns ratatouille.wire.in.user
  (:require [ratatouille.models.user :as models.user]
            [common-clj.schema.core :as common-schema]
            [schema-tools.core :as schema-tools]
            [schema.core :as s]))

(s/defschema User
  (-> (schema-tools/select-keys models.user/User [:user/cpf :user/name :user/telegram-chat-id])
      common-schema/un-namespaced))
