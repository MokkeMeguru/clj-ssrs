(ns reagent-ssr.css
  (:require [garden.def :refer [defstyles]]))

(defstyles screen
  [:body {:color "black"}]
  [:.content
   [:.title {:font-size "1.5rem"}]])
