(defproject reagent-ssr "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "MIT"
            :url "https://choosealicense.com/licenses/mit"
            :comment "MIT License"
            :year 2021
            :key "mit"}

  :dependencies [[org.clojure/clojure "1.10.3"]
                 [org.clojure/clojurescript "1.10.866"]

                 ;; server side
                 ;; - integrant
                 [integrant "0.8.0"]
                 [integrant/repl "0.3.2"]

                 ;; - reitit
                 [metosin/reitit "0.5.13"]
                 [metosin/ring-http-response "0.9.2"]
                 [metosin/reitit-frontend "0.5.13"]

                 ;; - ring
                 [ring/ring-jetty-adapter "1.9.4"]
                 [ring/ring-core "1.9.4"]
                 [ring/ring-defaults "0.3.2"]
                 [ring-webjars "0.2.0"]
                 [ring-cors "0.1.13"]
                 [ring-logger "1.0.1"]
                 [com.fasterxml.jackson.core/jackson-core "2.12.2"]

                 ;; http client
                 [clj-http "3.12.3"]

                 ;; - css
                 [garden "1.3.10"]

                 ;; - environ
                 [environ "1.2.0"]

                 ;; client side
                 ;; - shadow
                 [thheller/shadow-cljs "2.14.3"]

                 ;; - reagent
                 [reagent "1.0.0"]

                 ;; - re-frame
                 [re-frame "1.2.0"]
                 [day8.re-frame/tracing "0.6.0"]

                 ;; common
                 ;; - testing
                 [orchestra "2021.01.01-1"]
                 [org.clojure/test.check "1.1.0"]

                 ;; - logging
                 [com.taoensso/timbre "5.1.2"]]

  :plugins [;; environment variable
            [lein-environ "1.1.0"]
            ;; client-side
            [lein-shadow "0.3.1"]
            ;; css
            [lein-garden "0.3.0"]
            ;; license
            [lein-license "1.0.0"]
            ;; others
            [lein-pprint "1.3.2"]
            [lein-ancient "0.6.15"]
            [lein-cloverage "1.2.2"]
            [lein-shell "0.5.0"]]

  :main ^:skip-aot reagent-ssr.core

  :source-paths ["src/clj" "src/cljs" "src/cljc"]
  :test-paths ["test/clj" "test/cljs" "test/cljc"]

  :resource-paths ["resources" "target/cljsbuild"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"
                                    "test/js"
                                    "resources/public/css"]

  :garden {:builds [{:id           "screen"
                     :source-paths ["src/clj"]
                     :stylesheet   reagent-ssr.css/screen
                     :compiler     {:output-to     "resources/public/css/screen.css"
                                    :pretty-print? true}}]}

  :shadow-cljs {:nrepl {:port 8777}
                :builds {:app {:target :browser
                               :output-dir "resources/public/js/compiled"
                               :asset-path "/js/compiled"
                               :modules {:app {:init-fn reagent-ssr.core/init
                                               :preloads [devtools.preload
                                                          day8.re-frame-10x.preload]}}
                               :devtools {:http-root "public"
                                          :http-port 8080}}
                         :dev {:compiler-options {:closure-defines {re-frame.trace.trace-enabled? true
                                                                    day8.re-frame.tracing.trace-enabled? true}}}

                         :browser-test
                         {:target :browser-test
                          :ns-regexp "-test$"
                          :runner-ns shadow.test.browser
                          :test-dir "target/browser-test"
                          :devtools {:http-root "target/browser-test"
                                     :http-port 8290}}

                         :karma-test
                         {:target :karma
                          :ns-regexp "-test$"
                          :output-to "target/karma-test.js"}}}

  :repl-options
  {:host "0.0.0.0"
   :port 39998}

  :profiles
  {:uberjar {:aot :all
             :prep-tasks [["garden" "once"]
                          ["server:ci"]
                          ["client:ci"]
                          ["client:release"]]

             :jvm-opts ["-Dclojure.compiler.direct-linking=true"]
             :source-paths ["env/prod/clj" "env/prod/cljs"]
             :resource-paths ["env/prod/resources"]}

   :dev [:project/dev :profiles/dev]
   :test [:project/test :profiles/test]
   :prod {}

   :project/dev {:jvm-opts ["-Dconf=dev-config.edn"]
                 :dependencies [[binaryage/devtools "1.0.3"]
                                [cider/piggieback "0.5.2"]
                                [pjstadig/humane-test-output "0.11.0"]
                                [prone "2021-04-23"]
                                [ring/ring-devel "1.9.3"]
                                [ring/ring-mock "0.4.0"]]
                 :plugins [[com.jakemccrary/lein-test-refresh "0.24.1"]
                           [jonase/eastwood "0.3.5"]
                           ;; emacs dev tool
                           [refactor-nrepl "2.5.1"]
                           [cider/cider-nrepl "0.25.9"]]

                 :source-paths ["env/dev/clj" "env/dev/cljs"]
                 :resource-paths ["env/dev/resources"]

                 :injections [(require 'pjstadig.humane-test-output)
                              (pjstadig.humane-test-output/activate!)]}

   :project/test {:jvm-opts ["-Dconf=test-config.edn"]
                  :resource-paths ["env/test/resources"]}
   :profiles/dev {}
   :profiles/test {}

   :repl {:prep-tasks ^:replace ["javac" "compile"]
          :repl-options {:init-ns user}}}

  :target-path "target/%s"

  :shell {:commands {"karma" {:windows         ["cmd" "/c" "karma"]
                              :default-command "karma"}
                     "open"  {:windows         ["cmd" "/c" "start"]
                              :macosx          "open"
                              :linux           "xdg-open"}}}

  :aliases
  {"coverage" ["cloverage"
               "--ns-exclude-regex" "^(:?dev|user)$"
               "--codecov"
               "--summary"]
   "client:watch"        ["with-profile" "dev" "do"
                          ["shadow" "watch" "app" "browser-test" "karma-test"]]
   "client:release"      ["with-profile" "prod" "do"
                          ["shadow" "release" "app"]]
   "client:build-report" ["with-profile" "prod" "do"
                          ["shadow" "run" "shadow.cljs.build-report" "app" "target/build-report.html"]
                          ["shell" "open" "target/build-report.html"]]
   "client:ci"           ["with-profile" "prod" "do"
                          ["shadow" "compile" "karma-test"]
                          ["shell" "karma" "start" "--single-run" "--reporters" "junit,dots"]]
   "server:ci" ["with-profile" "prod" "do"
                ["test"]]})
