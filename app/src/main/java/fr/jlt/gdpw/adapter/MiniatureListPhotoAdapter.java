package fr.jlt.gdpw.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.StringTokenizer;

import fr.jlt.gdpw.R;
import fr.jlt.gdpw.activity.MiniaturePhotoActivity;
import fr.jlt.gdpw.metier.MiniatureCste;
import fr.jlt.gdpw.utils.Utils;


/**
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
        holder.textViewLib1.setText(cursor.getString(cursor.getColumnIndex(MiniatureCste.MARQUE)));
        holder.textViewLib2.setText(Utils.convertDate(cursor.getString(cursor.getColumnIndex(MiniatureCste.DATESORTIE))));
        StringBuffer sb = new StringBuffer();
        if (!"cadeau".equals(cursor.getString(cursor.getColumnIndex(MiniatureCste.REFERENCE)).trim().toLowerCase())) {
            sb.append("N° ");
        }
        sb.append(cursor.getString(cursor.getColumnIndex(MiniatureCste.REFERENCE)));
        sb.append(" - ");
        sb.append(Utils.getPreference(cursor.getString(cursor.getColumnIndex(MiniatureCste.PREFERENCE))));
        holder.textViewLib3.setText(sb.toString());
        holder.textViewLib4.setText(cursor.getString(cursor.getColumnIndex(MiniatureCste.PRIX)) + " €");
        holder.imageView.setImageResource(cursor.getInt(cursor.getColumnIndex(MiniatureCste.PHOTO_SMALL)));
//        int idPhoto = cursor.getInt(cursor.getColumnIndex(MiniatureCste.PHOTO_SMALL));
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inScaled = false;
//        Bitmap bimtBitmap = BitmapFactory.decodeResource(context.getResources(), idPhoto, options);
//        holder.imageView.setImageBitmap(bimtBitmap);
        // on stocke l'id de la grande photo en tant que tag dans la petite photo
        holder.imageView.setTag(cursor.getInt(cursor.getColumnIndex(MiniatureCste.PHOTO)));
    }

    /**
     * imageClick()
     *  gestionnaire de click sur l'image
     * @param view
     */
    public void imageClick(View view) {
        Context context = view.getContext();
        // on recupère l'id de la grande photo dans le tag de la petite
        Integer idPhoto = (Integer) view.getTag();
        Intent intent = new Intent(context, MiniaturePhotoActivity.class);
        intent.putExtra("idPhoto", idPhoto);
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