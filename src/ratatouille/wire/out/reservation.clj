(ns ratatouille.wire.out.reservation
  (:require [common-clj.keyword.core :as common-keyword]
            [common-clj.schema.core :as common-schema]
            [ratatouille.models.reservation :as models.reservation]
            [schema-tools.core :as schema-tools]
            [schema.core :as s]))

(def Status (->> models.reservation/statuses
                 (map common-keyword/un-namespaced)
                 (apply s/enum)))

(s/defschema Reservation
  (-> models.reservation/Reservation
      common-schema/un-namespaced
      (schema-tools/dissoc :created-at)
      (schema-tools/assoc :status Status)))
