(ns com.todos.ui
  (:require
    #?@(:cljs [[com.fulcrologic.semantic-ui.modules.dropdown.ui-dropdown :refer [ui-dropdown]]
               [com.fulcrologic.semantic-ui.modules.dropdown.ui-dropdown-menu :refer [ui-dropdown-menu]]
               [com.fulcrologic.semantic-ui.modules.dropdown.ui-dropdown-item :refer [ui-dropdown-item]]])
    #?(:clj  [com.fulcrologic.fulcro.dom-server :as dom :refer [div label input]]
       :cljs [com.fulcrologic.fulcro.dom :as dom :refer [div label input]])
    [com.fulcrologic.rad.form :as form]
    [com.todos.ui.todo-forms :refer [TodoForm TodoList]]
    [com.fulcrologic.fulcro.routing.dynamic-routing :refer [defrouter]]
    [com.fulcrologic.fulcro.application :as app]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.fulcro.dom.html-entities :as ent]
    [com.fulcrologic.rad.routing :as rroute]
    ))

(defsc LandingPage [this props]
  {:query ['*]
   :ident (fn [] [:component/id ::LandingPage])
   :initial-state {}
   :route-segment ["landing-page"]}
  (comp/fragment
    (dom/h1 :.ui.header "Todos App")
    (dom/p :.ui.message "This is a simple todo app built with Fulcro RAD. by @874anthony")))

(defrouter MainRouter [this props]
  {:router-targets [LandingPage TodoForm TodoList]})

(def ui-main-router (comp/factory MainRouter))
(defsc Root [this {:keys [router]}]
  {
   :query [{:router (comp/get-query MainRouter)}]
   :initial-state {:router {}}}
  (div :.ui.container.segment {:style {:marginTop "2em"}}

     #?(:cljs
        (comp/fragment
         (ui-dropdown {:className "item" :text "Todo"}
            (ui-dropdown-menu {}
              (ui-dropdown-item {:onClick (fn [] (rroute/route-to! this TodoList {}))} "View all")
              (ui-dropdown-item {:onClick (fn [] (form/create! this TodoForm))} "New")))))

    (ui-main-router router)
   ))

(def ui-root (comp/factory Root))
