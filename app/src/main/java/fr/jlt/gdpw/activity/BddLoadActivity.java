package fr.jlt.gdpw.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.provider.SyncStateContract;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.File;

import fr.jlt.gdpw.R;
import fr.jlt.gdpw.metier.BddSQLiteHelper;
import fr.jlt.gdpw.metier.MiniatureCste;
import fr.jlt.gdpw.metier.TableCreateCste;
import fr.jlt.gdpw.table.MiniatureBDD;
import fr.jlt.gdpw.utils.ImportMiniature;
import fr.jlt.gdpw.utils.Utils;

/**
 * Created by jluc1404x on 18/07/15.
 */
public class BddLoadActivity extends Activity {
    private Context context;
    private BddSQLiteHelper helper;
    private SQLiteDatabase bdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.bdd_chargement);
        context = this;
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ///storage/80D0-FD2F/

        String listeMiniatures = Utils.getExternalStorageDirectory(context) + "/GarageDePocheWanted/les_manquees.csv";
        TextView lib00 = (TextView) findViewById(R.id.bddLoadFileName);
        lib00.setText(listeMiniatures);

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

                    Utils.deleteRecursive(new File(Utils.getExternalFilesDir(context)));
                    Utils.deleteRecursive(new File(Utils.getExternalCacheDir(context)));
                }

                TextView lib00 = (TextView) findViewById(R.id.bddLoadFileName);
                ImportMiniature importMiniature = new ImportMiniature(context, bdd);
                importMiniature.importFile(lib00.getText().toString());

                Utils.deleteRecursive(new File(Utils.getExternalStorageDirectory(context) + "/GarageDePocheWanted/"));


                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                // On termine cette activité
                finish();
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

}
