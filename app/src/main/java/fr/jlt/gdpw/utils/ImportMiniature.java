package fr.jlt.gdpw.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.io.InputStream;
import java.util.Scanner;
import java.util.StringTokenizer;

import fr.jlt.gdpw.R;
import fr.jlt.gdpw.donneesDAO.Miniature;
import fr.jlt.gdpw.table.MiniatureBDD;

/**
 * Created by jluc1404x on 18/07/15.
 */
public class ImportMiniature {
    private Context ctx = null;
    private SQLiteDatabase bdd = null;

    /** items */
    private String[] items = null;
    /** nom : String */
    private String nom = null;
    /** nbItems : int */
    private int nbItems = 0;
    /** nbLignes : int */
    private int nbLignes = 0;
    /** nbTotalLignes : int */
    private int nbTotalLignes = 0;

    public ImportMiniature(Context ctx, SQLiteDatabase bdd) {
        this.ctx = ctx;
        this.bdd = bdd;
    }

    public void importFile() {
        InputStream inputStream = ctx.getResources().openRawResource(R.raw.les_manquees);
        Scanner scanner = null;
        String line = null;
        scanner = new Scanner(inputStream);
        while (scanner.hasNext()) {
            line = scanner.nextLine();
            nbLignes++;
            nbTotalLignes++;
            importLine(line);
        }
        Toast.makeText(ctx, nbLignes + " fiches importées.", Toast.LENGTH_LONG).show();
    }

    /**
     * Convertir une ligne du fichier en enregistrement dans la table
     * @param line String
     */
    private void importLine(final String line) {
        //items = line.split("\\|");
        items = line.split("\\|", -1);    //TO DO vérifier le -1 !
        if ("Modele".equals(items[0].trim())) {
            // on saute la ligne d'entête
            nbLignes--;
        } else {
            ajoutMiniature();
        }
    }




    /**
     * Convertir une ligne du fichier en enregistrement dans la table
     * @return Table
     */
    private void ajoutMiniature() {
        StringTokenizer st = null;
        Miniature miniature = new Miniature();

        miniature.setModele(items[0]);

//Log.d("ImportMiniature", "Collections : " + miniature.getModele());

        miniature.setMarque(items[1]);
        miniature.setCollection(items[2]);
        miniature.setPreference(items[3]);
        miniature.setReference(items[4]);
        miniature.setPrix(items[5]);
        miniature.setDateSortie(items[6].replaceAll("-", "/"));
        miniature.setCarrosserie(items[7]);
        miniature.setPhotoSmall(ctx.getResources().getIdentifier(items[8], "drawable", ctx.getPackageName()));
        miniature.setPhoto(ctx.getResources().getIdentifier(items[9], "drawable", ctx.getPackageName()));
        miniature.setEditeur(items[10]);
        miniature.setFabricant(items[11]);

        MiniatureBDD.add(miniature, bdd);
    }
}
