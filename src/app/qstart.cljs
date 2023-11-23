(ns app.qstart
  (:require
   [uix.core :as uix :refer [defui $]]
   [uix.dom]))

(defui app []
  ($ :button "I'm a button"))

(defonce root
  (uix.dom/create-root (js/document.getElementById "root")))

(defn render []
  (uix.dom/render-root ($ app) root))

(defn ^:export init []
  (render))