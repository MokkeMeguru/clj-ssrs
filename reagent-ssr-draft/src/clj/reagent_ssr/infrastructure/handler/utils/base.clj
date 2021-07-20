(ns reagent-ssr.infrastructure.handler.utils.base
  (:require [clojure.spec.alpha :as s]
            [clojure.string]))

;; before React 15
;;
;; (s/fdef react-id->str
;;   :args (s/cat :react-id vector?)
;;   :ret string?)

;; (s/fdef set-react-id
;;   :args (s/cat :react-id vector? :element vector?))

;; (defn- react-id->str [react-id]
;;   (str "." (clojure.string/join "." react-id)))

;; (defn- set-react-id [react-id element]
;;   (update element 1 merge {:data-reactid (react-id->str react-id)}))

;; (defn- normalize [component]
;;   (if (map? (second component))
;;     component
;;     (into [(first component) {}] (rest component))))

;; (defn render
;;   ([component] (render [0] component))
;;   ([id component]
;;    (println id component)
;;    (cond
;;      (fn? component) (render (component))
;;      (not (coll? component)) component
;;      (coll? (first component)) (map-indexed #(render (conj id %1) %2) component)
;;      (keyword? (first component)) (let [[tag opts & body] (normalize component)]
;;                                     (->> body
;;                                          (map-indexed #(render (conj id %1) %2))
;;                                          (into [tag opts])
;;                                          (set-react-id id)))
;;      (fn? (first component)) (render id (apply (first component) (rest component))))))

;; (render [0] [:html [:body [:div "hello"]]])
;; (render "hello")
