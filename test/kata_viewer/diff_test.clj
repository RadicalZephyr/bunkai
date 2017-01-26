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
              "cbab"))))

  (t/testing "different length sequences"
    (t/is (= #{[0 2]}
             (sut/edit-graph
              "a"
              "cba")))))

(t/deftest test-greedy-ses

  (t/is (= :diff/no-differences
           (sut/greedy-ses "" "")))
  (t/is (= :diff/no-differences
           (sut/greedy-ses "a" "a")))
  (t/is (= :diff/no-differences
           (sut/greedy-ses "abcdef" "abcdef")))

  (t/is (= :diff/length-of-ses>max
           (sut/greedy-ses "a" "b")))

  (t/is (= {1 1, 0 0, -1 1, -2 1}
           (sut/greedy-ses "aa" "ba")))

  (t/is (= {1 2, 0 1, -1 1, -2 1}
           (sut/greedy-ses "aa" "ab")))

  (t/is (= {1 0, 0 2, -1 2}
           (sut/greedy-ses "aa" "aaa")))
  (t/is (= {1 0, 0 2}
           (sut/greedy-ses "aaa" "aa")))

  (t/is (= {1 3, 0 2, -1 2, -2 2, 2 3, -3 1, 3 4, -4 1}
           (sut/greedy-ses "abc" "cba")))

  (t/is (= {0 5,-4 4,7 8,1 6,-2 5,4 6,-1 5,-8 1,-6 2,-3 4,6 7,3 7,2 6,-7 1,5 7,-5 2}
           (sut/greedy-ses "abcdef" "cbafed")))

  (t/is (= {1 4, 0 6, -1 6, -2 5, 2 5, -3 5}
           (sut/greedy-ses "abcdef" "abc1def")))

  (t/is (= {0 8,-4 6,7 11,1 8,-2 7,4 9,-1 7,-8 4,-6 5,-3 6,6 10,3 9,2 9,-7 4,9 12,5 10,-9 3,-10 3,-5 5,8 11}
           (sut/greedy-ses "abcdef" "abc1234def")))

  (t/is (= {0 7,-4 5,7 11,1 8,-2 6,4 9,-1 7,-8 4,-6 5,-3 6,6 10,3 9,2 8,-7 4,9 12,5 10,-9 3,-10 3,-5 5,8 11}
           (sut/greedy-ses "abc1234def" "abcdef")))
  )
