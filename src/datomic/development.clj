(ns development
  (:require
    [clojure.tools.namespace.repl :as tools-ns :refer [set-refresh-dirs]]
    [com.todos.components.datomic :refer [datomic-connections]]
    [mount.core :as mount]
    ))

(set-refresh-dirs "src/datomic" "src/shared")

(defn start []
  (mount/start-with-args {:config "config/defaults.edn"})
  :ok)

(defn stop
  []
  (mount/stop))

(def go start)

(defn restart
  []
  (stop)
  (tools-ns/refresh :after 'development/start))

(def reset #'restart)