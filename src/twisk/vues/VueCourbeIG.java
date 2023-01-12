package twisk.vues;

import javafx.application.Platform;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;
import twisk.mondeIG.CourbeIG;
import twisk.mondeIG.LigneDroiteIG;
import twisk.mondeIG.MondeIG;
import twisk.mondeIG.PointDeControleIG;

public class VueCourbeIG extends VueArcIG{

    private CubicCurve courbe;

    /**
     * Constructeur de la vue des CourbeIG
     * @param monde
     * @param arc
     */
    public VueCourbeIG(MondeIG monde, CourbeIG arc){

        super(monde, arc);

        courbe = new CubicCurve();
        courbe.setStartX(arc.getP1().getPosX());
        courbe.setStartY(arc.getP1().getPosY());
        courbe.setControlX1(arc.getX1());
        courbe.setControlY1(arc.getY1());
        courbe.setControlX2(arc.getX2());
        courbe.setControlY2(arc.getY2());
        courbe.setEndX(arc.getP2().getPosX());
        courbe.setEndY(arc.getP2().getPosY());
        courbe.setFill(null);

        courbe.getStyleClass().add("arc");
        mettreAJourTriangle(courbe.getControlX2(), courbe.getControlY2(), courbe.getEndX(), courbe.getEndY());

        courbe.endXProperty().addListener((ov, oldValue, newValue) -> mettreAJourTriangle(courbe.getControlX2(), courbe.getControlY2(), courbe.getEndX(), courbe.getEndY()));
        courbe.endYProperty().addListener((ov, oldValue, newValue) -> mettreAJourTriangle(courbe.getControlX2(), courbe.getControlY2(), courbe.getEndX(), courbe.getEndY()));

        this.getChildren().addAll(courbe);
    }

    /**
     *  Modifie le visuel de la courbe-flèche lors d'un changement d'état (sélectionné ou non)
     */
    @Override
    public void reagir() {

        Runnable command = new Runnable() {
            @Override
            public void run() {
                if (monde.estSelectionne(arc)) {
                    courbe.getStyleClass().set(0, "arc-select");
                    triangle.getStyleClass().set(0, "tete-fleche-select");
                } else {
                    courbe.getStyleClass().set(0, "arc");
                    triangle.getStyleClass().set(0, "tete-fleche");
                }
            }
        };
        if(Platform.isFxApplicationThread())
            command.run();
        else
            Platform.runLater(command);
    }

    /**
     * Attache les coordonnées des extrémités de l'arc aux coordonnées des points de contrôle correspondants.
     * @param vuePoint vue du point de contrôle auquel se rattacher
     * @param point point de contrôle auquel se rattacher
     */
    @Override
    public void setBindingPointDeControle(VuePointDeControleIG vuePoint, PointDeControleIG point) {

        if(arc.getP1().equals(point)){
            courbe.startXProperty().bind(vuePoint.layoutXProperty().add(vuePoint.getLayoutBounds().getMinX() + vuePoint.radiusProperty().intValue()));
            courbe.startYProperty().bind(vuePoint.layoutYProperty().add(vuePoint.getLayoutBounds().getMinY() + vuePoint.radiusProperty().intValue()));
        }
        if (arc.getP2().equals(point)) {
            courbe.endXProperty().bind(vuePoint.layoutXProperty().add(vuePoint.getLayoutBounds().getMinX() + vuePoint.radiusProperty().intValue()));
            courbe.endYProperty().bind(vuePoint.layoutYProperty().add(vuePoint.getLayoutBounds().getMinY() + vuePoint.radiusProperty().intValue()));
        }
    }
}
