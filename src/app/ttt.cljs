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
  #_($ :table
       ($ :tr ($ :td 1) ($ :td "x") ($ :td "o"))
       ($ :tr ($ :td 4) ($ :td 5) ($ :td 6))
       ($ :tr ($ :td 7) ($ :td 8) ($ :td 9)))
  ($ :div
     ($ :table {:style {:border-collapse "collapse"
                     ;:width "50%"
                        }}
        ($ :tr ($ cell) ($ cell) ($ cell))
        ($ :tr ($ cell) ($ cell) ($ cell))
        ($ :tr ($ cell) ($ cell) ($ cell))))
  #_($ :p
       ($ :h1 "tic tac toe")
       ($ :table
          ($ :tr ($ :td) ($ :td) ($ :td))
          ($ :tr ($ :td) ($ :td) ($ :td))
          ($ :tr ($ :td) ($ :td) ($ :td)))))

;;
(defonce root
  (uix.dom/create-root (js/document.getElementById "root")))

(defn render []
  (uix.dom/render-root ($ app) root))

(defn ^:export init [] ;; export, but default?
  ;; https://javascript.info/import-export
  (render))