(ns reagent-ssr.interface.gateway.initial-state.core
  #?(:cljs
     (:require [clojure.edn])))

(defn initial-state [state]
  #?(:clj (str state))
  #?(:cljs
     (let [mount (.-innerText (.getElementById js/document "mount"))]
       (if (empty? mount)
         state
         (let [state (.-innerText (.getElementById js/document "data"))]
           (clojure.edn/read-string state))))))
