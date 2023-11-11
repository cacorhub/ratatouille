(ns ratatouille.wire.out.user
  (:require [common-clj.schema.core :as common-schema]
            [schema-tools.core :as schema-tools]
            [ratatouille.models.user :as models.user]
            [common-clj.keyword.core :as common-keyword]
            [schema.core :as s]))

(def Status (->> models.user/statuses
                 (map common-keyword/un-namespaced)
                 (apply s/enum)))

(s/defschema User
  (-> (schema-tools/select-keys models.user/User [:user/id :user/cpf :user/name :user/telegram-chat-id])
      common-schema/un-namespaced
      (schema-tools/assoc :status Status)))