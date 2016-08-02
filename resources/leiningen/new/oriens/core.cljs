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
    [:home/title :home/content])
  Object
  (render [this]
    (let [{:keys [home/title home/content]} (om/props this)]
      (dom/div nil
        (dom/h3 nil title)
        (dom/p nil (str content))))))

(defui About
  static om/IQuery
  (query [this]
    [:about/title :about/content])
  Object
  (render [this]
    (let [{:keys [about/title about/content]} (om/props this)]
      (dom/div nil
        (dom/h3 nil title)
        (dom/p nil (str content))))))

(defn wrapper [{:keys [owner factory props]}]
  (let [route (c/current-route owner)]
    (dom/div #js {:style #js {:margin "0 auto"
                              :height 250
                              :width 500
                              :backgroundColor "oldlace"}}
      (dom/div #js {:style #js {:minWidth "100%"
                                :minHeight "48px"
                                :lineHeight "48px"
                                :verticalAlign "middle"
                                :borderBottomWidth "2px"
                                :borderBottomStyle "solid"}}
        (dom/h2 #js {:style #js {:margin 0
                                 :textAlign "center"
                                 :lineHeight "48px"}}
          "Oriens"))
      (dom/div #js {:style #js {:display "inline-block"
                                :width "25%"
                                :minHeight "80%"
                                :verticalAlign "top"
                                :backgroundColor "gainsboro"}}
        (dom/ul nil
          (dom/li #js {:style #js {:marginTop "20px"}}
            (dom/a #js {:href "/"
                        :style (when (= route :index)
                                 #js {:color "black"
                                      :cursor "text"})}
              "Home"))
          (dom/li #js {:style #js {:marginTop "5px"}}
            (dom/a #js {:href "/about"
                        :style (when (= route :about)
                                 #js {:color "black"
                                      :cursor "text"})}
              "About")))
        (dom/p #js {:style #js {:textAlign "center"
                                :textDecoration "underline"
                                :marginBottom "5px"
                                :marginTop "30px"
                                :fontWeight "bold"}}
          "Current route:")
        (dom/p #js {:style #js {:textAlign "center"
                                :margin 0
                                :color "red"}}
          (str (pr-str route))))
      (dom/div #js {:style #js {:display "inline-block"
                                :width "70%"
                                :minHeight "70%"
                                :verticalAlign "top"
                                :padding "12.5px 12.5px 12.5px 10.5px"
                                :borderLeftWidth "2px"
                                :borderLeftStyle "solid"}}
        (factory props)))))

(defonce app-state
  {:home/title "Home page"
   :home/content "This is the homepage. There isn't a lot to see here."
   :about/title "About page"
   :about/content "This is the about page, the place where one might write things about their own self."})

(def bidi-routes
  ["/" {""      :index
        "about" :about}])

(declare app)

(def history
  (pushy/pushy #(c/set-route! app (:handler %))
    (partial bidi/match-route bidi-routes)))

(def app
  (c/application {:routes {:index (c/index-route Home)
                           :about About}
                  :wrapper wrapper
                  :reconciler-opts {:state (atom app-state)
                                    :parser (om/parser {:read parser/read})}
                  :history {:setup    #(pushy/start! history)
                            :teardown #(pushy/stop! history)}}))

(defn init! []
  (c/mount! app (js/document.getElementById "app")))
