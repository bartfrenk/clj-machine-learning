(ns ml.graphical.max-sum)


(defn map-key
  [f coll]
  (zipmap coll (map f coll)))


(defn viterbi
  [{:keys [states p-emit p-trans p-init] :as model} ys]
  (let [cache (atom {})]
    (letfn [(backtrack [trace [t x] acc]
              (if (neg? t)
                acc
                (recur trace [(dec t) (first (get trace [t x]))] (cons x acc))))

            (joint-p [t current]
              (when-not (get @cache [t current])
                (swap!
                  cache
                  #(assoc % [t current]
                          (if (= t 0)
                            [:start (* (p-emit current (nth ys 0))
                                       (p-init current))]
                            (->> states
                                 (map-key
                                   (fn [previous]
                                     (* (p-emit current (nth ys t))
                                        (p-trans previous current)
                                        (joint-p (dec t) previous))))
                                 (apply max-key val))))))
              (second (get @cache [t current])))]

      (let [t-max (dec (count ys))
            r (->> states
                   (map-key (partial joint-p t-max))
                   (apply max-key val))]
        [(backtrack @cache [t-max (first r)] []) (second r)]))))
