(ns com.todos.model
  (:require
    [com.todos.model.todo :as todo]
    [com.fulcrologic.rad.attributes :as attr]))

(def all-attributes (vec
                      (concat
                        todo/attributes)))

