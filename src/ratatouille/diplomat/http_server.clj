(ns ratatouille.diplomat.http-server
  (:require [ratatouille.diplomat.http-server.user :as diplomat.http-server.user]))

(def routes [["/api/files" :post [diplomat.http-server.user/create!] :route-name :create-user]])