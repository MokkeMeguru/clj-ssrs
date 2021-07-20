(ns reagent-ssr.infrastructure.router.core-test
  (:require [reagent-ssr.infrastructure.router.core :as sut]
            [clojure.test :as t]
            [reitit.core :as r]))

(defn match-path [router path]
  (instance? reitit.core.Match (r/match-by-path router path)))

(t/deftest app-test
  (t/testing "match all static route"
    (let [router (sut/app-router)]
      (t/is (match-path router "/"))
      (t/is (match-path router "/about"))))
  (t/testing "match dynamic route"
    (let [router (sut/app-router)]
      (t/is (match-path router "/user/MeguruMokke")))))
