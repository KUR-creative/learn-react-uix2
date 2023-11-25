(ns app.ttt
  (:require
   [uix.core :as uix :refer [defui $]]
   [uix.dom]))

;;
(defn init-moves [] (vector))
(defn init-board [] (vec (repeat 9 "_")))

(def next-turn {"o" "x", "x" "o"})
(defn next-moves [moves pos]
  (conj moves pos))
(defn next-board [board pos ox]
  (assoc board pos ox))
(defn placeable? [board pos]
  (= (board pos) "_"))

(defn set-next! [pos {:keys [moves set-moves!
                             board set-board!
                             turn set-turn!]}]
  (when (placeable? board pos)
    (prn pos) (prn moves)
    (set-moves! (next-moves moves pos))
    (set-board! (next-board board pos turn))
    (set-turn! (next-turn turn))))

;;
(defui cell [{:keys [ox pos state]}]
  ($ :td {:key key
          :on-click #(set-next! pos state)
          :style {:border "1px solid black"
                  :font-size "xxx-large"
                  :padding "10px 20px"}}
     ox))

(defui board-table [{:keys [state]}]
  ($ :table {:style {:border-collapse "collapse"}}
     ($ :tbody
        (->> (:board state)
             (map-indexed (fn [idx ox]
                            ($ cell {:ox ox :pos idx :state state
                                     :key idx})))
             (partition 3)
             (map-indexed (fn [idx tds]
                            ($ :tr {:key idx} tds)))))))

(defui moves-list [{:keys [moves]}]
  ($ :ol
     (map #($ :li {:key %}
              ($ :button %)) moves)))

(defui app []
  (let [[moves set-moves!] (uix/use-state init-moves)
        [board set-board!] (uix/use-state init-board)
        [turn set-turn!] (uix/use-state "o")
        state {:moves moves :set-moves! set-moves!
               :board board :set-board! set-board!
               :turn turn :set-turn! set-turn!}]
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