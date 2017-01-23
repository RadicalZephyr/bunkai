(ns kata-viewer.diff-test
  (:require [kata-viewer.diff :as sut]
            [clojure.test :as t]))

;; Line-based diffing and patching tools

(t/deftest test-diff
  (t/testing "Add and remove at the end"
    (t/testing "Basic line addition"
      (t/is (= {0  [:+ "Hi!"]}
               (sut/diff [] ["Hi!"])))

      (t/is (= {1  [:+ "Bob"]}
               (sut/diff ["Hi!"]
                         ["Hi!" "Bob"]))))

    (t/testing "Basic line removal"
      (t/is (= {0  [:- "Hi!"]}
               (sut/diff ["Hi!"] [])))

      (t/is (= {1  [:- "Bob"]}
               (sut/diff ["Hi!" "Bob"] ["Hi!"])))))

  (t/testing "Add and remove at the beginning"
    (t/is (= {0  [:- "Hi!"]}
             (sut/diff ["Hi!" "Bob"]
                       ["Bob"] )))

    (t/is (= {0  [:+ "Hi!"]}
             (sut/diff ["Bob"]
                       ["Hi!" "Bob"] )))))

(t/deftest test-edit-graph
  (t/testing "Make basic same-length edit-graphs"
    (t/is (= #{[0,0]}
             (sut/edit-graph ["a"] ["a"])))

    (t/is (= #{[1,1]}
             (sut/edit-graph
              ["a" "b"]
              ["c" "b"])))

    (t/is (= #{[0,0] [1,1]}
             (sut/edit-graph
              ["a" "b"]
              ["a" "b"])))

    (t/is (= #{}
             (sut/edit-graph
              ["c"]
              ["d"]))))

  (t/testing "off 0-path diagonals"
    (t/is (= #{[1 0] [1 1]}
             (sut/edit-graph
              ["a" "b"]
              ["b" "b"])))

    (t/is (= #{[1 1]
               [0 2]
               [2 0]}
             (sut/edit-graph
              "abc"
              "cba")))

    (t/is (= #{[1 1]
               [2 0]
               [0 2]
               [3 2]
               [1 3]}
             (sut/edit-graph
              "abca"
              "cbab")))))
