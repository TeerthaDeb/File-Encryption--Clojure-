(def default-directory ".") ; Default to current director

(ns a2
  (:require [clojure.string :as str]
            [clojure.java.io :as io]
            [compress :refer [option3 option4]]))




; Display the menu and ask the user for the option
(defn showMenu
  []
  (println "\n\n*** Compression Menu ***")
  (println "------------------\n")
  (println "1. Display list of files")
  (println "2. Display file contents")
  (println "3. Compress a file")
  (println "4. Uncompress a file")
  (println "5. Exit")
  (do 
    (print "\nEnter an option? ") 
    (flush) 
    (read-line)))


(defn option1
  []
  (println "File List")
  (println "_________\n\n\n")
  (let [current-dir (io/file ".")] ; Uses current directory
    (doseq [file (filter #(.isFile %) (file-seq current-dir))]
      (print "* ")
      (println (.getPath file)))))



    
    
    
; Read and display the file contents (if the file exists). Java's File class can be used to 
; check for existence first. 
(defn option2
  []
  (print "\nPlease enter a file name => ")
  (flush)
  (let [default-directory "."
        directory (or (System/getenv "FILE_DIRECTORY") default-directory)
        file-name (str directory "/" (str/trim (read-line)))] ; Add a "/" separator
    (try
      (let [content (slurp file-name)]
        (println "\nFile content:")
        (println content))
      (catch Exception e
        (println "Oops! Specified file does not exist in " directory)))))

        

; If the menu selection is valid, call the relevant function to 
; process the selection
(defn processOption
  [option] ; other parm(s) can be provided here, if needed
  (if( = option "1")
     (option1)
     (if( = option "2")
        (option2)
        (if( = option "3")
           (compress/option3)  ; Call option3 from the compress module
           (if( = option "4")
              (compress/option4)  ; Call option4 from the compress module
              (println "Invalid Option, please try again"))))))


; Display the menu and get a menu item selection. Process the
; selection and then loop again to get the next menu selection
(defn menu
  [] ; parm(s) can be provided here, if needed
  (let [option (str/trim (showMenu))]
    (if (= option "5")
      (println "\nGood Bye.\n")
      (do 
         (processOption option)
         (recur )))))   ; other args(s) can be passed here, if needed




; ------------------------------
; Run the program. You might want to prepare the data required for the mapping operations
; before you display the menu. You don't have to do this but it might make some things easier

(menu) ; other args(s) can be passed here, if needed