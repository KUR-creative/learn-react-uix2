(ns app.ttt
  (:require
   [uix.core :as uix :refer [defui $]]
   [uix.dom]))

;;
(def turn-cycle (cycle [:x :o]))
(def moves [])
(def board (vec (repeat 9 "_")))

(defn next-moves [moves pos]
  (conj moves pos))
(defn next-board [board pos ox]
  (assoc board pos ox))

;;
(defui cell [{:keys [ox key]}]
  ($ :td {:key key
          :style {:border "1px solid black"
                  :font-size "xxx-large"
                  :padding "10px 20px"}}
     (if ox
       ox
       "_")))

(defui board-table [{:keys [board]}]
  ($ :table {:style {:border-collapse "collapse"}}
     (->> board
          (map-indexed (fn [idx ox] ($ cell {:ox ox :key idx})))
          (partition 3)
          (map #($ :tr %)))))

(defui app []
  ($ :div {:style {:display "flex"}}
     ($ board-table {:board board})
     ($ :ol
        ($ :li ($ :button "test"))
        ($ :li ($ :button "ppap")))))

;;
(defonce root
  (uix.dom/create-root (js/document.getElementById "root")))

(defn render []
  (uix.dom/render-root ($ app) root))

(defn ^:export init [] ;; export, but default?
  ;; https://javascript.info/import-export
  (render))