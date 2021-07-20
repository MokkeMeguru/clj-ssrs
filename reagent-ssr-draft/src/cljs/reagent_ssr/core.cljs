(ns reagent-ssr.core
  (:require [reagent-ssr.config :as config]
            [reagent.dom :as rdom]
            [react-dom]
            [reagent.core :as r]
            [reagent-ssr.infrastructure.webpage.home :as home-page]
            [reagent-ssr.service.main.views :as main-views]
            [reagent-ssr.routers :as routers]
            [re-frame.core :as rf]
            [reagent-ssr.service.main.events :as main-events]))

(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (rf/clear-subscription-cache!)
  (routers/init-routes!)
  (let [root-el (.getElementById js/document "app")]
    (react-dom/hydrate (r/as-element [main-views/main-panel]) root-el)))

(defn init []
  (rf/dispatch-sync [::main-events/initialize-db])
  (dev-setup)
  (mount-root))
