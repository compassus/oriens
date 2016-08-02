(ns leiningen.new.oriens
  (:require [leiningen.new.templates :refer [renderer name-to-path ->files]]
            [leiningen.core.main :as main]))

(def render (renderer "oriens"))

(defn oriens
  "FIXME: write documentation"
  [name]
  (let [data {:name name
              :sanitized (name-to-path name)}]
    (main/info "Generating fresh 'lein new' oriens project.")
    (->files data
      ["boot.properties" (render "boot.properties" data)]
      ["build.boot" (render "build.boot" data)]
      [".gitignore" (render "gitignore" data)]
      ["README.md" (render "README.md" data)]
      ["LICENSE" (render "LICENSE" data)]
      ["src/main/{{sanitized}}/core.cljs" (render "core.cljs" data)]
      ["src/main/{{sanitized}}/parser.cljs" (render "parser.cljs" data)]
      ["src/test/{{sanitized}}/tests.cljs" (render "tests.cljs" data)]
      ["resources/index.html" (render "index.html" data)]
      ["resources/js/dev.cljs.edn" (render "dev.cljs.edn" data)]
      ["resources/css/main.main.less" (render "main.main.less" data)])))
