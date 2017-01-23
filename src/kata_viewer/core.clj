(ns kata-viewer.core
  (:gen-class)
  (:require [clj-jgit.porcelain :as g]
            [clj-jgit.querying :as gq]
            [clojure.java.io :as io])
  (:import (java.io ByteArrayOutputStream)))

(def kata-dir "/Users/geoff/prog/clj/kata/bowling")

(def repo (g/load-repo kata-dir))

(def log (g/git-log repo))

(defn get-diff-body [rev-commit-1 rev-commit-2]
  (let [baos (ByteArrayOutputStream.)
        f (doto (org.eclipse.jgit.diff.DiffFormatter. baos)
            (.setRepository (.getRepository repo)))]
    (.format f rev-commit-1 rev-commit-2)
    (str baos)))
