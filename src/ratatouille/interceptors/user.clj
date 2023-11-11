(ns ratatouille.interceptors.user
  (:require [common-clj.error.core :as common-error]
            [io.pedestal.interceptor :as pedestal.interceptor]
            [cadastro-de-pessoa.cpf]))

(def cpf-validation-interceptor
  (pedestal.interceptor/interceptor {:name  ::cpf-validation-interceptor
                                     :enter (fn [{{:keys [json-params]} :request :as context}]
                                              (let [cpf (-> json-params :user :cpf)]
                                                (when-not (cadastro-de-pessoa.cpf/valid? cpf)
                                                  (common-error/http-friendly-exception 400
                                                                                        "invalid-cpf"
                                                                                        "The CPF provided is not valid"
                                                                                        {:cpf cpf})))
                                              context)}))
