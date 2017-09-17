package fr.jlt.gdpw.donneesDAO;

/**
 * Created by jluc1404x on 18/07/15.
 */
public class Miniature {
    /** id */
    private Integer id = 0;
    /** modèle */
    private String modele = null;
    /** carrosserie */
    private String carrosserie = null;
    /** collection */
    private String collection = null;
    /** dateSortie */
    private String dateSortie = null;
    /** editeur */
    private String editeur = null;
    /** fabricant */
    private String fabricant = null;
    /** marque */
    private String marque = null;
    /** photo */
    //private Integer photo = null;
    private String photo = null;
    /** preference */
    private String preference = null;
    /** numRef */
    private String reference = null;
    /** prix */
    private String prix = null;
    /** trouve */
    private String trouve = null;

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("id : ").append(id).append("\n");
        sb.append("modele : ").append(modele).append("\n");
        sb.append("carrosserie : ").append(carrosserie).append("\n");
        sb.append("collection : ").append(collection).append("\n");
        sb.append("dateSortie : ").append(dateSortie).append("\n");
        sb.append("editeur : ").append(editeur).append("\n");
        sb.append("fabricant : ").append(fabricant).append("\n");
        sb.append("marque : ").append(marque).append("\n");
        sb.append("photo : ").append(photo).append("\n");
        sb.append("preference : ").append(preference).append("\n");
        sb.append("reference : ").append(reference).append("\n");
        sb.append("prix : ").append(prix).append("\n");
        sb.append("trouve : ").append(trouve).append("\n");
        return sb.toString();
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public String getCarrosserie() {
        return carrosserie;
    }

    public void setCarrosserie(String carrosserie) {
        this.carrosserie = carrosserie;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getDateSortie() {
        return dateSortie;
    }

    public void setDateSortie(String dateSortie) {
        this.dateSortie = dateSortie;
    }

    public String getEditeur() {
        return editeur;
    }

    public void setEditeur(String editeur) {
        this.editeur = editeur;
    }

    public String getFabricant() {
        return fabricant;
    }

    public void setFabricant(String fabricant) {
        this.fabricant = fabricant;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getPreference() {
        return preference;
    }

    public void setPreference(String preference) {
        this.preference = preference;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTrouve() {
        return trouve;
    }

    public void setTrouve(String trouve) {
        this.trouve = trouve;
    }

}
