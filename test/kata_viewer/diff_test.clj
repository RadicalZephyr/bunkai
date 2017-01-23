(ns kata-viewer.diff-test
  (:require [kata-viewer.diff :as sut]
            [clojure.test :as t]))

;; Line-based diffing and patching tools

(t/deftest test-diff
  (t/is (= {0  [:+ "Hi!"]}
           (sut/diff "foo.clj" [] ["Hi!"])))

  (t/is (= {1  [:+ "Bob"]}
           (sut/diff "foo.clj"
                     ["Hi!"]
                     ["Hi!" "Bob"])))

  (t/is (= {0  [:- "Hi!"]}
           (sut/diff "foo.clj" ["Hi!"] []))))
