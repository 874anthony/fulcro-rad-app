(ns com.todos.model.todo
  (:require
    #?@(:clj
        [[com.todos.components.database-queries :as queries]])
    [com.fulcrologic.rad.attributes-options :as ao]
    [com.fulcrologic.rad.attributes :as attr :refer [defattr]]))

(defattr id :todo/id :uuid
     {ao/identity?   true
      ao/schema      :production})

(defattr title :todo/title :string
     {ao/identities   #{:todo/id}
      ao/required?   true
      ao/schema      :production})

(defattr all-todos :todo/all-todos :ref
     {ao/target     :todo/id
      ao/pc-output  [{:todo/all-todos [:todo/id]}]
      ao/pc-resolve (fn [{:keys [query-params] :as env} _]
                      #?(:clj
                          {:todo/all-todos (queries/get-all-todos env query-params)}))})

(def attributes [id title all-todos])
