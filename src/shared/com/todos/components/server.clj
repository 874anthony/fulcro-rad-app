(ns com.todos.components.server
  (:require
  [org.httpkit.server :refer [run-server]]
  [mount.core :as mount :refer [defstate]]
  [taoensso.timbre :as log]
  [com.todos.components.config :refer [config]]
  [com.todos.components.ring-middleware :refer [middleware]]
  ))

(defstate http-server
  :start
      (let [cfg      (get config :org.httpkit.server/config)
            stop-fn  (run-server middleware cfg)]

        (log/info "Starting webserver with config " cfg)
        {:stop stop-fn})
  :stop
      (let [{:keys [stop]} http-server]
        (when stop
          (stop))))
