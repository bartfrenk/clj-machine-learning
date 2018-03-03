(ns ml.graphical.max-sum)


(let [cache (atom {})]
  (defn- joint-p
    [{:keys [states p-emit p-trans p-init] :as model} t current]
    (letfn [(compute [t current]
              (if (= t 0)
                (* (p-emit current (nth ys 0))
                   (p-init current))
                (->> states
                     (map (fn [previous]
                            (* (p-emit current (nth ys t))
                               (p-trans previous current)
                               (joint-p model (dec t) previous))))
                     (apply max))))]
      (when-not (get @cache [t current])
        (swap! cache #(assoc % [t current] (compute t current))))
      (get @cache [t current]))))

(defn viterbi
  [{:keys [states] :as model} ys]
  (let [t-max (dec (count ys))]
    (->> states
         (map (partial joint-p model t-max))
         (apply max))))


(def wikipedia-model
  {:states ["H" "F"]
   :p-emit (fn [x y]
             (case [x y]
               ["H" "D"] 0.1
               ["H" "C"] 0.4
               ["H" "N"] 0.5
               ["F" "D"] 0.6
               ["F" "C"] 0.3
               ["F" "N"] 0.1))
   :p-trans (fn [x1 x2]
              (case [x1 x2]
                ["H" "H"] 0.7
                ["H" "F"] 0.3
                ["F" "H"] 0.4
                ["F" "F"] 0.6))
   :p-init (fn p-init [x]
             (case x
               "H" 0.6
               "F" 0.4))})


(def ys (apply concat (repeat 10 ["N" "C" "D"])))

(viterbi wikipedia-model ys)
