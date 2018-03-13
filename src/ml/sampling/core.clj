(ns ml.sampling.core
  (:require [incanter.charts :as c]
            [incanter.core :as i]))


(defn sample-exp
  "Sample from the exponential distribution with mean 1/lambda."
  [lambda]
  (- (/ (Math/log (- 1 (rand))) lambda)))


(-> (repeatedly 10000 #(sample-exp 1))
    (c/histogram :nbins 25)
    (i/view))

(-> (repeatedly 10000 #(rand))
    (c/histogram :nbins 25)
    (i/view))
