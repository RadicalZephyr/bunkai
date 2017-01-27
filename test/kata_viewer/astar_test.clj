(ns kata-viewer.astar-test
  (:require [kata-viewer.astar :as sut]
            [clojure.test :as t]))

(t/deftest test-neighbors
  (t/is (= [[0 1] [1 0]]
           (sut/neighbors #{} [0 0])))

  (t/is (= [[1 1] [2 0]]
           (sut/neighbors #{} [1 0])))

  (t/is (= [[0 1] [1 0] [1 1]]
           (sut/neighbors #{[0 0]} [0 0]))))
