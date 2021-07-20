(ns reagent-ssr.infrastructure.server
  (:require [integrant.core :as ig]
            [taoensso.timbre :as timbre]
            [ring.adapter.jetty :as jetty]))

(defmethod ig/init-key ::server [_ {:keys [port router]}]
  (timbre/info "server is running in port: " port)
  (timbre/info "router is: " router)
  (jetty/run-jetty router {:port 8080 :join? false}))

(defmethod ig/halt-key! ::server [_ server]
  (timbre/info "stop server")
  (.stop server))

;; (def api (run-jetty (reagent-ssr.infrastructure.router.core/app) {:port 3041 :join? false}))
;; (.stop api)
