(ns app.qstart
  (:require
   [uix.core :as uix :refer [defui $]]
   [uix.dom]))

(defui my-button [{:keys [on-click]}]
  ;; uix component doesn't need to start with a capital letter
  ;; uix takes care of it
  ($ :button {:on-click on-click}
     "I'm a button *"))

(defui b-button []
  ;; uix component doesn't need to start with a capital letter
  ;; uix takes care of it
  ($ :button "B"))

#_(defui resp-btn [{:keys [on-click children]}])

(defui app []
  (let [x -10]
    ($ :div
       ($ :h1 (str "ppap bbab" x))
       (if (pos? x)
         ($ my-button)
         ($ b-button))
       ($ :<> "empty component?") ; = <>empty component</> in JSX
     ;; <>empty component</> -> "empty component"
     ;; <div>empty component<div/> -> <div>"empty component"</div>
     ;; https://stackoverflow.com/questions/69019264/difference-between-and-div
       ($ my-button {:on-click #(js/console.log "clicked")})
       ($ :button {:on-click #(js/console.log "without defui")}
          "without defui"))))
;(js/alert "clicked")

;;
(defonce root
  (uix.dom/create-root (js/document.getElementById "root")))

(defn render []
  (uix.dom/render-root ($ app) root))

(defn ^:export init [] ;; export, but default?
  ;; https://javascript.info/import-export
  (render))
