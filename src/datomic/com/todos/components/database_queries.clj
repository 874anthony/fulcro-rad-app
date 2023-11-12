(ns com.todos.components.database-queries
  (:require
    [com.fulcrologic.rad.database-adapters.datomic-options :as do]
    [datomic.client.api :as d]
    [taoensso.timbre :as log]
    [taoensso.encore :as enc]
    ))

(defn- env->db [env]
  (some-> env (get-in [do/databases :production]) (deref)))

(defn get-all-todos
  [env query-params]
  (if-let [db (env->db env)]
    (let [ids (d/q '[:find ?uuid
                     :where
                     [?dbid :todo/id ?uuid]] db)]
      (mapv (fn [[id]] {:todo/id id}) ids))
      (log/error "No database connection available")))
