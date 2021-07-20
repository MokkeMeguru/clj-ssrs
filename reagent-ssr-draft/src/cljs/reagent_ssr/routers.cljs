(ns reagent-ssr.routers
  (:require [reagent-ssr.infrastructure.webpage.home :as home-page]
            [reagent-ssr.infrastructure.webpage.about :as about-page]
            [reitit.frontend :as reitit-frontend]
            [reitit.frontend.easy :as reitit-frontend-easy]
            [reagent-ssr.service.main.events :as main-events]
            [reagent-ssr.service.home.events :as home-events]
            [reagent-ssr.service.home.subs :as home-subs]
            [reagent-ssr.interface.gateway.initial-state.home :as home-state]
            [reagent-ssr.service.home.views :as home-views]
            [re-frame.core :as rf]))

(def routes
  [""
   ["/"
    {:name :home
     :view (home-page/page-body [home-views/title])
     :link-text "app-home"}]
   ["/about"
    {:name :about
     :view about-page/page-body
     :link-text "app-about"}]
   ["/users/:github-user"
    {:name :user
     :parameters {:path {:github-user string?}}}]])

(def router (reitit-frontend/router routes))

(defn on-navigate [new-match]
  (when new-match
    (rf/dispatch-sync [::main-events/navigated new-match])))

(defn init-routes! []
  (reitit-frontend-easy/start!
   router
   on-navigate
   {:use-fragment false}))
