(ns kata-viewer.diff
  (:refer-clojure :exclude [compare]))

(defn- compare [aline bline]
  (cond
    (= aline bline)        [:=]
    (= :missing aline) [:+ bline]
    (= :missing bline) [:- aline]
    :else :should-never-happen))

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
    (cond
      (and
       (> b-len a-len 0)
       (= (nth a 0) (nth b 1)))
      [(into [:missing] a) b]

      (and
       (> a-len b-len 0)
       (= (nth a 1) (nth b 0)))
      [a (into [:missing] b)]

      (> a-len b-len)
      [a (conj b :missing)]

      (< a-len b-len)
      [(conj a :missing) b])))

(defn diff [a b]
  (let [[a b] (diff-pad-collections a b)]
    (into {} t (map vector a b))))

(defn- compare-things [i [a b]]
  (when (= a b)
    [i,i]))

(defn edit-graph [a b]
  (->> (for [[x ia] (map-indexed vector a)
             [y ib] (map-indexed vector b)
             :when (= ia ib)]
         [x y])
       (into #{})))

(defn make-follow-snake [g m n]
  (fn [ox oy]
    (loop [[x y :as coord] [ox oy]]
      (if (and
           (> n x)
           (> m y)
           (g coord))
        (recur (mapv inc coord))
        coord))))

(defn greedy-ses [a b]
  (if (= a b)
    :diff/no-differences
    (let [g (edit-graph a b)
          n (count a)
          m (count b)
          max (+ m n)]
      (loop [ds (range 0 (inc max))
             vs []
             v {1 0}]
        (if (seq ds)
          (let [d (first ds)
                v (loop [ks (range (- d) (inc d) 2)
                         v v]
                    (if (seq ks)
                      (let [k (first ks)
                            k-1 (get v (dec k))
                            k+1 (get v (inc k))
                            x (if (or
                                   (= k (- d))
                                   (and
                                    (not= k d)
                                    (< k-1 k+1)))
                                k+1
                                (inc k-1))
                            y (- x k)
                            [x y] (loop [xy [x y]]
                                    (if (contains? g xy)
                                      (recur (mapv inc xy))
                                      xy))]
                        (if (and (>= x n) (>= y m))
                          (reduced v)
                          (recur (rest ks) (assoc v k x))))
                      v))]
            (if (reduced? v)
              (conj vs (unreduced v))
              (recur (rest ds) (conj vs v) v)))
          :diff/length-of-ses>max)))))
