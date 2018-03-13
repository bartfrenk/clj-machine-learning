(def project 'clj-machine-learning)
(def version "0.1.0-SNAPSHOT")

(set-env! :resource-paths #{"src"}
          :source-paths   #{"test"}
          :dependencies   '[[org.clojure/clojure "1.9.0"]
                            [adzerk/boot-test "RELEASE" :scope "test"]
                            [same/ish "0.1.0"]
                            [incanter "1.5.7"]])

(task-options!
 pom {:project     project
      :version     version
      :description "Machine learning algorithms implemented in Clojure."
      :scm         {:url "https://github.com/bartfrenk/clj-machine-learning"}
      :license     {"Eclipse Public License"
                    "http://www.eclipse.org/legal/epl-v10.html"}})


(deftask build
  "Build and install the project locally."
  []
  (comp (pom) (jar) (install)))


(require '[adzerk.boot-test :refer [test]])
