package twisk;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import twisk.exceptions.TwiskIOException;
import twisk.mondeIG.MondeIG;
import twisk.outils.RessourcesLoader;
import twisk.outils.TailleComposants;
import twisk.outils.ThreadsManager;
import twisk.vues.VueMenu;
import twisk.vues.VueMondeIG;
import twisk.vues.VueOutils;

public class MainTwisk extends Application {

    private static Stage primaryStage;

    private MondeIG monde;

    /**
     * Fonction start
     */
    @Override
    public void start(Stage primaryStage) throws Exception{

        MainTwisk.primaryStage = primaryStage;
        primaryStage.setTitle("Twisk");
        Image icon = new Image(getClass().getResourceAsStream("/images/icon.png"), 128, 128, true, false);
        primaryStage.getIcons().add(icon);
        primaryStage.setResizable(false);

        BorderPane root = new BorderPane();

        monde = new MondeIG();
        try {
            RessourcesLoader.getInstance().extraireMondesPreExistants();
        }catch(TwiskIOException e){
            e.afficherAlerte();
        }

        root.setBottom(new VueOutils(monde));
        root.setCenter(new VueMondeIG(monde));
        root.setTop(new VueMenu(monde));
        
        TailleComposants tailles = TailleComposants.getInstance();
        Scene scene = new Scene(root, tailles.getLargeurFenetre(), tailles.getHauteurFenetre());
        scene.getStylesheets().add(getClass().getResource("/style_principal.css").toString());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Fonction stop
     */
    @Override
    public void stop(){
        if(monde.enSimulation())
            ThreadsManager.getInstance().detruireTout();
    }

    /**
     * Fonction main
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Change le nom de la fenêtre principale
     */
    public static void setTitreStage(String titre){
        primaryStage.setTitle(titre);
    }
}
