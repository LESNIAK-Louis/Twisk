package twisk.exceptions;

public class ParametresIncorrectsException extends TwiskException{

    /**
     * Constructeur ParametresIncorrectsException
     */
    public ParametresIncorrectsException(String msg){

        super("Paramètres incorrects : " + msg);
    }
}
