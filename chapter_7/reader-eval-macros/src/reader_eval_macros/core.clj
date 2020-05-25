(ns reader-eval-macros.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

; 1. Use the list function, quoting, and read-string to create a list that, when evaluated, prints your first name and your favorite sci-fi movie.
(def read-name-movie
  (eval (eval (read-string "(list println \"tyler\" \"shrek\")"))))

; 2. Create an infix function that takes a list like (1 + 3 * 4 - 5) and transforms it into the lists that Clojure needs in order to correctly evaluate the expression using operator precedence rules.

;; NOTE: Below function does not work as intended, see 'infix below.
; (defn infix-helper
;   [first second & more]
;   (if (== 1 (count more))
;     (let [third (first more)]
;       (list second first third))
;     (let [[newfirst newsecond & more] more]
;       (list second first (infix-helper newfirst newsecond more))))
; )

;; NOTE: This function below assumes a 100% left-first-filling AST, regardless of the particular operators used.
(defn infix
  [arr]
  (if (== 1 (count arr))
    (first arr)
    (apply (fn [first second & more]
             (list second first (infix more))) arr)))

; (defn apply-test
;   [first second & more]
;   (println first second more))

; (defn switch-two-test
;   [arr]
;   (apply apply-test arr))

;; NOTE: This was experimental for use in determining strategy.
(defn switch-two
  [arr]
  (apply (fn [first second & more]
           (list second first more)) arr))


