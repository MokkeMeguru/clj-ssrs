(ns reagent-ssr.infrastructure.handler.about
  (:require
   [reagent-ssr.pkg.html-template.head :as head-template]
   [hiccup.core :refer [html]]
   [hiccup.page :refer [include-js html5]]
   [reagent-ssr.infrastructure.webpage.about :as about-page]
   [reagent-ssr.pkg.utils.string :as string]))

(defn header []
  (head-template/head
   "About"
   (head-template/og-meta
    {:title "About"
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
  (let [[script-data script-mount]  [[:p#data {:hidden true}]
                                     [:p#mount {:hidden true}
                                      (string/rand-str (inc (rand-int 32)))]]]
    {:status 200
     :headers {"Content-Type" "text/html"}
     :body
     (html
      (html5
       (header)
       [:body
        [:div#app
         (about-page/page-body)]
        script-data
        script-mount
        (include-js "/js/compiled/app.js")]))}))
