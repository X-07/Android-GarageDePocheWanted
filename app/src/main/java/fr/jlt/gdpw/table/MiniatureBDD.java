package fr.jlt.gdpw.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import fr.jlt.gdpw.donneesDAO.Miniature;
import fr.jlt.gdpw.execption.ElementAbsentException;
import fr.jlt.gdpw.metier.MiniatureCste;


/**
 * Created by jluc1404x on 18/07/15.
 */
public class MiniatureBDD {

//    public static String TAB_NAME = MiniatureCste.NAME;

    private static StringBuffer select = new StringBuffer()
            .append("SELECT ")
            .append(MiniatureCste.ID).append(", ")
            .append(MiniatureCste.RUBRIQUE).append(", ")
            .append(MiniatureCste.CARROSSERIE).append(", ")
            .append(MiniatureCste.COLLECTION).append(", ")
            .append(MiniatureCste.DATESORTIE).append(", ")
            .append(MiniatureCste.EDITEUR).append(", ")
            .append(MiniatureCste.FABRICANT).append(", ")
            .append(MiniatureCste.MARQUE).append(", ")
            .append(MiniatureCste.PHOTO_SMALL).append(", ")
            .append(MiniatureCste.PHOTO).append(", ")
            .append(MiniatureCste.PREFERENCE).append(", ")
            .append(MiniatureCste.REFERENCE).append(", ")
            .append(MiniatureCste.PRIX).append(" ")
            .append("FROM ").append(MiniatureCste.NAME).append(" ") ;


    private static StringBuffer selectListRub = new StringBuffer()
            .append("SELECT ")
            .append(MiniatureCste.ID).append(", ")
            .append(MiniatureCste.RUBRIQUE).append(", ")
            .append(MiniatureCste.DATESORTIE).append(", ")
            .append(MiniatureCste.MARQUE).append(", ")
            .append(MiniatureCste.PREFERENCE).append(", ")
            .append(MiniatureCste.PHOTO_SMALL).append(", ")
            .append(MiniatureCste.PHOTO).append(", ")
            .append(MiniatureCste.REFERENCE).append(", ")
            .append(MiniatureCste.PRIX).append(" ");

    private static StringBuffer selectListTab = new StringBuffer()
            .append("FROM ").append(MiniatureCste.NAME).append(" ");


    private static StringBuffer whereID = new StringBuffer()
            .append(" WHERE ").append(MiniatureCste.NAME).append(".").append(MiniatureCste.ID).append(" = ? ");

    private static StringBuffer whereRUB = new StringBuffer()
            .append(" WHERE ").append(MiniatureCste.NAME).append(".").append(MiniatureCste.RUBRIQUE).append(" = ? ");


    private MiniatureBDD() {
    }

    /**
     * @param miniature le métier à ajouter à la base
     */
    public static int add(Miniature miniature, SQLiteDatabase bdd) {
        ContentValues values = new ContentValues();
        values.put(MiniatureCste.RUBRIQUE, miniature.getModele());
        values.put(MiniatureCste.CARROSSERIE, miniature.getCarrosserie());
        values.put(MiniatureCste.COLLECTION, miniature.getCollection());
        values.put(MiniatureCste.DATESORTIE, miniature.getDateSortie());
        values.put(MiniatureCste.EDITEUR, miniature.getEditeur());
        values.put(MiniatureCste.FABRICANT, miniature.getFabricant());
        values.put(MiniatureCste.MARQUE, miniature.getMarque());
        values.put(MiniatureCste.PHOTO_SMALL, miniature.getPhotoSmall());
        values.put(MiniatureCste.PHOTO, miniature.getPhoto());
        values.put(MiniatureCste.PREFERENCE, miniature.getPreference());
        values.put(MiniatureCste.REFERENCE, miniature.getReference());
        values.put(MiniatureCste.PRIX, miniature.getPrix());

        long res = bdd.insert(MiniatureCste.NAME, null, values);
        return (int) res;
    }

