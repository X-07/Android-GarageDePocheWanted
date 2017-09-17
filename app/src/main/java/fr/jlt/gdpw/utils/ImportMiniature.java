package fr.jlt.gdpw.utils;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import fr.jlt.gdpw.donneesDAO.Miniature;
import fr.jlt.gdpw.execption.ModeleObligatoireException;
import fr.jlt.gdpw.execption.RubriquesObligatoiresException;
import fr.jlt.gdpw.table.MiniatureBDD;

/**
 * classe permettant d'importer un fichier .csv dans la base SQLite
 * Created by jluc1404x on 18/07/15.
 */
public class ImportMiniature {
    /** bdd */
    private SQLiteDatabase bdd = null;

    /** externalFilesDir */
    private String externalFilesDir = null;
    /** path : int */
    private String path = null;

    public ImportMiniature(SQLiteDatabase bdd, String externalFilesDir) {
        this.bdd = bdd;
        this.externalFilesDir = externalFilesDir;
    }

    public void importFile(String listeMiniatures) {
        File listeMini = new File(listeMiniatures);
        path = listeMini.getParent() + "/";
        try {
            FileInputStream fis = new FileInputStream(listeMiniatures);
            String line = null;
            Scanner scanner = new Scanner(fis);
            while (scanner.hasNext()) {
                line = scanner.nextLine();
                importLine(bdd, line, path, externalFilesDir);
            }
        } catch (FileNotFoundException e) {
            Log.d("ImportMiniature", " Fichier absent : " + listeMiniatures);
        }
    }

    /**
     * Convertir une ligne du fichier en enregistrement dans la table
     * @param line String
     */
    public static Boolean importLine(final SQLiteDatabase bdd, final String line, final String path, final String externalFilesDir) {
        Boolean ajout = null;
        //items = line.split("\\|");
        String[] items = line.split("\\|", -1);    //TO DO vérifier le -1 !
        if (!"Modele".equals(items[0].trim())) {
            ajout = ajoutMiniature(bdd, line, path, externalFilesDir, 0);
        }
        return ajout;
    }




    /**
     * Convertir une ligne du fichier en enregistrement dans la table
     * @return Table
     */
    public static Boolean ajoutMiniature(final SQLiteDatabase bdd, final String line, final String path, final String externalFilesDir, final int doublon) {
        Boolean ajout = null;
        String[] items = line.split("\\|", -1);
        if (items.length < 3) {
            throw new RubriquesObligatoiresException();
        }

        Miniature miniature = new Miniature();

        int i = 0;

        miniature.setModele(items[i++]);
        if (Utils.isEmptyOrNullOrBlank(miniature.getModele())) {
            throw new ModeleObligatoireException();
        }

        String trouve;
        try {
            Miniature miniatureOld = MiniatureBDD.get(miniature.getModele(), bdd);
            trouve = miniatureOld.getTrouve();
            switch(doublon) {
                case 0:
                    // remplacer la fiche existante
                    //Log.d("ImportMiniature", "doublon remplacé : " + miniature.getModele());
                    MiniatureBDD.delete(miniature.getModele(), bdd);
                    ajout = false;
                    break;
                case 1:
                    // garder la fiche existante
                    //Log.d("ImportMiniature", "doublon conservé : " + miniature.getModele());
                    ajout = null;
                    break;
                case 2:
                    // creer un doublon
                    //Log.d("ImportMiniature", "doublon créé : " + miniature.getModele());
                    ajout = true;
                    break;
            }
        }
        catch (Exception e) {
            //Log.d("ImportMiniature", "ajout : " + miniature.getModele());
            trouve = "0";
            ajout = true;
        }

        if (ajout != null) {
            try {
                miniature.setMarque(items[i++]);
                miniature.setPreference(items[i++]);
                //miniature.setPhoto(ctx.getResources().getIdentifier(items[i++], "drawable", ctx.getPackageName()));
                miniature.setPhoto(items[i++]);
                if (!"/".equals(miniature.getPhoto().substring(0, 1))) {
                    miniature.setPhoto("/" + miniature.getPhoto());
                }
                miniature.setCollection(items[i++]);
                miniature.setReference(items[i++]);
                miniature.setPrix(items[i++]);
                miniature.setDateSortie(items[i++].replaceAll("-", "/"));
                miniature.setCarrosserie(items[i++]);
                miniature.setEditeur(items[i++]);
                miniature.setFabricant(items[i++]);
//Log.d("ImportMiniature", "Mini : " + miniature.toString());
            }
            catch (IndexOutOfBoundsException e) {
                // ne rien faire
            }

            if (miniature.getPreference() == null) {
                miniature.setPreference("0");
            }
            miniature.setTrouve(trouve);

            MiniatureBDD.add(miniature, bdd);

            if (Utils.isNotEmptyOrNull(miniature.getPhoto())) {
                //Déplace la photo dans l'espace privée de l'appli.
                File fileSrc = new File(path + miniature.getPhoto());
                File fileDest = new File(externalFilesDir, miniature.getPhoto());
                Utils.makeDirs(fileDest, true);
                Utils.copyFile(fileSrc, fileDest);
                fileSrc.delete();

                //crée une miniature et la met en cache
                File fileCacheDest = new File(externalFilesDir + "/cached", miniature.getPhoto());
                Utils.makeDirs(fileCacheDest, true);
                Bitmap bmp = Utils.decodeSampledBitmapFromUri(fileDest.getAbsolutePath(), 200, 150);
                Utils.StoreImage(bmp, fileCacheDest);
            }
        }

        return ajout;
    }
}
