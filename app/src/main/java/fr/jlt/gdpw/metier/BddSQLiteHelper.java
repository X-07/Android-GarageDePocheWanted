package fr.jlt.gdpw.metier;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * classe permettant de dérer la base SQLite
 * Created by jluc1404x on 18/07/15.
 */
public class BddSQLiteHelper extends SQLiteOpenHelper {
    // Nous sommes à la première version de la base
    // Si je décide de la mettre à jour, il faudra changer cet attribut
    private final static int DB_VERSION = 1;
    // Le nom du fichier qui représente ma base
    private final static String DB_NAME = "GarageDePoche";

    private Context ctx = null;

    //Constructeur
    public BddSQLiteHelper(Context ctx) {
        super(ctx, DB_NAME, null, DB_VERSION);
        this.ctx = ctx;
    }

    @Override
    public void onCreate(SQLiteDatabase bdd) {
        // commande SQL pour Créer la table:TableExemple(col1,col2,col3,col4)
        bdd.execSQL(TableCreateCste.CREATION_TABLE_MINIATURE);

 //       ImportMiniature importMiniature = new ImportMiniature(ctx, bdd);
 //       importMiniature.importFile();
    }

    @Override
    public void onUpgrade(SQLiteDatabase bdd, int oldVersion, int newVersion) {
        // supprimer des tables si elles existent
        bdd.execSQL("DROP TABLE IF EXISTS " + MiniatureCste.NAME);
        // creation des nouvelles tables vides
        this.onCreate(bdd);
    }
}
