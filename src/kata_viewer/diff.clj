(ns kata-viewer.diff)

(defn- compare-lines [[i [_ aline bline]]]
  [i [:+ bline]])

(def t
  (comp
   (map (juxt #(apply = %) first second))
   (map-indexed vector)
   (remove (comp first second))
   (map compare-lines)))

(defn diff [f a b]
  (into {} t (map vector (concat a [""]) b)))
