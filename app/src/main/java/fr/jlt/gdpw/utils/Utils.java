package fr.jlt.gdpw.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Classe utilitaire
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


//    public static int calculateInSampleSize2(BitmapFactory.Options options, int reqWidth, int reqHeight) {
//        // Raw height and width of image
//        final int height = options.outHeight;
//        final int width = options.outWidth;
//        int inSampleSize = 1;
//
//        if (height > reqHeight || width > reqWidth) {
//
//            final int halfHeight = height / 2;
//            final int halfWidth = width / 2;
//
//            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
//            // height and width larger than the requested height and width.
//            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
//                inSampleSize *= 2;
//            }
//        }
//
//        return inSampleSize;
//    }

    public static void makeDirs(File file, boolean putNomedia) {
        String path = file.getParent();
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();

            if (putNomedia) {
                // Fichier qui va indiquer au syst??me que notre dossier ne contient rien qui int??resse la galerie
                File nomedia = new File(path + "/.nomedia");
                try {
                    nomedia.createNewFile();
                } catch (IOException e) {
                    Log.d("makeDirs '.nomedia' ", e.getMessage());
                }
            }
        }
    }

    public static void copyFile(File source, File dest) {
        InputStream is = null;
        OutputStream os = null;
        try {
            try {
                is = new FileInputStream(source);
                os = new FileOutputStream(dest);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
            }
            catch (Exception e) {
                Log.d("copyFile", e.getMessage());
            }
            finally {
                if (os != null) {
                    os.close();
                }
                if (is != null) {
                    is.close();
                }
            }
        }
        catch (Exception e) {
            Log.d("copyFile", e.getMessage());
        }

        // Fichier qui va indiquer au syst??me que notre dossier ne contient rien qui int??resse la galerie
        String path = dest.getParent();
        File nomedia = new File(path + "/.nomedia");
        if (!nomedia.exists()) {
            try {
                nomedia.createNewFile();
            }
            catch (IOException e) {
                Log.d("copyFile '.nomedia' ", e.getMessage());
            }
        }
    }

    public static void StoreImage(Bitmap myImage, File file) {
        FileOutputStream fileOutputStream = null;
        BufferedOutputStream bos = null;
        try {
            try {
                fileOutputStream = new FileOutputStream(file);
                bos = new BufferedOutputStream(fileOutputStream);
                myImage.compress(Bitmap.CompressFormat.JPEG, 70, bos);
            }
            catch (Exception e) {
                Log.d("StoreImage", e.getMessage());
            }
            finally {
                if (bos != null) {
                    bos.flush();
                    bos.close();
                }
            }
        }
        catch (Exception e) {
            Log.d("StoreImage", e.getMessage());
        }
    }

    public static String getExternalStorageDirectory(Context context) {
        String path = getExternalFilesDir(context);
        int pos = path.lastIndexOf("/Android/");
        if (pos == -1 || pos == 0)
            return null;

        return path.substring(0, pos);
    }

    public static String getExternalFilesDir(Context context) {
        File[] sds = ContextCompat.getExternalFilesDirs(context, null);
        if (sds.length > 1) {
            return sds[1].getAbsolutePath();
        } else {
            return sds[0].getAbsolutePath();
        }
    }

    public static String getExternalCacheDir(Context context) {
        File[] sds = ContextCompat.getExternalCacheDirs(context);
        if (sds.length > 1) {
            return sds[1].getAbsolutePath();
        } else {
            return sds[0].getAbsolutePath();
        }
    }

    public static void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteRecursive(child);
            }
        }
        fileOrDirectory.delete();
    }

    public static void deleteAllFiles(final File file, final Context ctx) {
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
            String uriString = preferences.getString("TREE_URI", null);
            if (uriString == null) {
                return;
            }

            Uri treeUri = Uri.parse(uriString);
            if (treeUri == null) {
                return;
            }

            DocumentFile documentFile = DocumentFile.fromTreeUri(ctx, treeUri);
            if (documentFile == null) {
                return;
            }
            if (!documentFile.canWrite()) {
                return;
            }

            String rootSD = "/storage" + uriString.substring(uriString.lastIndexOf("tree") + "tree" .length(), uriString.length());
            rootSD = rootSD.replaceAll("%3A", "/").replaceAll("%2F", "/");
            String[] partsRootSD = rootSD.split("\\/");
            String[] partsFile = file.getAbsolutePath().split("\\/");
            if (partsRootSD.length > partsFile.length) {
                return;
            }
            for (int i = 0; i < partsRootSD.length; i++) {
                if (!partsRootSD[i].equals(partsFile[i])) {
                    return;
                }
            }

            if (partsFile.length > partsRootSD.length) {
                for (int i = partsRootSD.length; i < partsFile.length; i++) {
                    DocumentFile nextDocument = documentFile.findFile(partsFile[i]);
                    if (nextDocument == null) {
                        return;
                    }
                    documentFile = nextDocument;
                }
            }

            if (documentFile != null) {
                for (DocumentFile docFile : documentFile.listFiles()) {
                    if (docFile.canWrite()) {
                        docFile.delete();
                    }
                }
            }
        }
        else {
            deleteRecursive(file);
        }

    }

