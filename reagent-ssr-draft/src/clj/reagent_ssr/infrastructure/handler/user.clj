(ns reagent-ssr.infrastructure.handler.user
  (:require
   [reagent-ssr.pkg.html-template.head :as head-template]
   [hiccup.core :refer [html]]
   [hiccup.page :refer [include-js html5]]
   [clj-http.client :as client]
   [cheshire.core :refer [parse-string generate-string]]
   [reagent-ssr.pkg.utils.string :as string]))

(defn get-github-user! [github-user]
  (client/get (format "https://api.github.com/users/%s" github-user)
              {:accept :json  :throw-exceptions false}))

(defn ->github-user [response]
  (parse-string (:body response) true))

;; (def github (->
;;              "MokkeMeguru"
;;              get-github-user!
;;              ->github-user))

(defn header [github-user user]
  (head-template/head
   (format "User: %s" github-user)
   (head-template/og-meta
    {:title (format "User: %s" github-user)
     :description ""
     :type "article"
     :url "http://localhost:3000"
     :image (:avatar_url user)
     :locale "ja_JP"}
    :twitter {:card "summary"
              :site (format "http://localhost:3000/users/%s" github-user)
              :creator "meguru.mokke@gmail.com"})
   :csss ["/css/screen.css"]
   :opts [[:link {:href "https://cdn.jsdelivr.net/npm/bulma@0.9.3/css/bulma-rtl.min.css" :rel "stylesheet" :type "text/css"}]]))

(defn exist-page [github-user response]
  (let [user  (->github-user response)
        [script-data script-mount] [[:p#data {:hidden true} (str (merge {:exist true} user))]
                                    [:p#mount {:hidden true}
                                     (string/rand-str (inc (rand-int 32)))]]]
    {:status 200
     :headers {"Content-Type" "text/html"}
     :body
     (html
      (html5
       (header github-user user)
       [:body
        [:div#app "loading"]
        script-data
        script-mount
        (include-js "/js/compiled/app.js")]))}))

(defn not-found-page [github-user]
  (let [[script-data script-mount] [[:script#data (str {:exit false})]
                                    [:script#mount (string/rand-str (inc (rand-int 32)))]]]
    {:status 200
     :headers {"Content-Type" "text/html"}
     :body
     (html
      (html5
       (head-template/head
        (format "User %s is not found" github-user)
        []
        :opts  [[:link {:href "https://cdn.jsdelivr.net/npm/bulma@0.9.3/css/bulma-rtl.min.css" :rel "stylesheet" :type "text/css"}]])
       [:body
        [:div#app (format "User %s is not found" github-user)]
        script-data
        script-mount
        (include-js "js/compiled/app.js")]))}))

(defn page [github-user]
  (let [response (get-github-user! github-user)]
    (if (= (:status response) 200)
      (exist-page github-user response)
      (not-found-page github-user))))
