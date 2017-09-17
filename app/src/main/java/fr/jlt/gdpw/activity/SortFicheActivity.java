package fr.jlt.gdpw.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import fr.jlt.gdpw.R;
import fr.jlt.gdpw.data.OrderDataBean;
import fr.jlt.gdpw.metier.MiniatureCste;

/**
 * classe permettant de frier la liste des miniature à afficher
 * Created by jluc1404x on 18/07/15.
 */
public class SortFicheActivity extends Activity {
    List<OrderDataBean> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sort_fiche);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        orderList = new ArrayList<OrderDataBean>();
        orderList.add(new OrderDataBean(0, MiniatureCste.RUBRIQUE, "par modèle (défaut)."))    ;
        orderList.add(new OrderDataBean(1, MiniatureCste.MARQUE, "par marque."))    ;
        orderList.add(new OrderDataBean(2, MiniatureCste.PREFERENCE, "par objectif."))    ;
        orderList.add(new OrderDataBean(3, MiniatureCste.PRIX, "par prix."))    ;
        orderList.add(new OrderDataBean(4, MiniatureCste.COLLECTION, "par collection."))    ;
        orderList.add(new OrderDataBean(5, MiniatureCste.EDITEUR, "par éditeur."))    ;
        orderList.add(new OrderDataBean(6, MiniatureCste.FABRICANT, "par fabricant."))    ;
        orderList.add(new OrderDataBean(7, MiniatureCste.DATESORTIE, "par date de sortie."))    ;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String ordre = preferences.getString("ORDRE", MiniatureCste.RUBRIQUE);

        RadioGroup radioGroup0 = (RadioGroup) findViewById(R.id.sortFicheRadioGroup0);
        for (OrderDataBean orderDB : orderList) {
            RadioButton radioButton = (RadioButton) radioGroup0.getChildAt(orderDB.getId());
            radioButton.setText(orderDB.getLibelle());
            if (orderDB.getRubrique().equals(ordre)) {
                radioButton.setChecked(true);
            }
        }

        int count = radioGroup0.getChildCount();
        for (int i = 0; i < count; i++) {
            RadioButton rb = (RadioButton) radioGroup0.getChildAt(i);
            if ("-".equals(rb.getText())) {
                rb.setVisibility(View.INVISIBLE);
            }
        }

        radioGroup0.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) findViewById(checkedId);
                for (OrderDataBean orderDB : orderList) {
                    if (rb.getText().equals(orderDB.getLibelle())) {
                        CheckBox checkBox = (CheckBox) findViewById(R.id.sortFicheCheckBox);
                        if (MiniatureCste.RUBRIQUE.equals(orderDB.getRubrique())) {
//                            checkBox.setChecked(false);
                            checkBox.setEnabled(false);
                            checkBox.setTextColor(Color.rgb(170, 170, 170));
                        }
                        else {
                            checkBox.setEnabled(true);
                            checkBox.setTextColor(Color.rgb(0, 0, 0));
                        }
                    }
                }
            }
        });

        String sens = preferences.getString("SENS", "ASC");
        RadioGroup radioGroup2 = (RadioGroup) findViewById(R.id.sortFicheRadioGroup2);
        if ("ASC".equals(sens)) {
//            radioGroup2.check(radioGroup2.getChildAt(0).getId());
            radioGroup2.check(R.id.sortFicheRadio20);     // plus rapide, moins évolutif
        }
        else if ("DESC".equals(sens)) {
//           radioGroup2.check(radioGroup2.getChildAt(1).getId());
            radioGroup2.check(R.id.sortFicheRadio21);     // plus rapide, moins évolutif
        }

        boolean sep = preferences.getBoolean("SEP", false);
        CheckBox checkBox = (CheckBox) findViewById(R.id.sortFicheCheckBox);
        if (MiniatureCste.RUBRIQUE.equals(ordre)) {
            checkBox.setChecked(false);
            checkBox.setEnabled(false);
            checkBox.setTextColor(Color.rgb(170, 170, 170));
        }
        else {
            checkBox.setChecked(sep);
            checkBox.setEnabled(true);
            checkBox.setTextColor(Color.rgb(0, 0, 0));
        }

        Button boutonValider = (Button) findViewById(R.id.sortFicheBoutonValider);
        boutonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ordre = "";
                String sens = "";
                boolean sep = false;
                RadioGroup radioGroup0 = (RadioGroup) findViewById(R.id.sortFicheRadioGroup0);
                RadioButton rb = (RadioButton) findViewById(radioGroup0.getCheckedRadioButtonId());
                for (OrderDataBean orderDB : orderList) {
                    if (rb.getText().equals(orderDB.getLibelle())) {
                        ordre = orderDB.getRubrique();
                    }
                }

                RadioGroup radioGroup2 = (RadioGroup) findViewById(R.id.sortFicheRadioGroup2);
                int id = radioGroup2.getCheckedRadioButtonId();
                if (id == R.id.sortFicheRadio20) {
                    sens = "ASC";
                }
                else if (id == R.id.sortFicheRadio21) {
                    sens = "DESC";
                }

                CheckBox checkBox = (CheckBox) findViewById(R.id.sortFicheCheckBox);
                sep = false;
                if (checkBox.isEnabled() && checkBox.isChecked()) {
                    sep = true;
                }

//                Toast.makeText(context, " ORDRE : '" + ordre + "' - SENS : '" + sens + "'" , Toast.LENGTH_SHORT).show();
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(v.getContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("ORDRE", ordre);
                editor.putString("SENS", sens);
                editor.putBoolean("SEP", sep);
                editor.commit();

                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                // On termine cette activité
                finish();
            }
        });


        Button boutonAnnuler = (Button) findViewById(R.id.sortFicheNameBoutonAnnuler);
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


}
