package fr.jlt.gdpw.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by jluc1404x on 18/07/15.
 */
public class Utils {
    private Utils() {

    }

    public static boolean isNotEmptyOrNull(String chaine) {
        return !isEmptyOrNull(chaine);
    }

    public static boolean isEmptyOrNull(String chaine) {
        boolean res = false;
        if (chaine == null || "".equals(chaine)) {
            res = true;
        }
        return res;
    }

    public static boolean isNotEmptyOrNullOrBlank(String chaine) {
        return !isEmptyOrNullOrBlank(chaine);
    }

    public static boolean isEmptyOrNullOrBlank(String chaine) {
        boolean res = false;
        if (chaine == null || "".equals(chaine.trim())) {
            res = true;
        }
        return res;
    }

    public static boolean isNotEmptyOrNull(Integer chaine) {
        return !isEmptyOrNull(chaine);
    }

    public static boolean isEmptyOrNull(Integer chaine) {
        boolean res = false;
        if (chaine == null) {
            res = true;
        }
        return res;
    }

    public static boolean isPresentInList(String val, String[] tab) {
        List<String> list = Arrays.asList(tab);
        return isPresentInList(val, list);
    }

    public static boolean isPresentInList(String val, List<String> liste) {
        boolean res = false;
        for (String elem : liste) {
            if (elem.equals(val)) {
                res = true;
                break;
            }
        }
        return res;
    }

    /**
     * Transforme  "AAAA/MM/JJ"  en  "JJ / MM / AAAA"
     * @param date
     * @return
     */
    public static String convertDate(String date) {
        String ret = null;
        try {
            StringTokenizer st = new StringTokenizer(date, "/");
            String AAAA = st.nextToken();
            String MM = st.nextToken();
            String JJ = st.nextToken();
            ret = new StringBuffer(JJ).append(" / ").append(MM).append(" / ").append(AAAA).toString();
        } catch (Exception e) {
           ret = date;
        }
        return ret;
    }

    public static String getPreference(String code)  {
        String preference = null;
        if ("0".equals(code)) {
            preference = "faible";         //   faible     mineur   basse
        }
        if ("1".equals(code)) {
            preference = "normal";         //   normal    moyenne
        }
        if ("2".equals(code)) {
            preference = "important";     //    important    majeur  haute
        }
        if ("3".equals(code)) {
            preference = "prioritaire";
        }
        return preference;
    }


    public static Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap bm = BitmapFactory.decodeFile(path, options);

        return bm;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float)height / (float)reqHeight);
            } else {
                inSampleSize = Math.round((float)width / (float)reqWidth);
            }
        }

        return inSampleSize;
    }


    public static int calculateInSampleSize2(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

}
