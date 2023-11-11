(ns ratatouille.wire.out.user
  (:require [common-clj.schema.core :as common-schema]
            [schema-tools.core :as schema-tools]
            [ratatouille.models.user :as models.user]
            [schema.core :as s]))

(s/defschema User
  (-> (schema-tools/select-keys models.user/User [:user/id :user/cpf :user/name :user/telegram-chat-id])
      common-schema/un-namespaced))