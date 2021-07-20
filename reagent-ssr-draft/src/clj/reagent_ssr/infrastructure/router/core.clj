(ns reagent-ssr.infrastructure.router.core
  (:require
   [clojure.spec.alpha :as s]
   [reitit.ring :as ring]
   [reitit.coercion.spec]
   [reitit.ring.coercion :as rrc]
   [muuntaja.core :as m]
   [reitit.dev.pretty :as pretty]
   [reagent-ssr.domain.model.github :as github-model]
   [integrant.core :as ig]
   [ring.logger :refer [wrap-with-logger]]
   [taoensso.timbre :as timbre]
   [reagent-ssr.infrastructure.handler.home :as home-handler]
   [reagent-ssr.infrastructure.handler.about :as about-handler]
   [reagent-ssr.infrastructure.handler.user :as user-handler]
   [clojure.java.io :as io]))

(defn app-router []
  (ring/router
   [""
    ["/js/*" (ring/create-resource-handler {:root "public/js"})]
    ["/css/*" (ring/create-resource-handler {:root "public/css"})]

    ["/"  {:get {:handler (fn [_]
                            (home-handler/page))}}]
    ["/about" {:get {:handler (fn [_]
                                (about-handler/page))}}]
    ["/users/:github-user" {:get {:parameters {:path (s/keys :req-un [::github-model/github-user])}
                                  :handler (fn [{:keys [path-params]}]
                                             (user-handler/page (:github-user path-params)))}}]]
   {:exception pretty/exception
    :data {:coercion reitit.coercion.spec/coercion
           :muuntaja m/instance
           :middleware [rrc/coerce-request-middleware
                        rrc/coerce-response-middleware]}}))

(defn app []
  (ring/ring-handler
   (app-router)
   (ring/routes
    (ring/create-default-handler
     {:not-found (constantly {:status 404
                              :header {"Content-Type" "text/html"}
                              :body (slurp (io/resource "public/404.html"))})}))
   {:middleware [wrap-with-logger]}))

(defmethod ig/init-key ::router [_ _]
  (timbre/info "router got: ")
  (app))
