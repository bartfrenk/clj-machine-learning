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

(defn sample-gaussian [n]
  (let [rng (Random.)]
    (repeatedly n #(.nextGaussian rng))))

(defn sample-multivariate-gaussian [mu cov]
  (let [n (m/ecount mu)
        e (m/scale (m/identity-matrix n) 1e-8)
        L (:L (mp/cholesky (m/add cov e)
                           {:results [:L]}))
        u (m/matrix (sample-gaussian n))
        samples (m/add mu (m/mmul L u))]
    (m/to-nested-vectors samples)))

(defn generate-targets
  [ws xs beta]
  (let [sd (/ beta)
        exps (map (partial m/mmul ws) xs)
        n (count xs)
        targets (map + exps (d/sample n (d/normal {:mu 0 :sd sd})))]
    targets))

(defn generate-data
  [ws n beta]
  (let [xs (map vector (repeat 1) (range n))
        ys (generate-targets ws xs beta)]
    [(m/matrix xs) (m/matrix ys)]))
