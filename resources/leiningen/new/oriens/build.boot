(set-env!
 :source-paths    #{"src/main"}
 :resource-paths  #{"resources"}
 :dependencies '[[org.clojure/clojurescript   "1.9.89"]
                 [org.omcljs/om               "1.0.0-alpha41"]
                 [compassus                   "0.2.1"]
                 [bidi                        "2.0.9"]
                 [kibu/pushy                  "0.3.6"]

                 [com.cognitect/transit-clj   "0.8.288"        :scope "test"]
                 [com.cemerick/piggieback     "0.2.1"          :scope "test"]
                 [adzerk/boot-cljs            "1.7.228-1"      :scope "test"]
                 [adzerk/boot-cljs-repl       "0.3.3"          :scope "test"]
                 [adzerk/boot-reload          "0.4.12"         :scope "test"]
                 [crisptrutski/boot-cljs-test "0.2.2-SNAPSHOT" :scope "test"]
                 [deraen/boot-less            "0.5.0"          :scope "test"]
                 [org.clojure/tools.nrepl     "0.2.12"         :scope "test"]
                 [pandeiro/boot-http          "0.7.3"          :scope "test"]
                 [weasel                      "0.7.0"          :scope "test"]])

(require
 '[adzerk.boot-cljs            :refer [cljs]]
 '[adzerk.boot-cljs-repl       :refer [cljs-repl start-repl]]
 '[adzerk.boot-reload          :refer [reload]]
 '[crisptrutski.boot-cljs-test :refer [test-cljs]]
 '[deraen.boot-less            :refer [less]]
 '[pandeiro.boot-http          :refer [serve]])

(deftask dev []
  (comp
    (serve)
    (watch)
    (cljs-repl)
    (reload :on-jsload '{{name}}.core/init!)
    (speak)
    (less)
    (cljs :source-map true
          :compiler-options {:parallel-build true}
          :ids #{"js/dev"})
    (sift :move {#"dev.js" "main.js"})
    (target)))

(deftask release []
  (comp
    (less)
    (cljs :optimizations :advanced
      :ids #{"js/dev"}
      :compiler-options {:parallel-build true
                         :elide-asserts true
                         :closure-defines {"goog.DEBUG" false}})
    (sift :move {#"dev.js" "main.js"})
    (target)))

(deftask testing []
  (set-env! :source-paths #(conj % "src/test"))
  identity)

(ns-unmap 'boot.user 'test)

(deftask test
  [e exit?     bool  "Exit after running the tests."]
  (let [exit? (cond-> exit?
                (nil? exit?) not)]
    (comp
      (testing)
      (test-cljs
        :js-env :node
        :namespaces #{'{{name}}.tests}
        :cljs-opts {:parallel-build true}
        :exit? exit?))))

(deftask auto-test []
  (comp
    (watch)
    (speak)
    (test :exit? false)))
