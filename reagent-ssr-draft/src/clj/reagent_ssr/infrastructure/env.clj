(ns reagent-ssr.infrastructure.env
  (:require [environ.core :refer [env]]
            [integrant.core :as ig]
            [orchestra.spec.test :as st]))

(defmethod ig/init-key ::env [_ _]
  (println "load env")
  (let [running (or (:env env) "dev")]
    (when (.contains ["test" "dev"] running)
      (st/instrument))
    {}))
