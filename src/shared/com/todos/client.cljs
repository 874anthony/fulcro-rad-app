(ns com.todos.client
  (:require
    [com.fulcrologic.fulcro.algorithms.timbre-support :refer [console-appender prefix-output-fn]]
    [com.fulcrologic.fulcro.algorithms.tx-processing.synchronous-tx-processing :as sync]
    [com.fulcrologic.fulcro.algorithms.tx-processing.batched-processing :as btxn]
    [com.fulcrologic.fulcro.application :as app]
    [com.fulcrologic.fulcro.components :as comp]
    [com.fulcrologic.fulcro.mutations :as m]
    [com.fulcrologic.fulcro.routing.dynamic-routing :as dr]
    [com.fulcrologic.fulcro.react.version18 :refer [with-react18]]
    [com.fulcrologic.rad.application :as rad-app]
    [com.fulcrologic.rad.authorization :as auth]
    [com.fulcrologic.rad.rendering.semantic-ui.semantic-ui-controls :as sui]
    [com.fulcrologic.rad.report :as report]
    [com.fulcrologic.rad.routing :as routing]
    [com.fulcrologic.rad.routing.history :as history]
    [com.fulcrologic.rad.routing.html5-history :as hist5 :refer [new-html5-history]]
    [com.fulcrologic.rad.type-support.date-time :as datetime]
    [taoensso.timbre :as log]
    [taoensso.tufte :as tufte :refer [profile]]))

(defn setup-RAD
  [app]
  (rad-app/install-ui-controls! app sui/all-controls)
  (report/install-formatter! app :boolean :affirmation (fn [_ value] (if value "yes" "no"))))

(defonce app (-> (rad-app/fulcro-rad-app {})
                 (with-react18)
                 (btxn/with-batched-reads)))

