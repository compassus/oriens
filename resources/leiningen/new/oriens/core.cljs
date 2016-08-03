(ns {{name}}.core
  (:require [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]
            [compassus.core :as c]
            [bidi.bidi :as bidi]
            [pushy.core :as pushy]
            [{{name}}.parser :as parser]))

(defui Home
  static om/IQuery
  (query [this]
    [:app/title])
  Object
  (render [this]
    (let [{:keys [app/title]} (om/props this)]
      (dom/div nil
        (dom/h3 nil title)
        (dom/p nil "It works!")))))

(def app-state
  (atom {:app/title "Oriens"}))

(def bidi-routes
  ["/" {""      :index
        "about" :about}])

(declare app)

(def history
  (pushy/pushy #(c/set-route! app (:handler %))
    (partial bidi/match-route bidi-routes)))

(def app
  (c/application {:routes {:index (c/index-route Home)}
                  :reconciler-opts {:state app-state
                                    :parser (om/parser {:read parser/read})}
                  :history {:setup    #(pushy/start! history)
                            :teardown #(pushy/stop! history)}}))

(defn init! []
  (c/mount! app (js/document.getElementById "app")))
