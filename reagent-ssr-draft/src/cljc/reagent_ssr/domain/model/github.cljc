(ns reagent-ssr.domain.model.github
  (:require [clojure.spec.alpha :as s]))

(def github-regex  #"(?i)^[a-z\d](?:[a-z\d]|-(?=[a-z\d])){0,38}$")

(s/def ::github-user (s/and string? #(= % (re-matches github-regex %))))
