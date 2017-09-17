package fr.jlt.gdpw.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.provider.DocumentFile;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;
import java.util.Scanner;

import fr.jlt.gdpw.R;
import fr.jlt.gdpw.constantes.ActivityCste;
import fr.jlt.gdpw.execption.ModeleObligatoireException;
import fr.jlt.gdpw.execption.RubriquesObligatoiresException;
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
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private TextView fileName = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        setContentView(R.layout.bdd_chargement);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        CheckBox checkBox = (CheckBox) findViewById(R.id.bddLoadCheckBox);
        // Utils.getExternalStorageDirectory(this) = "/storage/80D0-FD2F/"
        if (Utils.getExternalStorageDirectory(this).startsWith("/storage/emulated/")) {
            editor.putString("SDCard", "false");
            editor.apply();
            checkBox.setVisibility(View.INVISIBLE);
        }
        else {
            // With Android Level >= 23, you have to ask the user
            // for permission with device (For example read/write data on the device).
            if (android.os.Build.VERSION.SDK_INT >= 23) {
                checkBox.setVisibility(View.VISIBLE);
            }
            else {
                checkBox.setVisibility(View.INVISIBLE);
            }
        }

        gererAuthorisation();

        String listeMiniatures = Utils.getExternalStorageDirectory(this) + "/GarageDePocheWanted/les_manquees.csv";
        //String listeMiniatures = Environment.getExternalStoragePublicDirectory("") + "/GarageDePocheWanted/les_manquees.csv";
        fileName = (TextView) findViewById(R.id.bddLoadFileName);
        fileName.setText(listeMiniatures);

        helper = new BddSQLiteHelper(this);
        bdd = helper.getReadableDatabase();
        Cursor cursor = MiniatureBDD.getAllList(bdd, MiniatureCste.RUBRIQUE, "ASC");

        CheckBox checkBoxPurge = (CheckBox) findViewById(R.id.bddLoadCheckBoxPurge);
        if (cursor.getCount() == 0) {
            checkBoxPurge.setEnabled(false);
            checkBoxPurge.setTextColor(Color.GRAY);
        }

        Button boutonModifier = (Button) findViewById(R.id.bddLoadBoutonModifier);
        boutonModifier.setEnabled(false);


        Button boutonValider = (Button) findViewById(R.id.bddLoadBoutonValider);
        boutonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean purgeBdd = false;
                int doublon = 0;
                CheckBox checkBoxPurge = (CheckBox) findViewById(R.id.bddLoadCheckBoxPurge);
                if (checkBoxPurge.isChecked()) {
                    purgeBdd = true;
                }

                RadioGroup radioGroup2 = (RadioGroup) findViewById(R.id.bddLoadRadioGroup2);
                switch(radioGroup2.getCheckedRadioButtonId()) {
                    case R.id.bddLoadRadio20:
                        doublon = 0;
                        break;
                    case R.id.bddLoadRadio21:
                        doublon = 1;
                        break;
                    case R.id.bddLoadRadio22:
                        doublon = 2;
                        break;
                }

                ChargementAsync chargement = new ChargementAsync(v.getContext(), purgeBdd, doublon);
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

    public void handleDirSDCardClick(View v) {
        CheckBox checkBox = (CheckBox) findViewById(R.id.bddLoadCheckBox);
        if (checkBox.isChecked()) {
            checkBox.setChecked(false);
            editor.putString("SDCard", "true");
            editor.apply();
            gererAuthorisationSDCard();
        }
    }

    private void gererAuthorisation() {
        // With Android Level >= 23, you have to ask the user
        // for permission with device (For example read/write data on the device).
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            boolean needReadAccess = false;
            boolean needWriteAccess = false;
            // lecture
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                needReadAccess = true;
            }
            // écriture
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                needWriteAccess = true;
            }
            if (needReadAccess && needWriteAccess) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, ActivityCste.REQUEST_READ_WRITE_PERMISSION);
            }
            else if (needReadAccess) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, ActivityCste.REQUEST_READ_PERMISSION);
            }
            else if (needWriteAccess) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, ActivityCste.REQUEST_WRITE_PERMISSION);
            }
            // carte SD
            String prefSDCard = preferences.getString("SDCard", "true");
            if ("true".equals(prefSDCard)) {
                String uriString = preferences.getString("TREE_URI", null);
                if (uriString == null) {
                    gererAuthorisationSDCard();
                    return;
                }
                Uri treeUri = Uri.parse(uriString);
                if (treeUri == null) {
                    gererAuthorisationSDCard();
                    return;
                }

                DocumentFile documentFile = DocumentFile.fromTreeUri(this, treeUri);
                if (documentFile == null) {
                    gererAuthorisationSDCard();
                    return;
                }
                if (!documentFile.canWrite()) {
                    gererAuthorisationSDCard();
                    return;
                }
            }
        }
    }

    private void gererAuthorisationSDCard() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("SD Card location");
        builder.setMessage("Sélectionner le répertoire de votre SD Card ou se trouve les fichiers qui seront importés dans l'application.\n\n" +
                "Cette autorisation est utilisée pour nettoyer automatique l'espace occupé sur la SD Card après l'import.\n\n");
        builder.setPositiveButton("Accepter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                editor.putString("SDCard", "true");
                editor.apply();
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                startActivityForResult(intent, ActivityCste.REQUEST_SDCARD_DIR);
            }
        });
        builder.setNegativeButton("Refuser", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editor.putString("SDCard", "false");
                editor.apply();
            }
        });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                editor.putString("SDCard", "false");
                editor.apply();
            }
        });
        builder.show();

    }


    // When you have the request results
    @Override
    public final void onActivityResult(final int requestCode, final int resultCode, final Intent resultData) {
        if(resultCode == RESULT_OK && requestCode == ActivityCste.REQUEST_SDCARD_DIR){
            Uri treeUri = null;
            // Get Uri from Storage Access Framework.
            treeUri = resultData.getData();

            // Persist URI in shared preference so that you can use it later.
            // Use your own framework here instead of PreferenceUtil.
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            Editor editor = preferences.edit();
            if (treeUri == null) {
                editor.putString("TREE_URI", null);
            }
            else {
                editor.putString("TREE_URI", treeUri.toString());
            }
            editor.apply();

            // Persist access permissions.
            grantUriPermission(getPackageName(), treeUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //final int takeFlags = resultData.getFlags() & (Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //getContentResolver().takePersistableUriPermission(treeUri, takeFlags);
            getContentResolver().takePersistableUriPermission(treeUri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
    }

    // When you have the request results
    // Utile pour le cas ou la permission n'est pas accordée, on peut désactiver le code concerné par cette permission
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //
        // Note: If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0) {
            switch (requestCode) {
                case ActivityCste.REQUEST_READ_WRITE_PERMISSION:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) { //Read access

                    }
                    if (grantResults[1] == PackageManager.PERMISSION_GRANTED) { //Write access

                    }
                    break;
                case ActivityCste.REQUEST_READ_PERMISSION:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    }
                    break;
                case ActivityCste.REQUEST_WRITE_PERMISSION:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    }
                    break;
            }
        } else {
            Toast.makeText(getApplicationContext(), "Permission Cancelled!", Toast.LENGTH_SHORT).show();
        }
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
    private class ChargementAsync  extends AsyncTask<Object, String, String> {
        // Référence faible à l'activité
        private WeakReference<Context> wCtx = null;
        private boolean purgeBdd;
        private int doublon = 0;
        ProgressDialog progressDialog;

        public ChargementAsync (Context aCtx, boolean razBDD, int doublon) {
            wCtx = new WeakReference<Context>(aCtx);
            this.purgeBdd = razBDD;
            this.doublon = doublon;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(wCtx.get(), "Garage de Poche - Wanted", " Chargement en cours ... ");
        }

        @Override
        protected void onProgressUpdate(String... values){
            progressDialog.setMessage(values[0]);
        }

        @Override
        protected String doInBackground(Object... params) {
            SQLiteDatabase bdd = (SQLiteDatabase) params[0];
            String externalFilesDir = (String) params[1];
            String fileName = (String) params[2];

            if (purgeBdd) {
                publishProgress("  Purge de la base en cours ...");

                bdd.execSQL("DROP TABLE IF EXISTS " + MiniatureCste.NAME);
                bdd.execSQL(TableCreateCste.CREATION_TABLE_MINIATURE);

                Utils.deleteRecursive(new File(Utils.getExternalFilesDir(wCtx.get())));
            }

            String resp = null;

            File listeMini = new File(fileName);
            String path = listeMini.getParent() + "/";
            int i = 0;
            try {
                Boolean ajout = null;
                String nbMax = null;
                FileInputStream fis = new FileInputStream(fileName);
                String line = null;
                Scanner scanner = new Scanner(fis);

                int nbLignes = 0;
                int nbAdd = 0;
                int nbMaj = 0;
                while (scanner.hasNext()) {
                    line = scanner.nextLine();
                    String[] items = line.split("\\|", -1);
                    if (!"Modele *".equals(items[0].trim())) {
                        nbLignes++;
                        ajout = ImportMiniature.ajoutMiniature(bdd, line, path, externalFilesDir, doublon);
                        if (ajout != null) {
                            if (ajout) {
                                nbAdd++;
                            } else {
                                nbMaj++;
                            }
                        }
                        publishProgress(" Import fiche N° " + ++i);
                    }
                }

                StringBuffer sb = new StringBuffer();
                sb.append(nbLignes).append(" fiches importées.");
                if (nbAdd > 0) {
                    sb.append("\n  - ").append(nbAdd).append(" ajoutées.");
                }
                if (nbMaj > 0) {
                    sb.append("\n  - ").append(nbMaj).append(" remplacées.");
                }
                if (nbLignes - nbAdd - nbMaj > 0) {
                    sb.append("\n  - ").append(nbLignes - nbAdd - nbMaj).append(" inchangées.");
                }
                resp = sb.toString();
            } catch (FileNotFoundException e) {
                resp = " Fichier absent : " + fileName;
            } catch (ModeleObligatoireException e) {
                resp = " Fiche N° " + i + " : modèle non renseigné.\n\t\t\t Import stoppé.";
            } catch (RubriquesObligatoiresException e) {
                resp = " Fiche N° " + i + " : au moins 1 champ absent.\n  - Modèle\n  - Marque\n  - Préférence\n\t\t\t Import stoppé.";
            }

            publishProgress(" Suppression des fichiers inutiles ...");
            Utils.deleteAllFiles(new File(path), wCtx.get());
            publishProgress(" Import terminé");

            return resp;
        }

        @Override
        protected void onPostExecute(String result) {
//            File listeMini = new File(fileName.getText().toString());
//            String path = listeMini.getParent() + "/";
//            try {
//                Utils.deleteRecursive(new File(path), wCtx.get());
//            }
//            catch (Exception e) {
//                Log.e("GarageDePoche-Wanted", e.getMessage());
//            }

            progressDialog.dismiss();

            Toast.makeText(wCtx.get(), result, Toast.LENGTH_LONG).show();

            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            // On termine cette activité
            finish();
        }

    }

}
