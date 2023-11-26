(ns app.ttt
  (:require
   [uix.core :as uix :refer [defui $]]
   [uix.dom]))

;;
(defn cell
  ([] {})
  ([ox no] {:ox ox :no no}))

(defn new-board []
  (vec (repeatedly 9 cell)))

(defn place [board idx ox no]
  (assoc board idx (cell ox no)))

(defn ox [v]
  (if v
    v
    "_"))

;;
(defn print-board
  ([board view]
   (let [rows (partition 3 board)]
     (run! #(prn %) (map #(map view %) rows))
     rows))
  ([board]
   (print-board board identity)))

(defui app []
  ($ :h1 "ppap"))


;;
(defonce root
  (uix.dom/create-root (js/document.getElementById "root")))

(defn render []
  (uix.dom/render-root ($ app) root))

(defn ^:export init [] ;; export, but default?
  ;; https://javascript.info/import-export
  (render))

;;
(comment
  (print-board [0 1 2 3 4 5 6 7 8])
  (print-board (new-board))
  (print-board (place (new-board) 1 "x" 0))
  (print-board (place (place (new-board) 1 "x" 0)
                      5 "o" 1)
               #(ox (:ox %))))