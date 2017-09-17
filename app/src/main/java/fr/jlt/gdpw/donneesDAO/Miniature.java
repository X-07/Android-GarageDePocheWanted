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
    /** photoSmall */
    private Integer photoSmall = null;
    /** photo */
    private Integer photo = null;
    /** preference */
    private String preference = null;
    /** numRef */
    private String reference = null;
    /** prix */
    private String prix = null;


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

    public Integer getPhotoSmall() {
        return photoSmall;
    }

    public void setPhotoSmall(Integer photoSmall) {
        this.photoSmall = photoSmall;
    }

    public Integer getPhoto() {
        return photo;
    }

    public void setPhoto(Integer photo) {
        this.photo = photo;
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
}
