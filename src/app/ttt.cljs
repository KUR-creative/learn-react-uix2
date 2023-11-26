(ns app.ttt
  (:require
   [uix.core :as uix :refer [defui $]]
   [uix.dom]))

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