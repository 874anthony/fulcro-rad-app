(ns com.todos.components.auto-resolvers
  (:require
    [com.todos.model :refer [all-attributes]]
    [mount.core :refer [defstate]]
    [com.fulcrologic.rad.resolvers :as res]
    [com.fulcrologic.rad.database-adapters.datomic-cloud :as datomic]))

(defstate automatic-resolvers
    :start
          (vec
            (concat
              (res/generate-resolvers all-attributes)
              (datomic/generate-resolvers all-attributes :production))))
