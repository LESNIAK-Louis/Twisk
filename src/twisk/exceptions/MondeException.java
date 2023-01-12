package twisk.exceptions;
public class MondeException extends TwiskException {
    /**
     * Constructeur MondeException
     */
    public MondeException(String msg){ super("Le monde n'est pas valide : " + msg ); }
}
