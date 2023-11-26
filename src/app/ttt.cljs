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

(defn place [board pos ox no]
  (assoc board pos (cell ox no)))

(defn ox [v]
  (if v
    v
    "_"))
(defn cell-ox [cell]
  (ox (:ox cell)))

;;
(defn print-board
  ([board view]
   (let [rows (partition 3 board)]
     (run! #(prn %) (map #(map view %) rows))
     rows))
  ([board]
   (print-board board identity)))

(defui box [{:keys [ox]}]
  ($ :td {:key key
          :style {:border "1px solid black"
                  :font-size "xxx-large"
                  :padding "10px 20px"}}
     ox))

(defui board-table [{:keys [board]}]
  ($ :table {:style {:border-collapse "collapse"}}
     ($ :tbody
        (->> board
             (map-indexed (fn [idx cell]
                            ($ box {:ox (cell-ox cell) ;:pos idx 
                                    :key idx})))
             (partition 3)
             (map-indexed (fn [idx tds]
                            ($ :tr {:key idx} tds)))))))

(defui app []
  (let [[board set-board!] (uix/use-state new-board)]
    ($ :div
       #_($ game-status {:turn turn})
       ($ :div {:style {:display "flex"}}
          ($ board-table {:board board})
          #_($ moves-list {:state state})))))


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