    /**
     * @param id : identifiant de l'enregistrement à retrouver
     */
    public static Miniature get(final long id, SQLiteDatabase bdd) {
        String[] args = {Long.toString(id)};
        String statement = new StringBuffer().append(select).append(whereID).toString();
//Log.d("statement_GetInt", statement);
        Cursor cursor = bdd.rawQuery(statement, args);

        Miniature table = null;
        try {
            if (cursor.moveToFirst() && !cursor.isNull(MiniatureCste.COL_ID)) {
                table = getMiniatureFromCursor(cursor);
                return table;
            } else {
                throw new ElementAbsentException("NOT FOUND");
            }
        } finally {
            cursor.close();
        }
    }

    /**
     * @param modele : valeur de l'enregistrement dont on veut l'identifiant
     */
    public static Miniature get(String modele, SQLiteDatabase bdd) {
        String[] args = {modele};
        String statement = new StringBuffer().append(select).append(whereRUB).toString();
//Log.d("statement_GetString", statement);
        Cursor cursor = bdd.rawQuery(statement, args);

        Miniature table = null;
        try {
            if (cursor.moveToFirst() && !cursor.isNull(MiniatureCste.COL_ID)) {
                table = getMiniatureFromCursor(cursor);
                return table;
            } else {
                throw new ElementAbsentException("NOT FOUND");
            }
        } finally {
            cursor.close();
        }
    }

    /**
     * @param cursor
     * @return
     */
    private static Miniature getMiniatureFromCursor(Cursor cursor) {
        Miniature table = new Miniature();
        table.setId(cursor.getInt(MiniatureCste.COL_ID));
        table.setModele(cursor.getString(MiniatureCste.COL_RUBRIQUE));
        table.setCarrosserie(cursor.getString(MiniatureCste.COL_CARROSSERIE));
        table.setCollection(cursor.getString(MiniatureCste.COL_COLLECTION));
        table.setDateSortie(cursor.getString(MiniatureCste.COL_DATESORTIE));
        table.setEditeur(cursor.getString(MiniatureCste.COL_EDITEUR));
        table.setFabricant(cursor.getString(MiniatureCste.COL_FABRICANT));
        table.setMarque(cursor.getString(MiniatureCste.COL_MARQUE));
        table.setPhotoSmall(cursor.getString(MiniatureCste.COL_PHOTO_SMALL));
        table.setPhoto(cursor.getString(MiniatureCste.COL_PHOTO));
        table.setPreference(cursor.getString(MiniatureCste.COL_PREFERENCE));
        table.setReference(cursor.getString(MiniatureCste.COL_REFERENCE));
        table.setPrix(cursor.getString(MiniatureCste.COL_PRIX));

        return table;
    }

    /**
     *
     * @param bdd
     * @param ordre
     * @param sens
     * @return
     */
    public static Cursor getAllList(SQLiteDatabase bdd, String ordre, String sens) {
        StringBuffer selectListOrdered = new StringBuffer().append(selectListRub);
        if (!(MiniatureCste.RUBRIQUE.equals(ordre) || MiniatureCste.DATESORTIE.equals(ordre) || MiniatureCste.MARQUE.equals(ordre) || MiniatureCste.PREFERENCE.equals(ordre) || MiniatureCste.PRIX.equals(ordre) || MiniatureCste.REFERENCE.equals(ordre))) {
            // toutes ces rubriques sont dans la table Miniature il suffit d'ajouter la rubrique du tri à la liste des colonnes
            selectListOrdered.append(", ").append(ordre).append(" ");
        }
        selectListOrdered.append(selectListTab);

        selectListOrdered.append("ORDER BY ").append(ordre).append(" ").append(sens);
        if (!MiniatureCste.RUBRIQUE.equals(ordre)) {
            // on ajoute le critère du tri secondaire si nécessaire
            selectListOrdered.append(", ").append(MiniatureCste.RUBRIQUE).append(" ").append(sens);
        }
//Log.d("statement_AllList", selectListOrdered.toString());
        Cursor cursor = bdd.rawQuery(selectListOrdered.toString(), null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

}