//    public static void deleteRecursive(File fileOrDirectory, final Context ctx) {
//        if (fileOrDirectory.isDirectory()) {
//            for (File child : fileOrDirectory.listFiles()) {
//                deleteRecursive(child, ctx);
//            }
//        }
//
//        // First try the normal deletion.
//        if (fileOrDirectory.delete()) {
//            return;
//        }
//
//        // Try with Storage Access Framework.
//        if (android.os.Build.VERSION.SDK_INT >= 23) {
//            DocumentFile document = getDocumentFile(fileOrDirectory, fileOrDirectory.isDirectory(), ctx);
//            if (!document.delete()) {
//                Log.d("Util", "Delete impossible " + document.getName() );
//            }
//        }
//    }

//    public static DocumentFile getDocumentFile(final File file, final boolean isDirectory, final Context ctx) {
////        String baseFolder = getExtSdCardFolder(file, ctx);
////        if (baseFolder == null) {
////            return null;
////        }
//
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
//        String uriString = preferences.getString("TREE_URI", null);
//        if (uriString == null) {
//            return null;
//        }
//        Uri treeUri = Uri.parse(uriString);
//
//        if (treeUri == null) {
//            return null;
//        }
//        String racine = treeUri.getLastPathSegment();
//        if (racine.indexOf(":") > 0) {
//            if (racine.indexOf(":") + 1 == racine.length()) {
//                racine = racine.substring(0, racine.length() - 1);
//            }
//            else {
//                racine = racine.substring(racine.indexOf(":") + 1);
//            }
//        }
//
//        // start with root of SD card and then parse through document tree.
//        DocumentFile document = DocumentFile.fromTreeUri(ctx, treeUri);
//
//        String relativePath = null;
//        try {
//            String fullPath = file.getCanonicalPath();
//            relativePath = fullPath.substring(fullPath.indexOf(racine) + racine.length() + 1);
//        }
//        catch (IOException e) {
//            return null;
//        }
//
//        String[] parts = relativePath.split("\\/");
//        for (int i = 0; i < parts.length; i++) {
//            DocumentFile nextDocument = document.findFile(parts[i]);
//
//            if (nextDocument == null) {
//                if ((i < parts.length - 1) || isDirectory) {
//                    nextDocument = document.createDirectory(parts[i]);
//                }
//                else {
//                    nextDocument = document.createFile("image", parts[i]);
//                }
//            }
//            document = nextDocument;
//        }
//
//        return document;
//    }

//    public static String getExtSdCardFolder(final File file, final Context ctx) {
//        String[] extSdPaths = getExtSdCardPaths(ctx);
//        try {
//            for (int i = 0; i < extSdPaths.length; i++) {
//                if (file.getCanonicalPath().startsWith(extSdPaths[i])) {
//                    return extSdPaths[i];
//                }
//            }
//        }
//        catch (IOException e) {
//            return null;
//        }
//        return null;
//    }
//
//    /**
//     * Get a list of external SD card paths. (Kitkat or higher.)
//     *
//     * @return A list of external SD card paths.
//     */
//     private static String[] getExtSdCardPaths(final Context ctx) {
//        List<String> paths = new ArrayList<>();
//        for (File file : ctx.getExternalFilesDirs("external")) {
//            if (file != null && !file.equals(ctx.getExternalFilesDir("external"))) {
//                int index = file.getAbsolutePath().lastIndexOf("/Android/data");
//                if (index < 0) {
//                    Log.w("Utils", "Unexpected external file dir: " + file.getAbsolutePath());
//                }
//                else {
//                    String path = file.getAbsolutePath().substring(0, index);
//                    try {
//                        path = new File(path).getCanonicalPath();
//                    }
//                    catch (IOException e) {
//                        // Keep non-canonical path.
//                    }
//                    paths.add(path);
//                }
//            }
//        }
//        return paths.toArray(new String[paths.size()]);
//    }


}
