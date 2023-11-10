(ns com.todos.components.parser
  (:require
    [com.todos.components.auto-resolvers :refer [automatic-resolvers]]
    [com.todos.components.config :refer [config]]
    [com.todos.components.datomic :refer [datomic-connections]]
    [com.todos.components.delete-middleware :as delete]
    [com.todos.components.save-middleware :as save]
    [com.todos.model :refer [all-attributes]]
    [com.fulcrologic.rad.attributes :as attr]
    [com.fulcrologic.rad.database-adapters.datomic-cloud :as datomic]
    [com.fulcrologic.rad.form :as form]
    [com.fulcrologic.rad.pathom :as pathom]
    [mount.core :refer [defstate]]
    [com.wsscode.pathom.core :as p]
    [com.fulcrologic.rad.type-support.date-time :as dt]
    [com.wsscode.pathom.connect :as pc]
    ))

(pc/defresolver index-explorer [{::pc/keys [indexes]} _]
                {::pc/input  #{:com.wsscode.pathom.viz.index-explorer/id}
                 ::pc/output [:com.wsscode.pathom.viz.index-explorer/index]}
                {:com.wsscode.pathom.viz.index-explorer/index
                 (p/transduce-maps
                   (remove (comp #{::pc/resolve ::pc/mutate} key))
                   indexes)})

(defstate parser
    :start
          (pathom/new-parser config
             [(attr/pathom-plugin all-attributes)
              (form/pathom-plugin save/middleware delete/middleware)
              (datomic/pathom-plugin (fn [env] {:production (:main datomic-connections)}))

              {::p/wrap-parser
               (fn transform-parser-out-plugin-external [parser]
                 (fn transform-parser-out-plugin-internal [env tx]
                   (dt/with-timezone "America/Los_Angeles"
                       (if (and (map? env) (seq tx))
                         (parser env tx)
                         {}))))}]
               [automatic-resolvers
                form/resolvers
                index-explorer]))