(ns {{name}}.core
  (:require [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]
            [compassus.core :as c]
            [bidi.bidi :as bidi]
            [pushy.core :as pushy]
            [{{name}}.parser :as parser]))

(defui ^:once Home
  static om/IQuery
  (query [this]
    [:app/title])
  Object
  (render [this]
    (let [{:keys [app/title]} (om/props this)]
      (dom/div nil
        (dom/h3 nil title)
        (dom/p nil "It works!")))))

(defonce app-state
  (atom {:app/title "Oriens"}))

(defonce bidi-routes
  ["/" {""      :index
        "about" :about}])

(declare app)

(defonce history
  (pushy/pushy #(c/set-route! app (:handler %))
    (partial bidi/match-route bidi-routes)))

(defonce app
  (c/application {:routes {:index (c/index-route Home)}
                  :reconciler-opts {:state app-state
                                    :parser (om/parser {:read parser/read})}
                  :history {:setup    #(pushy/start! history)
                            :teardown #(pushy/stop! history)}}))

(defonce mounted? (atom false))

(defn init! []
  (enable-console-print!)
  (if-not @mounted?
    (do
      (c/mount! app (js/document.getElementById "app"))
      (swap! mounted? not))
    (let [route->component (-> app :config :route->component)
          c (om/class->any (c/get-reconciler app) (get route->component (c/current-route app)))]
      (.forceUpdate c))))
