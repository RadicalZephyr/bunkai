(ns kata-viewer.diff
  (:refer-clojure :exclude [compare]))

(defn- compare [aline bline]
  (cond
    (= aline bline)        [:=]
    (= :missing aline) [:+ bline]
    (= :missing bline) [:- aline]))

(defn- compare-lines [[i [_ aline bline]]]
  [i (compare aline bline)])

(def t
  (comp
   (map (juxt #(apply = %) first second))
   (map-indexed vector)
   (remove (comp first second))
   (map compare-lines)))

(defn- diff-pad-collections [a b]
  (let [a-len (count a)
        b-len (count b)]
    (if (> a-len b-len)
      [a (conj b :missing)]
      [(conj a :missing) b])))

(defn diff [f a b]
  (let [[a b] (diff-pad-collections a b)]
    (into {} t (map vector a b))))
