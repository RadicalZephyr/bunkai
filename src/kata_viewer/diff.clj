(ns kata-viewer.diff)

(defn- compare-lines [f]
  (fn [[i [_ aline bline]]]
    [[f i] [:+ bline]]))

(defn- make-transducer [f]
  (comp
   (map (juxt #(apply = %) first second))
   (map-indexed vector)
   (remove (comp first second))
   (map (compare-lines f))))

(defn diff [f a b]
  (let [t (make-transducer f)]
    (into {} t (map vector (concat a [""]) b))))
