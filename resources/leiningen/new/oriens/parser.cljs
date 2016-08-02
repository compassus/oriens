(ns {{name}}.parser
  (:require [om.next :as om]))

(defmulti read om/dispatch)

(defmethod read :default
  [{:keys [state query]} _ _]
  {:value (select-keys @state query)})
