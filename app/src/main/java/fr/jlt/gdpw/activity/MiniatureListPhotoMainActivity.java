package fr.jlt.gdpw.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import fr.jlt.gdpw.R;
import fr.jlt.gdpw.adapter.MiniatureListPhotoAdapter;
import fr.jlt.gdpw.constantes.ActivityCste;
import fr.jlt.gdpw.metier.BddSQLiteHelper;
import fr.jlt.gdpw.metier.MiniatureCste;
import fr.jlt.gdpw.table.MiniatureBDD;

/**
 * Created by jluc1404x on 18/07/15.
 */
public class MiniatureListPhotoMainActivity extends Activity {
    private ListView listView;
    private TextView textView;
    private BddSQLiteHelper helper;
    private SQLiteDatabase bdd;
    private Context context;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.miniature_list_photo);

        context = this;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String ordre = preferences.getString("ORDRE", MiniatureCste.RUBRIQUE);
        String sens = preferences.getString("SENS", "ASC");

        helper = new BddSQLiteHelper(this);
        bdd = helper.getReadableDatabase();
        cursor = MiniatureBDD.getAllList(bdd, ordre, sens);
        if (cursor.getCount() == 0) {
            // la base est vide ==> il faut la recharger
            Intent intent = new Intent(context, BddLoadActivity.class);
            startActivityForResult(intent, ActivityCste.REQUEST_CODE_LOAD_BDD);
        }


        textView = (TextView) findViewById(R.id.miniatureListPhotoNbFiche);
        textView.setText("[ " + String.valueOf(cursor.getCount()) + " ]");

        listView = (ListView) findViewById(R.id.miniatureListPhoto);
        // Assign adapter to ListView
        listView.setAdapter(useCustomCusorAdapter(cursor));

//        listView.setFastScrollEnabled(true);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                Intent intent = new Intent(context, MiniatureFicheActivity.class);
                intent.putExtra("id", cursor.getInt(0));
                startActivityForResult(intent, ActivityCste.REQUEST_CODE_FICHE);
            }
        });
    }

    // plus simple lors de l'appel, mais nécessite la création d'une classe CustomCursorAdapter
    private CursorAdapter useCustomCusorAdapter(Cursor cursor) {
         return new MiniatureListPhotoAdapter(context, cursor, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bdd.close();
        helper.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // vérifie que c'est le bon  code d'appel
        if (ActivityCste.REQUEST_CODE_FICHE == requestCode) {
            // Vérifie que le résultat est OK
            if (resultCode == RESULT_OK) {
                restart();
            }
        }
        else if (ActivityCste.REQUEST_CODE_SORT == requestCode) {
            // Vérifie que le résultat est OK
            if (resultCode == RESULT_OK) {
                restart();

                // Si l'activité est annulé
            } else if (resultCode == RESULT_CANCELED) {
                // On affiche que l'opération est annulée
                Toast.makeText(this, "Opération annulée", Toast.LENGTH_SHORT).show();
            }
        }
        else if (ActivityCste.REQUEST_CODE_LOAD_BDD == requestCode) {
            // Vérifie que le résultat est OK
            if (resultCode == RESULT_OK) {
                restart();

                // Si l'activité est annulé
            } else if (resultCode == RESULT_CANCELED) {
                // On affiche que l'opération est annulée
                Toast.makeText(this, "Chargement de la base annulé", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void restart() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String ordre = preferences.getString("ORDRE", MiniatureCste.RUBRIQUE);
        String sens = preferences.getString("SENS", "ASC");
        cursor = MiniatureBDD.getAllList(bdd, ordre, sens);
        // Assign adapter to ListView
        listView.setAdapter(useCustomCusorAdapter(cursor));
        textView.setText("[" + String.valueOf(cursor.getCount()) + "]");
    }

    public void handleSortClick(View v) {
        Intent intent = new Intent(this, SortFicheActivity.class);
        startActivityForResult(intent, ActivityCste.REQUEST_CODE_SORT);
    }

    public void handleBddLoadClick(View v) {
        Intent intent = new Intent(context, BddLoadActivity.class);
        startActivityForResult(intent, ActivityCste.REQUEST_CODE_LOAD_BDD);
    }

}
