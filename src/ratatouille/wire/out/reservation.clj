(ns ratatouille.wire.out.reservation
  (:require [common-clj.keyword.core :as common-keyword]
            [common-clj.schema.core :as common-schema]
            [ratatouille.models.reservation :as models.reservation]
            [schema.core :as s]
            [schema-tools.core :as schema-tools]))

(def Status (->> models.reservation/statuses
                 (map common-keyword/un-namespaced)
                 (apply s/enum)))

(s/defschema Reservation
  (-> models.reservation/Reservation
      common-schema/un-namespaced
      (schema-tools/assoc :status Status)))