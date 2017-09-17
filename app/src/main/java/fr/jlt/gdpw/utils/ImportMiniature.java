package fr.jlt.gdpw.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    /** nbLignes : int */
    private int nbLignes = 0;

    public ImportMiniature(Context ctx, SQLiteDatabase bdd) {
        this.ctx = ctx;
        this.bdd = bdd;
    }

    public void importFile() {
        String ExternalStorageDirectoryPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String listeMiniatures = ExternalStorageDirectoryPath + "/GarageDePocheWanted/les_manquees.csv";
        try {
            FileInputStream fis = new FileInputStream(listeMiniatures);
            String line = null;
            Scanner scanner = new Scanner(fis);
            while (scanner.hasNext()) {
                line = scanner.nextLine();
                nbLignes++;
                importLine(line);
            }
            Toast.makeText(ctx, nbLignes + " fiches importées.", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            Toast.makeText(ctx, " Fichier absent : " + listeMiniatures, Toast.LENGTH_LONG).show();
        }
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
        Miniature miniature = new Miniature();

        int i = 0;

        miniature.setModele(items[i++]);

//Log.d("ImportMiniature", "Collections : " + miniature.getModele());

        miniature.setMarque(items[i++]);
        miniature.setCollection(items[i++]);
        miniature.setPreference(items[i++]);
        miniature.setReference(items[i++]);
        miniature.setPrix(items[i++]);
        miniature.setDateSortie(items[i++].replaceAll("-", "/"));
        miniature.setCarrosserie(items[i++]);
         //miniature.setPhoto(ctx.getResources().getIdentifier(items[i++], "drawable", ctx.getPackageName()));
        miniature.setPhoto("/GarageDePocheWanted/Photos/" + items[i++]);
        miniature.setEditeur(items[i++]);
        miniature.setFabricant(items[i++]);

        //Log.d("ImportMiniature", "Mini : " + miniature.toString());


        MiniatureBDD.add(miniature, bdd);
    }
}
