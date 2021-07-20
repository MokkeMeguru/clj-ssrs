(ns reagent-ssr.service.main.events
  (:require [re-frame.core :as rf]
            [reagent-ssr.service.main.db :as main-db]
            [reitit.frontend.easy :as reitit-frontend-easy]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [reitit.frontend.controllers :as reitit-frontend-controllers]
            [reagent-ssr.interface.gateway.initial-state.home :as home-state]
            [clojure.edn]))

;; navigation

(rf/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
            (println "hello"  (home-state/initial-state "initial state"))
            (merge
             main-db/default-db
             (home-state/initial-state "initial state"))))

(rf/reg-fx
 ::navigate!
 (fn [route]
   (apply reitit-frontend-easy/push-state route)))

(rf/reg-event-fx
 ::navigate
 (fn [_cofx [_ & route]]
   {::navigate! route}))

(rf/reg-event-db
 ::navigated
 (fn [db [_ new-match]]
   (let [old-match (:current-route db)
         controllers (reitit-frontend-controllers/apply-controllers (:controllers old-match) new-match)]
     (when-not (= new-match old-match) (.scrollTo js/window 0 0))
     (assoc db :current-route (assoc new-match :controllers controllers)))))
