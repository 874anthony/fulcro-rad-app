(ns com.todos.components.save-middleware
  (:require
    [com.fulcrologic.rad.middleware.save-middleware :as r.s.middleware]
    [com.fulcrologic.rad.database-adapters.datomic-cloud :as datomic]))

(defn wrap-exceptions-as-form-errors
  ([handler]
   (fn [pathom-env]
     (try
       (let [handler-result (handler pathom-env)]
         handler-result)
       (catch Throwable t
         {:com.fulcrologic.rad.form/errors [{:message (str "Unexpected error saving form: " (ex-message t))}]})))))

(def middleware
  (->
    (datomic/wrap-datomic-save)
    (wrap-exceptions-as-form-errors)
    (r.s.middleware/wrap-rewrite-values)))