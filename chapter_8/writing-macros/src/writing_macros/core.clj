(ns writing-macros.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

; (when-valid order-details order-details-validations
;             (println "It was a success!")
;             (render :success))

(def order-details
  {:name "Mitchard Blimmons"
   :email "mitchard.blimmonsgmail.com"})

(def order-details-validations
  {:name
   ["Please enter a name" not-empty]

   :email
   ["Please enter an email address" not-empty

    "Your email address doesn't look like an email address"
    #(or (empty? %) (re-seq #"@" %))]})

(defn error-messages-for
  "Return a seq of error messages"
  [to-validate message-validator-pairs]
  (map first (filter #(not ((second %) to-validate))
                     (partition 2 message-validator-pairs))))

; (error-messages-for "" ["Please enter a name" not-empty])

(defn validate
  "Returns a map with a vector of errors for each key"
  [to-validate validations]
  (reduce (fn [errors validation]
            (let [[fieldname validation-check-groups] validation
                  value (get to-validate fieldname)
                  error-messages (error-messages-for value validation-check-groups)]
              (if (empty? error-messages)
                errors
                (assoc errors fieldname error-messages))))
          {}
          validations))

; (validate order-details order-details-validations)

(defmacro if-valid
  "Handle validation more concisely"
  [to-validate validations errors-name & then-else]
  `(let [~errors-name (validate ~to-validate ~validations)]
     (if (empty? ~errors-name)
       ~@then-else)))

; (macroexpand
;  '(if-valid order-details order-details-validations my-error-name
;             (println :success)
;             (println :failure my-error-name)))

; ---------------------------------------------------------------

; 1. Write the macro when-valid so that it behaves similarly to when. Here is an example of calling it:
; (when-valid order-details order-details-validations
;             (println "It's a success!")
;             (render :success))
; When the data is valid, the println and render forms should be evaluated, and when-valid should return nil if the data is invalid.

; Note: Here's the implementation of 'when:
; (defmacro when
;   "Evaluates test. If logical true, evaluates body in an implicit do."
;   {:added "1.0"}
;   [test & body]
;   (list 'if test (cons 'do body)))
;   
; (macroexpand '(when (the-cows-come :home)
;                 (call me :pappy)
;                 (slap me :silly)))
; => (if (the-cows-come :home)
;        (do (call me :pappy)
;            (slap me :silly)))

(defmacro when-valid
  "Handle validation more concisely"
  [to-validate validations errors-name & then]
  `(let [errors-name# (validate ~to-validate ~validations)]
     (if (empty? errors-name#)
       (do ~@then)
       nil)))

; 2. You saw that and is implemented as a macro. Implement or as a macro.
; (defmacro and
;   "Evaluates exprs one at a time, from left to right. If a form
;   returns logical false (nil or false), and returns that value and
;   doesn't evaluate any of the other expressions, otherwise it returns
;   the value of the last expr. (and) returns true."
;   {:added "1.0"}
;   ([] true)
;   ([x] x)
;   ([x & next]
;    `(let [and# ~x]
;       (if and# (and ~@next) and#))))

(defmacro or
  "Evaluates exprs one at a time, from left to right. If a form
returns a logical true value, or returns that value and doesn't
evaluate any of the other expressions, otherwise it returns the
value of the last expression. (or) returns nil."
  ([] nil)
  ([x] x)
  ([x & next]
   `(let [or# ~x]
      (if or# or# (or ~@next)))))

; 3. In Chapter 5 you created a series of functions (c-int, c-str, c-dex) to read an RPG character’s attributes. Write a macro that defines an arbitrary number of attribute-retrieving functions using one macro call. Here’s how you would call it:
; (defattrs c-int :intelligence
;   c-str :strength
;   c-dex :dexterity)

; TODO: Return to this problem once I find my code from earlier chapters :)))))