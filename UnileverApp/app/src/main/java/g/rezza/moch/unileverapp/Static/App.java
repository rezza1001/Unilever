package g.rezza.moch.unileverapp.Static;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import g.rezza.moch.unileverapp.lib.Parse;

public class App {
    public static final int MENU_LIST = 1;
    public static final int MENU_GRID = 2;

    public static String DOKU_MALLID = "5865";
    public static String SHAREDKEY = "0RGmzf441tJU";
    public static String DOKU_URL_RECEIVE = "https://staging.doku.com/Suite/Receive";


    public static String getWords(Double amount, String transid){
        StringBuilder words = new StringBuilder();
        words.append(amount).append(DOKU_MALLID).append(SHAREDKEY).append(transid);
        try {
            return Parse.SHA1(words.toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getSession(){
        Calendar calendar = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String session = df.format(calendar.getTime());
        return session;
    }
}
