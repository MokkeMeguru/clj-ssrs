(ns reagent-ssr.service.home.views
  (:require [reagent-ssr.service.home.subs :as home-subs]
            [re-frame.core :as rf]))

(defn title []
  @(rf/subscribe [::home-subs/title]))
