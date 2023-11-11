(ns ratatouille.wire.in.user
  (:require [ratatouille.models.user :as models.user]
            [common-clj.schema.core :as common-schema]
            [schema.core :as s]))

(s/defschema User
  (common-schema/un-namespaced models.user/User))