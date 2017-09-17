package fr.jlt.gdpw.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;
import java.util.Scanner;

import fr.jlt.gdpw.R;
import fr.jlt.gdpw.metier.BddSQLiteHelper;
import fr.jlt.gdpw.metier.MiniatureCste;
import fr.jlt.gdpw.metier.TableCreateCste;
import fr.jlt.gdpw.table.MiniatureBDD;
import fr.jlt.gdpw.utils.ImportMiniature;
import fr.jlt.gdpw.utils.Utils;

/**
 * classe permettant de charger un fichier .csv dans la base SQLite de l'appli
 * Created by jluc1404x on 18/07/15.
 */
public class BddLoadActivity extends Activity {
    private BddSQLiteHelper helper;
    private SQLiteDatabase bdd;

    private TextView fileName = null;

    private static final int REQUEST_ID_WRITE_PERMISSION = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.bdd_chargement);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ///storage/80D0-FD2F/
        if (android.os.Build.VERSION.SDK_INT >= 23) {

            // Check if we have permission
            int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);


            if (permission != PackageManager.PERMISSION_GRANTED) {
                // If don't have permission so prompt the user.
                this.requestPermissions(new String[]{permissionName}, REQUEST_ID_WRITE_PERMISSION);
            }
        }

        String listeMiniatures = Utils.getExternalStorageDirectory(this) + "/GarageDePocheWanted/les_manquees.csv";
        //String listeMiniatures = Environment.getExternalStoragePublicDirectory("") + "/GarageDePocheWanted/les_manquees.csv";
        fileName = (TextView) findViewById(R.id.bddLoadFileName);
        fileName.setText(listeMiniatures);

        helper = new BddSQLiteHelper(this);
        bdd = helper.getReadableDatabase();
        Cursor cursor = MiniatureBDD.getAllList(bdd, MiniatureCste.RUBRIQUE, "ASC");

        RadioGroup radioGroup1 = (RadioGroup) findViewById(R.id.bddLoadRadioGroup1);
        radioGroup1.check(R.id.bddLoadRadio10);
        if (cursor.getCount() == 0) {
            RadioButton radioButton = (RadioButton) findViewById(R.id.bddLoadRadio11);
            radioButton.setEnabled(false);
            radioButton.setTextColor(Color.GRAY);
        }

        Button boutonModifier = (Button) findViewById(R.id.bddLoadBoutonModifier);
        boutonModifier.setEnabled(false);

        Button boutonValider = (Button) findViewById(R.id.bddLoadBoutonValider);
        boutonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup radioGroup1 = (RadioGroup) findViewById(R.id.bddLoadRadioGroup1);
                int id = radioGroup1.getCheckedRadioButtonId();
                if (id == R.id.bddLoadRadio11) {
                    bdd.execSQL("DROP TABLE IF EXISTS " + MiniatureCste.NAME);
                    bdd.execSQL(TableCreateCste.CREATION_TABLE_MINIATURE);

                    Utils.deleteRecursive(new File(Utils.getExternalFilesDir(v.getContext())));
                }

                ChargementAsync chargement = new ChargementAsync(v.getContext());
                chargement.execute(bdd, Utils.getExternalFilesDir(v.getContext()), fileName.getText().toString());

                // déporté dans ChargementAsync
                //Intent intent = new Intent();
                //setResult(RESULT_OK, intent);
                // // On termine cette activité
                //finish();
            }
        });


        Button boutonAnnuler = (Button) findViewById(R.id.bddLoadBoutonAnnuler);
        boutonAnnuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                // On termine cette activité
                finish();
            }

        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Désactive la touche retour
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(RESULT_CANCELED);
            // On termine cette activité
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        bdd.close();
        helper.close();
    }


    // chargement assynchrone
    private class ChargementAsync  extends AsyncTask<Object, Void, String> {
        // Référence faible à l'activité
        private WeakReference<Context> wCtx = null;
        ProgressDialog progressDialog;

        public ChargementAsync (Context aCtx) {
            wCtx = new WeakReference<Context>(aCtx);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(wCtx.get(), "Garage de Poche - Wanted", " Chargement en cours ... ");
        }

        @Override
        protected void onProgressUpdate(Void... values){
            //do nothing
        }

        @Override
        protected String doInBackground(Object... params) {
            SQLiteDatabase bdd = (SQLiteDatabase) params[0];
            String externalFilesDir = (String) params[1];
            String fileName = (String) params[2];

            String resp = null;

            File listeMini = new File(fileName);
            String path = listeMini.getParent() + "/";
            try {
                Boolean ajout = null;
                String nbMax = null;
                int i = 0;
                FileInputStream fis = new FileInputStream(fileName);
                String line = null;
                Scanner scanner = new Scanner(fis);

                int nbLignes = 0;
                int nbAdd = 0;
                int nbMaj = 0;
                while (scanner.hasNext()) {
                    line = scanner.nextLine();
                    String[] items = line.split("\\|", -1);
                    if (!"Modele".equals(items[0].trim())) {
                        nbLignes++;
                        ajout = ImportMiniature.ajoutMiniature(bdd, line, path, externalFilesDir);
                        if (ajout) {
                            nbAdd++;
                        } else {
                            nbMaj++;
                        }
                    }
                }
                resp = nbLignes + " fiches importées.\n  - " + nbAdd + " nouvelles\n  - " + nbMaj + " modifiées";
            } catch (FileNotFoundException e) {
                resp = " Fichier absent : " + fileName;
            }

            return resp;
        }

        @Override
        protected void onPostExecute(String result) {
            File listeMini = new File(fileName.getText().toString());
            String path = listeMini.getParent() + "/";
            try {
                Utils.deleteRecursive(new File(path));
            }
            catch (Exception e) {
                Log.e("GarageDePoche-Wanted", e.getMessage());
            }

            progressDialog.dismiss();

            Toast.makeText(wCtx.get(), result, Toast.LENGTH_LONG).show();

            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            // On termine cette activité
            finish();
        }

    }

}
