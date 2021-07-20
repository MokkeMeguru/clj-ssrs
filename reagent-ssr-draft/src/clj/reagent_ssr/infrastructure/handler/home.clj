(ns reagent-ssr.infrastructure.handler.home
  (:require
   [reagent-ssr.pkg.html-template.head :as head-template]
   [reagent-ssr.infrastructure.webpage.home :as home-page]
   [reagent-ssr.interface.gateway.initial-state.home :as home-state]
   [reagent-ssr.pkg.utils.string :as string]
   [hiccup.core :refer [html]]
   [hiccup.page :refer [include-js html5]]))

;; server only rendering
(defn header []
  (head-template/head
   "Home"
   (head-template/og-meta
    {:title "Home"
     :description ""
     :type "website"
     :url "http://localhost:3000"
     :image "https://avatars.githubusercontent.com/u/30849444?v=4"
     :locale "ja_JP"}
    :twitter {:card "summary"
              :site "http://localhost:3000"
              :creator "meguru.mokke@gmail.com"})
   :csss ["/css/screen.css"]
   :opts [[:link {:href "https://cdn.jsdelivr.net/npm/bulma@0.9.3/css/bulma-rtl.min.css" :rel "stylesheet" :type "text/css"}]]))

(defn page []
  (let [title  "Hello from CLJC System (SSR Mode)"
        [script-data script-mount]  [[:p#data {:hidden true} (home-state/initial-state {:title title})]
                                     [:p#mount {:hidden true} (string/rand-str (inc (rand-int 32)))]]]
    {:status 200
     :headers {"Content-Type" "text/html"}
     :body
     (html
      (html5
       (header)
       [:body
        [:div#app
         (home-page/page-body "Hello from CLJC System (SSR Mode)")]
        script-data
        script-mount
        (include-js "/js/compiled/app.js")]))}))
