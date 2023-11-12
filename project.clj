(defproject ratatouille "0.1.0"
  :description "A Telegram Bot in order to notify students about school canteen menu"
  :url "https://github.com/macielti/ratatouille"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}

  :dependencies [[org.clojure/clojure "1.11.1"]
                 [net.clojars.macielti/common-clj "23.42.46"]
                 [hashp "0.2.2"]
                 [metosin/schema-tools "0.13.1"]
                 [cadastro-de-pessoa "0.4.1"]
                 [danlentz/clj-uuid "0.1.9"]]

  :injections [(require 'hashp.core)]

  :repl-options {:init-ns ratatouille.components}

  :test-paths ["test/unit" "test/integration" "test/helpers"]

  :main ratatouille.components/start-system!)
