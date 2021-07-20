(ns reagent-ssr.interface.gateway.initial-state.home
  #?(:cljs
     (:require [clojure.edn]
               [goog.crypt.base64 :as b64])

     :clj
     (:require [cheshire.core]))
  #?(:clj
     (:import java.util.Base64)))

(defn initial-state [state]
  #?(:clj
     (.encodeToString (Base64/getEncoder)
                      (.getBytes (cheshire.core/generate-string {:home state})))
     :cljs
     (let [mount (.-innerText (.getElementById js/document "mount"))]
       (if (empty? mount)
         {:home state}
         (let [state (.-innerText (.getElementById js/document "data"))]
           (js->clj (.parse js/JSON (b64/decodeString state)) :keywordize-keys true))))))
