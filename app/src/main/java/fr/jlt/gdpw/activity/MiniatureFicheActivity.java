package fr.jlt.gdpw.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.jar.JarEntry;

import fr.jlt.gdpw.R;
import fr.jlt.gdpw.donneesDAO.Miniature;
import fr.jlt.gdpw.metier.BddSQLiteHelper;
import fr.jlt.gdpw.table.MiniatureBDD;
import fr.jlt.gdpw.utils.Utils;


public class MiniatureFicheActivity extends Activity {
    private String imageName = null;
    private Miniature miniature;
    private boolean trouveModifie = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.miniature_fiche);

        int id = this.getIntent().getExtras().getInt("id");
        this.getIntent().removeExtra("id");

        BddSQLiteHelper helper = new BddSQLiteHelper(this);
        SQLiteDatabase bdd = helper.getReadableDatabase();
        miniature = MiniatureBDD.get(id, bdd);
        bdd.close();
        helper.close();

        imageName = Utils.getExternalFilesDir(this) + miniature.getPhoto();

        TextView name = (TextView) findViewById(R.id.miniatureFicheModele);
        name.setText(miniature.getModele());

        ImageView img = (ImageView) findViewById(R.id.miniatureFichePhoto);
        Bitmap bmp = BitmapFactory.decodeFile(imageName);
        if (bmp != null) {
            img.setImageBitmap(bmp);
        }
        else {
            img.setImageResource(R.drawable.silhouette_large);
        }


        TextView tit0A = (TextView) findViewById(R.id.miniatureFicheTit0A);
        TextView tit01 = (TextView) findViewById(R.id.miniatureFicheTit01);
        TextView tit02 = (TextView) findViewById(R.id.miniatureFicheTit02);
        TextView tit03 = (TextView) findViewById(R.id.miniatureFicheTit03);
        TextView tit04 = (TextView) findViewById(R.id.miniatureFicheTit04);
        TextView tit05 = (TextView) findViewById(R.id.miniatureFicheTit05);
        TextView tit06 = (TextView) findViewById(R.id.miniatureFicheTit06);
        TextView tit07 = (TextView) findViewById(R.id.miniatureFicheTit07);
        TextView tit08 = (TextView) findViewById(R.id.miniatureFicheTit08);
        TextView tit09 = (TextView) findViewById(R.id.miniatureFicheTit09);
        TextView tit10 = (TextView) findViewById(R.id.miniatureFicheTit10);


        TextView lib01 = (TextView) findViewById(R.id.miniatureFicheLib01);
        TextView lib02 = (TextView) findViewById(R.id.miniatureFicheLib02);
        TextView lib03 = (TextView) findViewById(R.id.miniatureFicheLib03);
        TextView lib04 = (TextView) findViewById(R.id.miniatureFicheLib04);
        TextView lib05 = (TextView) findViewById(R.id.miniatureFicheLib05);
        TextView lib06 = (TextView) findViewById(R.id.miniatureFicheLib06);
        TextView lib07 = (TextView) findViewById(R.id.miniatureFicheLib07);
        TextView lib08 = (TextView) findViewById(R.id.miniatureFicheLib08);
        TextView lib09 = (TextView) findViewById(R.id.miniatureFicheLib09);
        TextView lib10 = (TextView) findViewById(R.id.miniatureFicheLib10);


        CheckBox checkBox = (CheckBox) findViewById(R.id.miniatureFicheCheckBox);
        if ("0".equals(miniature.getTrouve())) {
            checkBox.setChecked(false);
        }
        else {
            checkBox.setChecked(true);
        }

        tit01.setText("Objectif :");
        tit01.setTypeface(lib05.getTypeface(), Typeface.BOLD);
        lib01.setText(Utils.getPreference(miniature.getPreference()));
        lib01.setTextColor(Color.rgb(130, 0, 0));
        lib01.setTypeface(lib05.getTypeface(), Typeface.BOLD_ITALIC);

        tit02.setText("Marque :");
        lib02.setText(miniature.getMarque());

        tit03.setText("Carrosserie :");
        lib03.setText(miniature.getCarrosserie());

        tit04.setText("Miniature");
//        tit04.setTextColor(Color.rgb(54, 0, 127));
        tit04.setTextColor(getResources().getColor(R.color.app_color));
        tit04.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26.f);
        tit04.setTypeface(null, Typeface.BOLD_ITALIC);
        tit04.setPadding(tit0A.getPaddingLeft(), 5, tit0A.getPaddingRight(), 20);
        tit04.setGravity(Gravity.LEFT);
        lib04.setText("");

        tit05.setText("Collection :");
        lib05.setText(miniature.getCollection());

        tit06.setText("Editeur :");
        lib06.setText(miniature.getEditeur());

        tit07.setText("Référence :");
        lib07.setText(miniature.getReference());

        tit08.setText("Fabricant :");
        lib08.setText(miniature.getFabricant());

        tit09.setText("Date de sortie :");
        lib09.setText(Utils.convertDate(miniature.getDateSortie()));

        tit10.setText("Prix initial :");
        tit10.setTypeface(lib05.getTypeface(), Typeface.BOLD);
        lib10.setText(miniature.getPrix() + " €");
        lib10.setTypeface(lib05.getTypeface(), Typeface.BOLD_ITALIC);


    }

    // termine l'activité (donc retour à l'activité précédente) si on clique sur la vue
    public void handleClick(View v) {
        if (trouveModifie) {
            setResult(RESULT_OK);
        }
        else {
            setResult(RESULT_CANCELED);
        }
        // On termine cette activité
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Désactive la touche retour
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (trouveModifie) {
                setResult(RESULT_OK);
            }
            else {
                setResult(RESULT_CANCELED);
            }
            // On termine cette activité
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    // affiche l'image en grand si on clique sur l'image
    public void handleImgClick(View v) {
        Intent intent = new Intent(this, MiniaturePhotoActivity.class);
        intent.putExtra("idPhoto", imageName);
        startActivity(intent);
    }

    public void handleTrouveClick(View v) {
        trouveModifie = true;
        CheckBox checkBox = (CheckBox) findViewById(R.id.miniatureFicheCheckBox);
        if (checkBox.isChecked()) {
            miniature.setTrouve("1");
        }
        else {
            miniature.setTrouve("0");
        }

        BddSQLiteHelper helper = new BddSQLiteHelper(this);
        SQLiteDatabase bdd = helper.getReadableDatabase();

        MiniatureBDD.upDateTrouve(miniature, bdd);

        bdd.close();
        helper.close();
    }
}