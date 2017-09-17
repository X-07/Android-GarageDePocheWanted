package fr.jlt.gdpw.utils;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

import fr.jlt.gdpw.donneesDAO.Miniature;
import fr.jlt.gdpw.metier.MiniatureCste;
import fr.jlt.gdpw.table.MiniatureBDD;

/**
 * Created by jluc1404x on 18/07/15.
 */
public class ImportMiniature {
    /** ctx */
    private Context ctx = null;
    /** bdd */
    private SQLiteDatabase bdd = null;

    /** items */
    private String[] items = null;
    /** nom : String */
    private String nom = null;
    /** nbLignes : int */
    private int nbLignes = 0;
    /** nbAdd : int */
    private int nbAdd = 0;
    /** nbMaj : int */
    private int nbMaj = 0;
    /** path : int */
    private String path = null;

    public ImportMiniature(Context ctx, SQLiteDatabase bdd) {
        this.ctx = ctx;
        this.bdd = bdd;
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
                nbLignes++;
                importLine(line);
            }
            Toast.makeText(ctx, nbLignes + " fiches importées.\n  - " + nbAdd + " nouvelles\n  - " + nbMaj + " modifiées", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            Toast.makeText(ctx, " Fichier absent : " + listeMiniatures, Toast.LENGTH_LONG).show();
        }
        listeMini.delete();
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

        String trouve;
        try {
            Miniature miniatureOld = MiniatureBDD.get(miniature.getModele(), bdd);
            trouve = miniatureOld.getTrouve();
            MiniatureBDD.delete(miniature.getModele(), bdd);
            nbMaj++;
        }
        catch (Exception e) {
            trouve = "0";
            nbAdd++;
        }

        miniature.setMarque(items[i++]);
        miniature.setCollection(items[i++]);
        miniature.setPreference(items[i++]);
        miniature.setReference(items[i++]);
        miniature.setPrix(items[i++]);
        miniature.setDateSortie(items[i++].replaceAll("-", "/"));
        miniature.setCarrosserie(items[i++]);
        //miniature.setPhoto(ctx.getResources().getIdentifier(items[i++], "drawable", ctx.getPackageName()));
        miniature.setPhoto(items[i++]);
        if (!"/".equals(miniature.getPhoto().substring(0, 1)))  {
            miniature.setPhoto("/" + miniature.getPhoto());
        }
        miniature.setEditeur(items[i++]);
        miniature.setFabricant(items[i++]);
        miniature.setTrouve(trouve);
//Log.d("ImportMiniature", "Mini : " + miniature.toString());

        MiniatureBDD.add(miniature, bdd);

        //Déplace la photo dans l'espace privée de l'appli.
        File fileSrc = new File(path + miniature.getPhoto());
        File fileDest = new File(Utils.getExternalFilesDir(ctx), miniature.getPhoto());
        Utils.makeDirs(fileDest);
        Utils.copyFile(fileSrc, fileDest);
        fileSrc.delete();

        //crée une miniature et la met en cache
        File fileCacheDest = new File(Utils.getExternalCacheDir(ctx), miniature.getPhoto());
        Utils.makeDirs(fileCacheDest);
        Bitmap bmp = Utils.decodeSampledBitmapFromUri(fileDest.getAbsolutePath(), 200, 150);
        Utils.StoreImage(bmp, fileCacheDest);

    }
}
