package twisk.vues;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Shape;
import twisk.mondeIG.ArcIG;
import twisk.mondeIG.MondeIG;
import twisk.mondeIG.PointDeControleIG;
import twisk.outils.TailleComposants;

public abstract class VueArcIG extends Pane implements Observateur {

    protected MondeIG monde;
    protected ArcIG arc;
    protected Polyline triangle;

    /**
     * Constructeur de la vue des ArcIG
     * @param monde
     * @param arc
     */
    public VueArcIG(MondeIG monde, ArcIG arc){

        this.monde = monde;
        this.arc = arc;

        this.setOnMouseClicked(event -> {
            if(!monde.enSimulation())
                monde.selectionnerArc(arc);
        });
        this.setPickOnBounds(false);

        monde.ajouterObservateur(this);
    }

    /**
     * Attache les coordonnées des extrémités de l'arc aux coordonnées des points de contrôle correspondants.
     * @param vuePoint vue du point de contrôle auquel se rattacher
     * @param point point de contrôle auquel se rattacher
     */
    public abstract void setBindingPointDeControle(VuePointDeControleIG vuePoint, PointDeControleIG point);

    /**
     * Mise à jour à jour l'affichage du triangle en extrémité de flèche
     * @param debutX début de la ligne (X)
     * @param debutY début de la ligne (Y)
     * @param finX fin de la ligne (X)
     * @param finY fin de la ligne (Y)
     */
    protected void mettreAJourTriangle(double debutX, double debutY, double finX, double finY){
        if(this.getChildren().contains(triangle))
            this.getChildren().remove(triangle);

        triangle = dessinerTriangleFleche(debutX, debutY, finX, finY);
        triangle.getStyleClass().add("tete-fleche");
        this.getChildren().add(triangle);
    }

    /**
     * Fonction permettant de dessiner le triangle en bout de ligne pour créer visuellement une flèche
     * @param debutX début de la ligne (X)
     * @param debutY début de la ligne (Y)
     * @param finX fin de la ligne (X)
     * @param finY fin de la ligne (Y)
     */
    protected Polyline dessinerTriangleFleche(double debutX, double debutY, double finX, double finY){

        double c = (debutY - finY) / ((debutX - finX) == 0 ? 1 : (debutX - finX));
        double angle = (debutX - finX) == 0 ? Math.toRadians(90 * ((debutY - finY) < 0 ? 1 : -1)) : Math.atan(c);
        double angleTriangle = debutX > finX ? Math.toRadians(25) : -Math.toRadians(210);
        double tailleFleche = Math.sqrt(Math.pow(debutX - finX, 2) + Math.pow(debutY - finY, 2));
        double tailleTriangle = TailleComposants.getInstance().getTailleTriangleFleche();
        double rayonPoint = TailleComposants.getInstance().getTaillePointControle();
        double decalagePoint = monde.enSimulation() ? 1.0 : (1.0 - rayonPoint / tailleFleche);
        int x1, y1, x2, y2, x3, y3;
        x1 = (int)(debutX + decalagePoint * (finX - debutX));
        y1 = (int)(debutY + decalagePoint * (finY - debutY));
        x2 = (int)(x1 + tailleTriangle * Math.cos(angle - angleTriangle));
        y2 = (int)(y1 + tailleTriangle * Math.sin(angle - angleTriangle));
        x3 = (int)(x1 + tailleTriangle * Math.cos(angle + angleTriangle));
        y3 = (int)(y1 + tailleTriangle * Math.sin(angle + angleTriangle));

        Polyline triangle = new Polyline(x1, y1, x2, y2, x3, y3);
        return triangle;
    }


}
