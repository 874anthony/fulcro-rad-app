(ns com.todos.ui.todo-forms
  (:require
    #?(:clj  [com.fulcrologic.fulcro.dom-server :as dom :refer [div label input]]
       :cljs [com.fulcrologic.fulcro.dom :as dom :refer [div label input]])
    [clojure.string :as str]
    [com.todos.model.todo :as todo]
    [com.fulcrologic.rad.form :as form]
    [com.fulcrologic.rad.form-options :as fo]
    [com.fulcrologic.rad.report :as report]
    [com.fulcrologic.rad.report-options :as ro]
  ))

(form/defsc-form TodoForm [this props]
  {fo/id       todo/id
   fo/attributes [todo/title]
   fo/route-prefix "/todo"
   fo/title   "Create a Todo Item"}
)

(report/defsc-report TodoList [this props]
  {ro/title              "All Todos"
   ro/form-links         {todo/title TodoForm}
   ro/row-pk             todo/id
   ro/columns            [todo/title]
   ro/source-attribute   :todo/all-todos
   ro/run-on-mount?      true
   ro/row-visible?       (fn [{::keys [filter-title]} {:todo/keys [title]}]
                            (let [nm     (some-> title (str/lower-case))
                                  target (some-> filter-title (str/trim) (str/lower-case))]
                              (or
                                (nil? target)
                                (empty? target)
                                (and nm (str/includes? nm target)))))
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

   ro/row-actions        [{:label    "Edit"}
                          {:label    "Delete"}]
   ro/route              "/todos"
   }
)

