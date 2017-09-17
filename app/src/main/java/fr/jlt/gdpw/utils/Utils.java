package fr.jlt.gdpw.utils;

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
}
