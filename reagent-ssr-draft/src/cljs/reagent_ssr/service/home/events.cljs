(ns reagent-ssr.service.home.events
  (:require [re-frame.core :as rf]))

(rf/reg-event-db
 ::title
 (fn [db [_ title]]
   (assoc db :home title)))
