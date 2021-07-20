(ns reagent-ssr.core
  (:gen-class)
  (:require
   [integrant.core :as ig]
   [environ.core :refer [env]]
   [clojure.java.io :as io]))

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

(defn -main
  [& _]
  (-> config-file
      load-config
      ig/init))
