(ns ml.graphical.max-sum
  (:require [ml.graphical.max-sum :as sut]
            [clojure.test :refer :all]
            [same]))





(deftest viterbi-test
  (let [wikipedia-model
        {:states [:H :F]
         :p-emit (fn [x y]
                   (case [x y]
                     [:H :D] 0.1
                     [:H :C] 0.4
                     [:H :N] 0.5
                     [:F :D] 0.6
                     [:F :C] 0.3
                     [:F :N] 0.1))
         :p-trans (fn [x1 x2]
                    (case [x1 x2]
                      [:H :H] 0.7
                      [:H :F] 0.3
                      [:F :H] 0.4
                      [:F :F] 0.6))
         :p-init (fn p-init [x]
                   (case x
                     :H 0.6
                     :F 0.4))}]

    (testing "path matches that on the Viterbi wikipedia page"
      (is (= [:H, :H, :F] (first (sut/viterbi wikipedia-model [:N :C :D])))))

    (testing "probability matches that on the Viterbi wikipedia page"
      (is (same/ish? 0.0151199999 (second (sut/viterbi wikipedia-model [:N :C :D])))))))

