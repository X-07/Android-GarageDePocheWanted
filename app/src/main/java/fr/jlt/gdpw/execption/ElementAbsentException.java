package fr.jlt.gdpw.execption;

/**
 * Exception remontée quand l'élément recherché n'existe pas dans la table
 * Created by jluc1404x on 18/07/15.
 */
public class ElementAbsentException extends RuntimeException {
    public ElementAbsentException(String msg) {
        super(msg);
    }
}
