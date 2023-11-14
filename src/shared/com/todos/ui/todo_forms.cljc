(ns com.todos.ui.todo-forms
  (:require
    #?(:clj [com.fulcrologic.fulcro.dom-server :as dom :refer [div label input]]
       :cljs [com.fulcrologic.fulcro.dom :as dom :refer [div label input]])
    [clojure.string :as str]
    [com.todos.model.todo :as todo]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.rad.form :as form]
    [com.fulcrologic.rad.form-options :as fo]
    [com.fulcrologic.rad.report :as report]
    [com.fulcrologic.rad.report-options :as ro]
    ))

(form/defsc-form TodoForm [this props]
  {fo/id              todo/id
   fo/attributes      [todo/title todo/completed?]
   fo/fields-visible? {:todo/completed? false}
   fo/default-values  {:todo/completed? false}
   fo/route-prefix    "todo"
   fo/title           "Create a Todo Item"
   fo/cancel-route    ["todos"]
   }
)

(report/defsc-report TodoList [this props]
  {ro/title              "All Todos"
   ro/row-pk             todo/id
   ro/columns            [todo/title todo/completed?]
   ro/column-formatters  {:todo/completed? (fn [this completed?] (if completed? "Yes" "No"))}
   ro/source-attribute   :todo/all-todos
   ro/run-on-mount?      true
   ro/row-visible?       (fn [{::keys [filter-title]} {:todo/keys [title]}]
                            (let [title-lowercase     (some-> title (str/lower-case))
                                  filter-lowercase (some-> filter-title (str/trim) (str/lower-case))]
                              (or
                                (empty? filter-lowercase)
                                (and title-lowercase (str/includes? title-lowercase filter-lowercase)))))
   ro/controls           {::new-todo      {:type   :button
                                           :local?  true
                                           :label   "New Todo"
                                           :action  (fn [this _] (form/create! this TodoForm))}
                          ::filter-title  {:type          :string
                                           :local?        true
                                           :placeholder  "Filter by title"
                                           :onChange     (fn [this _] (report/filter-rows! this))}
                          }
   ro/control-layout     {:action-buttons [::new-todo]
                          :inputs         [[::filter-title]]}

   ro/form-links         {todo/title TodoForm}
   ro/row-actions        [{:label    "Mark as completed"
                            :action  (fn [report-instance {:todo/keys [id]}]
                                      #?(:cljs
                                         (comp/transact! report-instance [(todo/set-todo-completed {:todo/id id
                                                                                                    :todo/completed? true})])))
                           :disabled? (fn [_ row-props] (:todo/completed? row-props))}
                          {:label "Delete"
                           :action (fn [report-instance {:todo/keys [id]}] (form/delete! report-instance :todo/id id))
                           }]
   ro/route              "todos"
   })