(ns app.ttt
  (:require
   [uix.core :as uix :refer [defui $]]
   [uix.dom]))

;;
(def turn-cycle (cycle [:x :o]))
(def moves [])
(def board (vec (repeat 9 "_")))

(defn init-moves [] (vector))
(defn init-board [] (vec (repeat 9 "_")))
;(defn init-state [] {:moves (init-moves) :board (init-board)})

(defn next-moves [moves pos]
  (conj moves pos))
(defn next-board [board pos ox]
  (assoc board pos ox))
(defn placeable? [board pos]
  (= (board pos) "_"))

;;
(defui cell [{ox :ox pos :pos
              {:keys [moves set-moves! board set-board!]} :state}]
  ($ :td {:key key
          :on-click (fn []
                      (when (placeable? board pos)
                        (prn pos)
                        (prn moves)
                        (set-moves! (next-moves moves pos))))
          :style {:border "1px solid black"
                  :font-size "xxx-large"
                  :padding "10px 20px"}}
     (if (placeable? board pos)
       ox
       "_")))

(defui board-table [{:keys [state]}]
  ($ :table {:style {:border-collapse "collapse"}}
     (->> (:board state)
          (map-indexed (fn [idx ox]
                         ($ cell {:ox ox :pos idx :state state
                                  :key idx})))
          (partition 3)
          (map #($ :tr %)))))

(defui moves-list [{:keys [moves]}]
  ($ :ol
     (map #($ :li {:key %}
              ($ :button %)) moves)))

(defui app []
  (let [[moves set-moves!] (uix/use-state init-moves)
        [board set-board!] (uix/use-state init-board)
        state {:moves moves :set-moves! set-moves!
               :board board :set-board! set-board!}]
    ($ :div {:style {:display "flex"}}
       ($ board-table {:state state})
       ($ moves-list {:moves moves}))))

;;
(defonce root
  (uix.dom/create-root (js/document.getElementById "root")))

(defn render []
  (uix.dom/render-root ($ app) root))

(defn ^:export init [] ;; export, but default?
  ;; https://javascript.info/import-export
  (render))