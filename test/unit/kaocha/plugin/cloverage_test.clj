(ns kaocha.plugin.cloverage-test
  (:refer-clojure :exclude [symbol])
  (:require [clojure.test :refer :all]
            [clojure.tools.cli :as cli]
            [kaocha.core-ext :refer :all]
            [kaocha.plugin :as plugin]
            [kaocha.plugin.cloverage :as cov]
            [slingshot.slingshot :refer [try+]]))

(require 'matcher-combinators.test)

(defmethod assert-expr 'thrown+? [msg form]
  ;; (is (thrown? m expr))
  (let [m (second form)
        body (nthnext form 2)]
    `(try+
      ~@body
      (do-report {:type :fail, :message ~msg,
                  :expected '~form, :actual nil})
      (catch ~m e#
        (do-report {:type :pass, :message ~msg,
                    :expected '~form, :actual e#})
        e#))))

(defn- update-config' [config cli-opts]
  (let [opts (-> cli-opts
                 (cli/parse-opts cov/cli-opts)
                 :options)]
    (-> {:kaocha/cli-options opts
         :cloverage/opts config}
        (cov/update-config)
        :cloverage/opts)))

(deftest update-config-test
  (testing "when no options given"
    (testing "it populates with defaults"
      (is (= cov/default-opts (:cloverage/opts (cov/update-config {}))))))

  (let [re->str (fn [re]
                  (is (regex? re))
                  (str re))]

    (testing "--cov-output"
      (is (match? {:output "foo/bar"}
                  (update-config' {}
                                  ["--cov-output" "foo/bar"])))

      (is (match? {:output "foo/bar"}
                  (update-config' {:output "bar/baz"}
                                  ["--cov-output" "foo/bar"]))))
    (testing "--cov-text"
      (is (match? {:text? false} (update-config' {} [])))
      (is (match? {:text? true} (update-config' {:text? true} [])))
      (is (match? {:text? false} (update-config' {:text? true}
                                                 ["--no-cov-text"])))
      (is (match? {:text? true} (update-config' {} ["--cov-text"]))))

    (testing "--cov-html"
      (is (match? {:html? true} (update-config' {} [])))
      (is (match? {:html? false} (update-config' {:html? false} [])))
      (is (match? {:html? true} (update-config' {:html? false}
                                                ["--cov-html"])))
      (is (match? {:html? false} (update-config' {} ["--no-cov-html"]))))

    (testing "--emma-xml"
      (is (match? {:emma-xml? false} (update-config' {} [])))
      (is (match? {:emma-xml? true} (update-config' {:emma-xml? true} [])))
      (is (match? {:emma-xml? false} (update-config' {:emma-xml? true}
                                                     ["--no-emma-xml"])))
      (is (match? {:emma-xml? true} (update-config' {} ["--emma-xml"]))))

    (testing "--lcov"
      (is (match? {:lcov? false} (update-config' {} [])))
      (is (match? {:lcov? true} (update-config' {:lcov? true} [])))
      (is (match? {:lcov? false} (update-config' {:lcov? true}
                                                 ["--no-lcov"])))
      (is (match? {:lcov? true} (update-config' {} ["--lcov"]))))

    (testing "--codecov"
      (is (match? {:codecov? false} (update-config' {} [])))
      (is (match? {:codecov? true} (update-config' {:codecov? true} [])))
      (is (match? {:codecov? false} (update-config' {:codecov? true}
                                                    ["--no-codecov"])))
      (is (match? {:codecov? true} (update-config' {} ["--codecov"]))))

    (testing "--coveralls"
      (is (match? {:coveralls? false} (update-config' {} [])))
      (is (match? {:coveralls? true} (update-config' {:coveralls? true} [])))
      (is (match? {:coveralls? false} (update-config' {:coveralls? true}
                                                      ["--no-coveralls"])))
      (is (match? {:coveralls? true} (update-config' {} ["--coveralls"]))))

    (testing "--cov-summary"
      (is (match? {:summary? true} (update-config' {} [])))
      (is (match? {:summary? false} (update-config' {:summary? false} [])))
      (is (match? {:summary? true} (update-config' {:summary? false}
                                                   ["--cov-summary"])))
      (is (match? {:summary? false} (update-config' {} ["--no-cov-summary"]))))

    (testing "--cov-fail-threshold PERCENT"
      (is (match? {:fail-threshold 0} (update-config' {} [])))
      (is (match? {:fail-threshold 20} (update-config' {:fail-threshold 20} [])))
      (is (match? {:fail-threshold 42} (update-config' {:fail-threshold 20} ["--cov-fail-threshold" "42"])))
      (is (match? {:fail-threshold 42} (update-config' {} ["--cov-fail-threshold" "42"]))))

    (testing "--cov-low-watermark PERCENT"
      (is (match? {:low-watermark 50} (update-config' {} [])))
      (is (match? {:low-watermark 20} (update-config' {:low-watermark 20} [])))
      (is (match? {:low-watermark 42} (update-config' {:low-watermark 20} ["--cov-low-watermark" "42"])))
      (is (match? {:low-watermark 42} (update-config' {} ["--cov-low-watermark" "42"]))))

    (testing "--cov-high-watermark PERCENT"
      (is (match? {:high-watermark 80} (update-config' {} [])))
      (is (match? {:high-watermark 20} (update-config' {:high-watermark 20} [])))
      (is (match? {:high-watermark 42} (update-config' {:high-watermark 20} ["--cov-high-watermark" "42"])))
      (is (match? {:high-watermark 42} (update-config' {} ["--cov-high-watermark" "42"]))))

    (testing "--nop"
      (is (match? {:nop? false} (update-config' {} [])))
      (is (match? {:nop? true} (update-config' {:nop? true} [])))
      (is (match? {:nop? false} (update-config' {:nop? true}
                                                ["--no-cov-nop"])))
      (is (match? {:nop? true} (update-config' {} ["--cov-nop"]))))

    (testing "--cov-ns-regex"
      (is (= [] (:ns-regex (update-config' {} []))))
      (is (= ["foo.*"] (map re->str (:ns-regex (update-config' {:ns-regex ["foo.*"]} [])))))
      (is (= ["bar"] (map re->str (:ns-regex (update-config' {:ns-regex ["foo"]} ["--cov-ns-regex" "bar"]))))))

    (testing "--cov-src-ns-path"
      (is (= [] (:src-ns-path (update-config' {} []))))
      (is (= ["src"] (:src-ns-path (update-config' {:src-ns-path ["src"]} []))))
      (is (= ["src"] (:src-ns-path (update-config' {} ["--cov-src-ns-path" "src"])))))

    (testing "--cov-ns-exclude-regex"
      (is (= [] (:ns-exclude-regex (update-config' {} []))))
      (is (= ["foo.*"] (map re->str (:ns-exclude-regex (update-config' {:ns-exclude-regex ["foo.*"]} [])))))
      (is (= ["foo" "bar"] (map re->str (:ns-exclude-regex (update-config' {:ns-exclude-regex ["foo"]} ["--cov-ns-exclude-regex" "bar"]))))))

    (testing "--cov-exclude-call"
      (is (= [] (:exclude-call (update-config' {} []))))
      (is (= ['my.ns/fn] (:exclude-call (update-config' {:exclude-call ['my.ns/fn]} []))))
      (is (= ['my.other.ns/fn] (:exclude-call (update-config' {} ["--cov-exclude-call" "my.other.ns/fn"]))))
      (is (= ['my.ns/fn 'my.other.ns/fn] (:exclude-call (update-config' {:exclude-call ['my.ns/fn]} ["--cov-exclude-call" "my.other.ns/fn"])))))

    (testing "--test-ns-regex"
      (is (= [] (:test-ns-regex (update-config' {} []))))
      (is (= ["foo.*"] (map re->str (:test-ns-regex (update-config' {:test-ns-regex ["foo.*"]} [])))))
      (is (= ["foo" "bar"] (map re->str (:test-ns-regex (update-config' {:test-ns-regex ["foo"]} ["--cov-test-ns-regex" "bar"]))))))))

(deftest run-cloverage-test
  (let [arglists (:arglists (meta #'cloverage.coverage/run-main))]
    (try
      (let [run-main (fn [a1] a1)]
        (alter-meta! #'cloverage.coverage/run-main assoc :arglists '([:a1]))
        (with-redefs [cloverage.coverage/run-main run-main]
          (is (= [:opts] (cov/run-cloverage :opts)))))

      (let [run-main (fn [a1 a2] [a1 a2])]
        (alter-meta! #'cloverage.coverage/run-main assoc :arglists '([:a1 :a2]))
        (with-redefs [cloverage.coverage/run-main run-main]
          (is (= [[:opts] {}] (cov/run-cloverage :opts)))))

      (testing "no matching clause"
        (alter-meta! #'cloverage.coverage/run-main assoc :arglists '([:a1 :a2 :a3]))
        (is (thrown? java.lang.IllegalArgumentException (cov/run-cloverage :opts))))

      (finally
        (alter-meta! #'cloverage.coverage/run-main assoc :arglists arglists)))))

(deftest cloverage-plugin-test
  (let [chain (plugin/load-all [:kaocha.plugin/cloverage])]

    (testing "cli-options"
      (is (= [["-h" "--help" "print help"]
              [nil "--cov-output PATH" "Cloverage output directory."]]
             (take 2 (plugin/run-hook* chain :kaocha.hooks/cli-options [["-h" "--help" "print help"]])))))

    (testing "config"
      (is (contains? (plugin/run-hook* chain :kaocha.hooks/config {})
                     :cloverage/opts)))

    (with-redefs [kaocha.api/run (fn [config]
                                   {:kaocha.result/tests [{:kaocha.result/count 3
                                                           :kaocha.result/pass 1
                                                           :kaocha.result/fail 2}]})
                  ;; unfortunately we can't invoke Cloverage "for real", or it
                  ;; will really mess up our coverage results
                  cloverage.coverage/run-main (fn [[opts] & _]
                                                ((cloverage.coverage/runner-fn {:runner :kaocha}) opts))]

      (is (thrown+? :kaocha/early-exit
                    (plugin/run-hook* chain :kaocha.hooks/main {:cloverage/opts {:src-ns-path ["src"]}})))

      (is (thrown+? :kaocha/early-exit
                    (plugin/run-hook* chain
                                      :kaocha.hooks/main
                                      (kaocha.config/normalize {:tests [{:source-paths ["src"]}]})))))))
