package twisk.exceptions;

public class ArcNonValideException extends TwiskException {

    /**
     * Constructeur ArcNonValideException
     */
    public ArcNonValideException(String msg){

        super("Arc non valide : " + msg);
    }
}
