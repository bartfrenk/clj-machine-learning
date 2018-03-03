(ns ml.online.core-test
  (:require [clojure.test :refer :all]
            [ml.online.core :refer :all]))

(deftest online-mean-test
  (testing "short range of numbers gives correct result"
    (is (= [1.0 1.5 2.0] (into [] sample-mean [1 2 3])))))


(deftest online-test
  (testing "sample mean and mean implemented by online are equal"
    (let [f (online (fn [[s n] x] [(+ s x) (inc n)])
                    (fn [[s n]]  (/ s n))
                    [0 0])
          xs (range 10)]
      (is (= (into [] sample-mean xs)
             (map float (into [] f xs)))))))
