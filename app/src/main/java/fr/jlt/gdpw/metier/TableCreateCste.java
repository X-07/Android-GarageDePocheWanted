package fr.jlt.gdpw.metier;

/**
 * Created by jluc1404x on 18/07/15.
 */
public interface TableCreateCste {

    String RUBRIQUE = "rubrique";

    String CREATION_TABLE_MINIATURE =
            "CREATE TABLE " + MiniatureCste.NAME + " ( " +
                    MiniatureCste.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    MiniatureCste.RUBRIQUE + " TEXT, " +
                    MiniatureCste.CARROSSERIE + " TEXT, " +
                    MiniatureCste.COLLECTION + " TEXT, " +
                    MiniatureCste.DATESORTIE + " TEXT, " +
                    MiniatureCste.EDITEUR + " TEXT, " +
                    MiniatureCste.FABRICANT + " TEXT, " +
                    MiniatureCste.MARQUE + " TEXT, " +
                    MiniatureCste.PHOTO + " TEXT, " +
                    MiniatureCste.PREFERENCE + " TEXT, " +
                    MiniatureCste.REFERENCE + " TEXT, " +
                    MiniatureCste.PRIX + " TEXT, " +
                    MiniatureCste.TROUVE + " TEXT " +
                    ")";


}
