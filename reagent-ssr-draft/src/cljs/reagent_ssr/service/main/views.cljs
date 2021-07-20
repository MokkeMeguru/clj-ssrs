(ns reagent-ssr.service.main.views
  (:require [re-frame.core :as rf]
            [reagent-ssr.service.main.subs :as subs]))

(defn main-panel []
  (let [current-route (rf/subscribe [::subs/current-route])]
    (when @current-route (-> @current-route :data :view))))
