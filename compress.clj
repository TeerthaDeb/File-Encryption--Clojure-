(ns compress
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))




;-------------------------------------- perfectly working.
(defn get-word-index [word frequency-map]
   (let [lowercase-word (clojure.string/lower-case word)]
     (get frequency-map lowercase-word)))

 (defn option3 [] ; ----------------------- Option 3
   (println "\nPlease enter a file name => ")
   (let [file-name (read-line)]
     (if (not (.exists (java.io.File. file-name)))
       (println "Opps ! File not found.")
       (let [freq-indices (with-open [freq-file (clojure.java.io/reader "frequency.txt")]
                            (reduce (fn [acc line]
                                      (let [words (re-seq #"\S+" (clojure.string/trim line))]
                                        (reduce (fn [inner-acc word]
                                                  (let [lowercase-word (clojure.string/lower-case word)]
                                                    (if (contains? inner-acc lowercase-word)
                                                      inner-acc
                                                      (assoc inner-acc lowercase-word (count inner-acc)))))
                                                acc
                                                words)))
                                    {}
                                    (line-seq freq-file)))]
         (with-open [input-file (clojure.java.io/reader file-name)
                     output-file (clojure.java.io/writer (str file-name ".clt"))]
           (let [text (slurp input-file)
                 words (re-seq #"\b\w+\b|\p{Punct}" text)
                 compressed-words (map #(if (re-matches #"\b\w+\b" %)
                                          (if (re-matches #"\d+" %)
                                            (str "@" % "@")
                                            (or (get-word-index % freq-indices) %))
                                          %)
                                       words) ; Map operation directly on 'words'
                  compressed-text (apply str (interpose " " compressed-words))] ; Use 'apply str' to join elements
             (spit output-file compressed-text)
             (println "file compressed Successfully.\n\n")))))))

;-----------------------------------------------

(require '[clojure.string :as string])

(defn replace-clt-with-ct [file-name]
  (string/replace file-name #"\.clt$" ".ct"))

(defn option4 []
   (println "\nPlease enter a file name => ")
   (let [file-name (read-line)]
     (if (not (.exists (java.io.File. file-name)))
       (println "Opps! File not found.")
       (let [freq-indices (with-open [freq-file (clojure.java.io/reader "frequency.txt")]
                            (reduce (fn [acc line]
                                      (let [words (re-seq #"\S+" (clojure.string/trim line))]
                                        (reduce (fn [inner-acc word]
                                                  (assoc inner-acc (count inner-acc) word))
                                                acc
                                                words)))
                                    {}
                                    (line-seq freq-file)))]
         (with-open [input-file (clojure.java.io/reader file-name)
                     output-file (clojure.java.io/writer (str (replace-clt-with-ct file-name)))]
           (let [compressed-text (clojure.string/split (slurp input-file) #"\s+")
                 decompressed-text (->> compressed-text
                                        (map #(if (re-matches #"\b\d+\b" %)
                                                (get freq-indices (Integer/parseInt %))
                                                %)) ;; Use word directly if not a number
                                        (remove nil?) ;; Remove nil values (non-matching numbers)
                                        (clojure.string/join " "))] ;; Join the words
             (spit output-file decompressed-text)
             (println "file uncompressed Successfully.\n\n")))))))
;-----------------------------------------------