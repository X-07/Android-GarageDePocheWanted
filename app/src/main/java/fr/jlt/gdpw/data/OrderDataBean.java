package fr.jlt.gdpw.data;

/**
 * classe représentant le critère de trie à effectuer sur la liste
 * Created by jluc1404x on 18/07/15.
 */
public class OrderDataBean {

    private int id;

    private String rubrique;

    private String libelle;

    public OrderDataBean(int id, String rubrique, String libelle) {
        this.id = id;
        this.rubrique = rubrique;
        this.libelle = libelle;
    }

    public int getId() {
        return id;
    }

    public String getRubrique() {
        return rubrique;
    }

    public String getLibelle() {
        return libelle;
    }
}
