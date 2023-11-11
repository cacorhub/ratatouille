(ns ratatouille.diplomat.http-server
  (:require [ratatouille.diplomat.http-server.user :as diplomat.http-server.user]
            [ratatouille.interceptors.user :as interceptors.user]))

(def routes [["/api/users" :post [interceptors.user/cpf-validation-interceptor
                                  diplomat.http-server.user/create!] :route-name :create-user]])