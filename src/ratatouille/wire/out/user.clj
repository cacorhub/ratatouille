(ns ratatouille.wire.out.user
  (:require [common-clj.keyword.core :as common-keyword]
            [common-clj.schema.core :as common-schema]
            [ratatouille.models.user :as models.user]
            [schema-tools.core :as schema-tools]
            [schema.core :as s]))

(def Status (->> models.user/statuses
                 (map common-keyword/un-namespaced)
                 (apply s/enum)))

(s/defschema User
  (-> (schema-tools/select-keys models.user/User [:user/id :user/cpf :user/name :user/telegram-chat-id])
      common-schema/un-namespaced
      (schema-tools/assoc :status Status)))
