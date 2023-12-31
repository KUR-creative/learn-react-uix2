(ns app.ttt
  (:require
   [clojure.string :refer [capitalize]]
   [uix.core :as uix :refer [defui $]]
   [uix.dom]))

;;
(def empty-cell {})
(defn cell
  ([] empty-cell)
  ([ox no] {:ox ox :no no}))

(defn new-board []
  (vec (repeatedly 9 cell)))

(defn placeable? [board pos]
  (nil? (:ox (board pos))))
(defn place [board pos ox no]
  (assoc board pos (cell ox no)))

(defn ox [v]
  (if v
    v
    "_"))
(defn cell-ox [cell]
  (ox (:ox cell)))

(defn history [board]
  (sort-by :no (filter :no board)))
(defn latest-cell [board]
  (apply max-key :no (filter :no board)))
(def next-player
  {nil "x" ; first turn
   "o" "x"
   "x" "o"})

(defn rowify [vec9]
  (->> vec9
       (partition 3)
       (map vec)
       vec))
(defn colify [rows]
  (apply map vector rows))
(defn diagonals [vec9]
  [[(vec9 0) (vec9 4) (vec9 8)]
   [(vec9 2) (vec9 4) (vec9 6)]])
(defn win-candidates [vec9]
  (let [rows (rowify vec9)]
    (concat rows (colify rows) (diagonals vec9))))
(defn winner [vec9]
  (some #(when (apply = %) (first %))
        (win-candidates vec9)))

(defn board-at [board n-move]
  (mapv #(if (<= (:no %) n-move) % empty-cell) board))

;;
(defn print-board
  ([board view]
   (let [rows (partition 3 board)]
     (run! #(prn %) (map #(map view %) rows))
     rows))
  ([board]
   (print-board board identity)))

(defui cell-td [{:keys [ox pos a-winner
                        board set-board! n-move set-n-move!]}]
  ($ :td {:key key
          :on-click #(let [{now-ox :ox no :no} (latest-cell board)]
                       (when (and (not a-winner) (placeable? board pos))
                         (set-board! (place board pos
                                            (next-player now-ox)
                                            (inc no)))
                         (set-n-move! (inc n-move))))
          :style {:border "1px solid black"
                  :font-size "xxx-large"
                  :padding "10px 20px"}}
     ox))

(defui board-table [{{:keys [board set-board! n-move set-n-move!]} :state}]
  ($ :table {:style {:border-collapse "collapse"}}
     ($ :tbody
        (let [the-board (board-at board n-move)
              a-winner (winner (mapv :ox the-board))]
          (->> the-board
               (map-indexed (fn [idx cell]
                              ($ cell-td {:ox (cell-ox cell) :pos idx
                                          :a-winner a-winner
                                          :board the-board :set-board! set-board!
                                          :n-move n-move :set-n-move! set-n-move!
                                          :key idx})))
               (partition 3)
               (map-indexed (fn [idx tds]
                              ($ :tr {:key idx} tds))))))))

(defui move-li [{:keys [no set-n-move!]}]
  ($ :li {:key no}
     ($ :button {:on-click #(set-n-move! no)}
        (str "Go to " (if (pos? no)
                        (str "move #" no)
                        "game start")))))

(defui moves-ol [{:keys [board n-move set-n-move!]}]
  (let [moves (history board)]
    ($ :ol
       (cons ($ move-li {:key -1 :no 0
                         :n-move n-move :set-n-move! set-n-move!})
             (map-indexed (fn [idx {no :no}]
                            ($ move-li {:key idx :no no
                                        :n-move n-move :set-n-move! set-n-move!}))
                          moves)))))

(defui game-status [{:keys [board n-move]}]
  ;; Add who win
  (let [the-board (board-at board n-move)
        a-winner (winner (mapv :ox the-board))
        turn (-> the-board latest-cell :ox next-player)]
    ($ :h3
       (if a-winner
         (str "Winner: " (capitalize a-winner))
         (str "Player: " (if turn
                           (capitalize turn)
                           "nil? wtf?"))))))

(defui app []
  (let [[board set-board!] (uix/use-state new-board)
        [n-move set-n-move!] (uix/use-state 0)
        state {:board board :set-board! set-board!
               :n-move n-move :set-n-move! set-n-move!}]
    ($ :div
       ($ game-status {:board board :n-move n-move})
       ($ :div {:style {:display "flex"}}
          ($ board-table {:state state})
          ($ moves-ol {:board board
                       :n-move n-move :set-n-move! set-n-move!})))))

;;
(defonce root
  (uix.dom/create-root (js/document.getElementById "root")))

(defn render []
  (uix.dom/render-root ($ app) root))

(defn ^:export init []
  (render))

;;
(comment
  (print-board [0 1 2 3 4 5 6 7 8])
  (print-board (new-board))
  (print-board (place (new-board) 1 "x" 0))
  (print-board (place (place (new-board) 1 "x" 0)
                      5 "o" 1)
               #(ox (:ox %)))

  (next-player (-> (new-board) latest-cell :ox))
  (def board (place (place (new-board) 1 "x" 0)
                    5 "o" 1))
  (next-player (-> (place board 0 "x" 2) latest-cell :ox))
  (history (place board 0 "x" 2))
  (def board (place board 0 "x" 2))

  (def n-move 1)
  (board-at board 0)
  (board-at board 1)
  (board-at board 2)
  (print-board (board-at board 6) #(ox (:ox %)))

  (some #(when (apply = %) %) (partition 3 [0 1 0
                                            1 1 0
                                            1 1 2]))
  (rowify [1 2 3
           0 0 0
           4 5 6])
  (colify (rowify [1 2 3
                   0 0 0
                   4 5 6]))
  (diagonals [1 2 3
              0 0 0
              4 5 6])
  (winner [1 2 3
           0 0 0
           4 5 6])
  (winner [1 2 3
           1 0 0
           1 5 6])
  (winner [7 2 3
           1 1 0
           1 5 7])
  (winner [7 2 3
           1 7 0
           1 5 7])
  (winner [1 1 1
           1 3 nil
           nil nil nil])

  (winner [nil nil nil
           1 1 1
           1 3 nil])
  (win-candidates [1 2 3
                   1 0 0
                   1 5 6]))
