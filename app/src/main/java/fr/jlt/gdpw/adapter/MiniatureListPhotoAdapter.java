package fr.jlt.gdpw.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import fr.jlt.gdpw.R;
import fr.jlt.gdpw.activity.MiniaturePhotoActivity;
import fr.jlt.gdpw.metier.MiniatureCste;
import fr.jlt.gdpw.utils.Utils;


/**
 * classe Adapter permettant d'afficher le ListView de MiniatureListPhotoMainActivity
 * Created by jluc1404x on 18/07/15.
 */
public class MiniatureListPhotoAdapter extends CursorAdapter {

    /**
     *  Holder : controleur qui mémorise les références vers les  sous vues.
     *  sera ensuite stocké en tant que propriété de la vue
     *  Utilsé afin d’éviter d’appeler les méthodes findViewById qui sont couteuses en ressource
     */
    private class ListPhotoHolder {
        public TextView separator = null;
        public TextView textViewName = null;
        public TextView textViewLib1 = null;
        public TextView textViewLib2 = null;
        public TextView textViewLib3 = null;
        public TextView textViewLib4 = null;
        public ImageView imageView = null;
    }

    private LayoutInflater mInflater;

    private String ordre;

    private boolean sep;


    /**
     * Constructeur
     * @param context
     * @param cursor
     * @param flags
     */
    public MiniatureListPhotoAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        ordre = preferences.getString("ORDRE", MiniatureCste.RUBRIQUE);
        sep = preferences.getBoolean("SEP", false);
    }

    /**
     * newView()
     *  Appelée pour créer une vue représentant un élément dans la liste,
     *  Il faut juste créer un objet sans définir de valeurs (c'est bindView() qui s'en charge)
     * @param context
     * @param cursor
     * @param parent
     * @return
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.miniature_list_photo_row, parent, false);
        // Ajout du Holder qui controllera les sous vues de cette vue
        ListPhotoHolder holder = new ListPhotoHolder();
        holder.separator = (TextView) view.findViewById(R.id.miniatureListPhotoRowSeparator);
        holder.textViewName = (TextView) view.findViewById(R.id.miniatureListPhotoRowModele);
        holder.textViewLib1 = (TextView) view.findViewById(R.id.miniatureListPhotoRowLib1);
        holder.textViewLib2 = (TextView) view.findViewById(R.id.miniatureListPhotoRowLib2);
        holder.textViewLib3 = (TextView) view.findViewById(R.id.miniatureListPhotoRowLib3);
        holder.textViewLib4 = (TextView) view.findViewById(R.id.miniatureListPhotoRowLib4);
        holder.imageView = (ImageView) view.findViewById(R.id.miniatureListPhotoRowPhoto);
        // on génère un événement sur la petite photo
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                imageClick(view);
            }
        });
        view.setTag(holder);
        return view;
    }

    /**
     * bindView()
     *  Appelée pour définir les valeurs à afficher.
     *  Le premier paramètre est
     *      soit le retour de NewView si besoin d'une nouvelle vue,
     *      soit une vue recyclée par android lorsqu'elle sort de l'écran et n'est plus visible.
     * @param view
     * @param context
     * @param cursor
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int visibilite = View.GONE; // invisible par défaut
        String separator = "";
        if (sep) {
            separator = getSeparator(cursor);
            final int position = cursor.getPosition();
            if (position == 0) {
                visibilite = View.VISIBLE;
            }
            else {
                cursor.moveToPosition(position - 1);
                String separatorPrev = getSeparator(cursor);
                if (!separator.equals(separatorPrev)) {
                    visibilite = View.VISIBLE;
                }
                cursor.moveToPosition(position);
            }
        }
        // Récupère le Holder pour utiliser les sous vues de cette vue
        ListPhotoHolder holder = (ListPhotoHolder) view.getTag();
        holder.separator.setText(separator);
        holder.separator.setVisibility(visibilite);
        // Valorise les champs des sous vues, à partir du cursor
        holder.textViewName.setText(cursor.getString(cursor.getColumnIndex(MiniatureCste.RUBRIQUE)));
        if ("1".equals(cursor.getString(cursor.getColumnIndex(MiniatureCste.TROUVE)))) {
            holder.textViewName.setPaintFlags(holder.textViewName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            //view.setBackgroundColor(context.getResources().getColor(R.color.app_color));
            view.setBackgroundColor(Color.LTGRAY);
            holder.textViewName.setTextColor(Color.WHITE);
            holder.textViewLib1.setTextColor(Color.WHITE);
            holder.textViewLib2.setTextColor(Color.WHITE);
            holder.textViewLib3.setTextColor(Color.WHITE);
            holder.textViewLib4.setTextColor(Color.WHITE);
        }
        else {
            holder.textViewName.setPaintFlags(holder.textViewName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            view.setBackgroundColor(Color.WHITE);
            //holder.textViewName.setTextColor(context.getResources().getColor(R.color.app_color));
            holder.textViewName.setTextColor(Color.BLACK);
            holder.textViewLib1.setTextColor(Color.GRAY);
            holder.textViewLib2.setTextColor(Color.GRAY);
            holder.textViewLib3.setTextColor(Color.GRAY);
            holder.textViewLib4.setTextColor(Color.DKGRAY);
        }

        holder.textViewLib1.setText(cursor.getString(cursor.getColumnIndex(MiniatureCste.MARQUE)));
        holder.textViewLib2.setText(Utils.convertDate(cursor.getString(cursor.getColumnIndex(MiniatureCste.DATESORTIE))));
        StringBuffer sb = new StringBuffer();
        if (cursor.getString(cursor.getColumnIndex(MiniatureCste.REFERENCE)) != null) {
            if (!"cadeau".equals(cursor.getString(cursor.getColumnIndex(MiniatureCste.REFERENCE)).trim().toLowerCase())) {
                sb.append("N° ");
            }
            sb.append(cursor.getString(cursor.getColumnIndex(MiniatureCste.REFERENCE)));
            sb.append(" - ");
        }
        if ("1".equals(cursor.getString(cursor.getColumnIndex(MiniatureCste.TROUVE)))) {
            sb.append("trouvé".toUpperCase());
        }
        else {
            sb.append(Utils.getPreference(cursor.getString(cursor.getColumnIndex(MiniatureCste.PREFERENCE))));
        }
        holder.textViewLib3.setText(sb.toString());
        if (cursor.getString(cursor.getColumnIndex(MiniatureCste.PRIX)) != null) {
            holder.textViewLib4.setText(cursor.getString(cursor.getColumnIndex(MiniatureCste.PRIX)) + " €");
        }
        else {
            holder.textViewLib4.setText("-.-- €");

        }

        String imageName = cursor.getString(cursor.getColumnIndex(MiniatureCste.PHOTO));
        if (Utils.isNotEmptyOrNull(imageName)) {
            Bitmap bmp = BitmapFactory.decodeFile(Utils.getExternalFilesDir(context) + "/cached" + imageName);
            if (bmp != null) {
                holder.imageView.setImageBitmap(bmp);
            }
            else {
                File fileDest = new File(Utils.getExternalFilesDir(context), imageName);
                File fileCacheDest = new File(Utils.getExternalFilesDir(context) + "/cached", imageName);
                Utils.makeDirs(fileCacheDest, true);
                bmp = Utils.decodeSampledBitmapFromUri(fileDest.getAbsolutePath(), 200, 150);
                if (bmp != null) {
                    Utils.StoreImage(bmp, fileCacheDest);
                    holder.imageView.setImageBitmap(bmp);
                }
                else {
                    holder.imageView.setImageResource(R.drawable.silhouette_small);
                }
            }
            // on stocke le nom de la grande photo en tant que tag dans la petite photo
            holder.imageView.setTag(Utils.getExternalFilesDir(context) + imageName);
        }
        else {
            holder.imageView.setImageResource(R.drawable.silhouette_small);
        }
    }

    /**
     * imageClick()
     *  gestionnaire de click sur l'image
     * @param view
     */
    public void imageClick(View view) {
        Context context = view.getContext();
        // on recupère l'id de la grande photo dans le tag de la petite
        String imageName = (String) view.getTag();
        Intent intent = new Intent(context, MiniaturePhotoActivity.class);
        intent.putExtra("idPhoto", imageName);
        context.startActivity(intent);
    }

    /**
     *
     * @param cursor
     * @return
     */
    private String getSeparator(Cursor cursor) {
        String separator = cursor.getString(cursor.getColumnIndex(ordre));
        if (Utils.isEmptyOrNullOrBlank(separator)) {
            separator = "Inconnu(e)";
        }
        else if (MiniatureCste.DATESORTIE.equals(ordre)) {
            separator = Utils.convertDate(separator);
        }
        else if (MiniatureCste.PREFERENCE.equals(ordre)) {
            separator = Utils.getPreference(separator);
        }
        return separator;
    }
}
