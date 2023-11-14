(ns com.todos.model.todo
  (:require
    #?@(:clj
         [[com.wsscode.pathom.connect :as pc :refer [defmutation]]
          [com.todos.components.database-queries :as queries]]
        :cljs
        [[com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]])
    [com.fulcrologic.rad.attributes-options :as ao]
    [com.wsscode.pathom.connect :as pc]
    [com.fulcrologic.rad.form :as form]
    [com.fulcrologic.rad.form-options :as fo]
    [com.fulcrologic.rad.attributes :as attr :refer [defattr]]))

(defattr id :todo/id :uuid
     {ao/identity?   true
      ao/schema      :production})

(defattr title :todo/title :string
     {ao/identities     #{:todo/id}
      ao/required?      true
      ao/schema         :production})

(defattr completed? :todo/completed? :boolean
     {ao/identities     #{:todo/id}
      ao/schema         :production
      fo/default-value  false})

(defattr all-todos :todo/all-todos :ref
         {ao/target :todo/id
      ao/pc-output  [{:todo/all-todos [:todo/id]}]
      ao/pc-resolve (fn [{:keys [query-params] :as env} _]
                      #?(:clj
                          {:todo/all-todos (queries/get-all-todos env query-params)}))})

#?(:clj
   (defmutation set-todo-completed [env {:todo/keys [id completed?]}]
     {::pc/params #{:todo/id}
      ::pc/output [:todo/id]}
      (form/save-form* env {::form/id          id
                            ::form/master-pk   :todo/id
                            ::form/delta       {[:todo/id id] {:todo/completed? {:before (not completed?) :after (boolean completed?)}}}}))
   :cljs
    (defmutation set-todo-completed [{:todo/keys [id completed?]}]
      (action [{:keys [state]}]
        (swap! state assoc-in [:todo/id id :todo/completed?] completed?))
      (remote [_] true)))
(def attributes [id title completed? all-todos])

#?(:clj
   (def resolvers [set-todo-completed]))