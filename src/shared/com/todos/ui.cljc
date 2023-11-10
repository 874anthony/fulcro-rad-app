(ns com.todos.ui
  (:require
  #?@(:cljs [[com.fulcrologic.semantic-ui.modules.dropdown.ui-dropdown :refer [ui-dropdown]]
             [com.fulcrologic.semantic-ui.modules.dropdown.ui-dropdown-menu :refer [ui-dropdown-menu]]
             [com.fulcrologic.semantic-ui.modules.dropdown.ui-dropdown-item :refer [ui-dropdown-item]]])
  #?(:clj  [com.fulcrologic.fulcro.dom-server :as dom :refer [div label input]]
     :cljs [com.fulcrologic.fulcro.dom :as dom :refer [div label input]])
    [com.fulcrologic.fulcro.application :as app]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.fulcro.dom.html-entities :as ent]))

(defsc Root [this props]
  {}
  (dom/div
    (dom/div "TODOS App"))
 )

(def ui-root (comp/factory Root))
