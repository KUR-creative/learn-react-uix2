(ns app.qstart
  (:require
   [uix.core :as uix :refer [defui $]]
   [uix.dom]))

(defui my-button []
  ;; uix component doesn't need to start with a capital letter
  ;; uix takes care of it
  ($ :button "I'm a button"))

(defui app []
  ($ :div
     ($ :h1 "ppap bbab")
     ($ :<> "empty component?") ; = <>empty component</> in JSX
     ;; <>empty component</> -> "empty component"
     ;; <div>empty component<div/> -> <div>"empty component"</div>
     ;; https://stackoverflow.com/questions/69019264/difference-between-and-div
     ($ my-button)))

;;
(defonce root
  (uix.dom/create-root (js/document.getElementById "root")))

(defn render []
  (uix.dom/render-root ($ app) root))

(defn ^:export init [] ;; export, but default?
  ;; https://javascript.info/import-export
  (render))