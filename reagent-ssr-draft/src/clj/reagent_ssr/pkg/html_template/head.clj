(ns reagent-ssr.pkg.html-template.head
  (:require [hiccup.page :refer [include-css include-js]]
            [clojure.spec.alpha :as s]
            [clojure.string]))

;; domain
(def url-regex (re-pattern #"^(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"))

(s/def :meta-twitter/card (s/and string? #(some (partial = %) ["summary" "summary-large-image" "app" "player"])))
(s/def :meta-twitter/site string?)
(s/def :meta-twitter/creator string?)
(s/def ::meta-twitter (s/keys :opt-un [:meta-twitter/card :meta-twitter/site :meta-twitter/creator]))

(s/def :meta-facebook/app-id string?)
(s/def :meta-facebook/admins string?)
(s/def ::meta-facebook (s/keys :opt-un [:meta-facebook/app-id :meta-facebook/admins]))

(s/def :meta-default/title string?)
(s/def :meta-default/type string?)
(s/def :meta-default/url (s/and string? (partial re-matches url-regex)))
(s/def :meta-default/image (s/and string? (partial re-matches url-regex)))
(s/def :meta-default/locale string?)
(s/def :meta-default/description string?)
(s/def ::meta-default (s/keys :req-un [:meta-default/title :meta-default/type :meta-default/url :meta-default/image :meta-default/locale :meta-default/description]))

(s/def ::meta #(= :meta (first %)))
(s/def ::meta-vec (s/and vector? (s/coll-of ::meta)))

(s/def ::csss (s/coll-of string?))
(s/def ::jss (s/coll-of string?))
(s/def ::opts (s/coll-of vector?))

(s/fdef og-meta-twitter
  :args (s/cat :meta-twitter ::meta-twitter)
  :ret ::meta-vec)

(s/fdef og-meta-facebook
  :args (s/cat :meta-facebook ::meta-facebook)
  :ret ::meta-vec)

(s/fdef og-meta
  :args (s/cat :meta-default  ::meta-default
               :kwargs (s/keys* :opt-un [::meta-twitter ::meta-facebook]))
  :ret ::meta-vec)

(s/fdef head
  :args (s/cat :title string?
               :mata-vec (s/or :empty empty?
                               :exist ::meta-vec)
               :kwargs (s/keys* :opt-un [::csss ::jss ::opts])))

;; impl


(defn- og-meta-twitter
  [{:keys [card site creator]}]
  (cond-> []
    card (conj [:meta {:property "twitter:card" :content card}])
    site  (conj [:meta {:property "twitter:site" :content site}])
    creator (conj [:meta {:property "twitter:creator" :content creator}])))

(defn- og-meta-facebook
  [{:keys [app-id admins]}]
  (cond-> []
    app-id (conj [:meta {:property "fb:app_id" :content app-id}])
    admins (conj [:meta {:property "fb:admins" :content admins}])))

(defn og-meta
  [default & {:keys [facebook twitter]}]
  (vec
   (cond->
    [[:meta {:property "og:title" :content  (:title default)}]
     [:meta {:property "og:type" :content (:type default)}]
     [:meta {:property "og:url" :content  (:url default)}]
     [:meta {:property "og:image" :content (:image default)}]
     [:meta {:property "og:locale" :content (:locale default)}]
     [:meta {:property "og:description" :content (:description default)}]]
     (map? twitter)
     (concat (og-meta-twitter twitter))
     (map? facebook)
     (concat (og-meta-facebook facebook)))))

(defn head [title meta-vec & {:keys [csss jss opts]}]
  (vec
   (concat
    [:head {:prefix "og: https://ogp.me/ns#"}]
    (cond->
     (concat [[:meta {:charset "utf-8"}]
              [:meta {:name "viewport"
                      :content "width=device-width, initial-scale=1"}]
              [:title title]]
             meta-vec)
      (vector? csss) (concat (apply include-css csss))
      (vector? jss) (concat  (apply include-js jss))
      (vector? opts) (concat opts)))))

;; (apply og-meta [{:title "hello" :type "article" :url "http://hoge" :image "http://hoge" :locale "ja_JP"}
;;                 :twitter {:card "summary"}])

;; (head
;;  "this is title"
;;  (og-meta {:title "hello" :type "article" :url "http://hoge" :image "http://hoge" :locale "ja_JP"}
;;           :twitter {:card "summary"}
;;           :facebook {:app-id "id"}))
