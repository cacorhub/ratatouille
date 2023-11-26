(defproject ratatouille "0.1.0"
  :description "A Telegram Bot in order to notify students about school canteen menu"
  :url "https://github.com/macielti/ratatouille"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}

  :plugins [[com.github.clojure-lsp/lein-clojure-lsp "1.4.2"]]

  :dependencies [[org.clojure/clojure "1.11.1"]
                 [net.clojars.macielti/common-clj "24.47.47"]
                 [clojure.java-time "1.4.2"]
                 [hashp "0.2.2"]
                 [metosin/schema-tools "0.13.1"]
                 [cadastro-de-pessoa "0.4.1"]
                 [danlentz/clj-uuid "0.1.9"]
                 [metosin/schema-tools "0.13.1"]
                 [clj.qrgen "0.4.0"]]

  :clojure-lsp {:settings {:clean {:ns-inner-blocks-indentation :same-line}}} ;; API options
  :aliases {"clean-ns"     ["clojure-lsp" "clean-ns" "--dry"] ;; check if namespaces are clean
            "format"       ["clojure-lsp" "format" "--dry"] ;; check if namespaces are formatted
            "diagnostics"  ["clojure-lsp" "diagnostics"]    ;; check if project has any diagnostics (clj-kondo findings)
            "lint"         ["do" ["clean-ns"] ["format"] ["diagnostics"]] ;; check all above

            "clean-ns-fix" ["clojure-lsp" "clean-ns"]       ;; Fix namespaces not clean
            "format-fix"   ["clojure-lsp" "format"]         ;; Fix namespaces not formatted
            "lint-fix"     ["do" ["clean-ns-fix"] ["format-fix"]]} ;; Fix both

  :injections [(require 'hashp.core)]

  :repl-options {:init-ns ratatouille.components}

  :test-paths ["test/unit" "test/integration" "test/helpers"]

  :main ratatouille.components/start-system!)
