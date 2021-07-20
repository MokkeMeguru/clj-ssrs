(ns reagent-ssr.infrastructure.logger
  (:require [integrant.core :as ig]
            [taoensso.timbre :as timbre]
            [taoensso.timbre.tools.logging :as tlogging]))

(defmethod ig/init-key ::logger [_ _]
  (println "set logger with log-level" :info)
  (timbre/set-level! :info)
  (tlogging/use-timbre)
  {})
