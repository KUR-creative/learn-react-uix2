(ns app.ttt
  (:require
   [uix.core :as uix :refer [defui $]]
   [uix.dom]))

(defui cell [{:keys [ox]}]
  ($ :td {:style {:border "1px solid black"
                  :font-size "xxx-large"
                  :padding "10px 20px"}}
     (if ox
       ox
       "_")))

(defui app []
  ($ :div {:style {:display "flex"}}
     ($ :div "Player: X"
        ($ :table {:style {:border-collapse "collapse"}}
           ($ :tr ($ cell) ($ cell) ($ cell))
           ($ :tr ($ cell) ($ cell) ($ cell))
           ($ :tr ($ cell) ($ cell) ($ cell))))
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