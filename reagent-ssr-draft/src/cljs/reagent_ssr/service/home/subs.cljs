(ns reagent-ssr.service.home.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 ::home
 (fn [db]
   (:home db)))

(rf/reg-sub
 ::title
 :<- [::home]
 (fn [home _]
   (:title home)))
