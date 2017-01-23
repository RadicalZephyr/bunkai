(ns kata-viewer.diff-test
  (:require [kata-viewer.diff :as sut]
            [clojure.test :as t]))

;; Line-based diffing and patching tools

(t/deftest test-diff
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
             (sut/diff ["Hi!" "Bob"] ["Hi!"]))))

  (t/testing "Is it removal or addition, or both"
    (t/is (= {0  [:- "Hi!"]}
             (sut/diff ["Hi!" "Bob"]
                       ["Bob"] )))

    (t/is (= {0  [:+ "Hi!"]}
             (sut/diff ["Bob"]
                       ["Hi!" "Bob"] )))))
