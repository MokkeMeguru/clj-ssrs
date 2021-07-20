(ns reagent-ssr.infrastructure.webpage.home)

;; SSR
(defn page-body [title]
  [:div.container
   [:div.columns
    [:div.column
     [:p.title
      #?(:cljs {:on-click (fn [] (println "Hello"))})
      title]]]])
