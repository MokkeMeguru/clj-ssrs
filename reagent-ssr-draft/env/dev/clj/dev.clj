(ns dev
  (:require
   [environ.core :refer [env]]
   [integrant.repl :as igr]
   [orchestra.spec.test :as st]
   [clojure.java.io :as io]
   [integrant.core :as ig]))

(def config-file
  (if-let [config-file (env :config-file)]
    config-file
    "config.edn"))

(defn load-config
  [config-file]
  (-> config-file
      io/resource
      slurp
      ig/read-string
      (doto
       ig/load-namespaces)))

(defn start
  ([]
   (start config-file))
  ([config-file]
   (igr/set-prep! (constantly (load-config config-file)))
   (igr/prep)
   (igr/init)))

(defn stop []
  (igr/halt))

(defn restart []
  (st/instrument)
  (igr/reset-all))
