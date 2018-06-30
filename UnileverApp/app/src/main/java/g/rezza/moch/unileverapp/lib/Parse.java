package g.rezza.moch.unileverapp.lib;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.logging.Level;

public class Parse {

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String toCurrnecy(double pData){
        try {
            NumberFormat formatKurensi = NumberFormat.getCurrencyInstance(new Locale("id"));
            String data = formatKurensi.format(pData);
            data = data.substring(1,(data.length()-3));
            return data;
        }catch (Exception e){
            return "0";
        }

    }

    public static String toCurrnecy(String pData){
        try {
            double dData = Double.parseDouble(pData);
            NumberFormat formatKurensi = NumberFormat.getCurrencyInstance(new Locale("id"));
            String data = formatKurensi.format(dData);
            data = data.substring(1,(data.length()-3));
            return data;
        }catch (Exception e){
            return "0";
        }

    }

    public static String currencyToString(String pData){
        try {
            DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(new Locale("id"));
            String data = pData.replaceAll("\\.","");
            return data;
        }catch (Exception e){
            Log.e("TAG",e.getMessage());
            return "0";
        }

    }

    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        byte[] sha1hash = md.digest();
        return convertToHex(sha1hash);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static String getString(String param){
        String data = "";

        try {
            data = param;
            if (param == null){
                data = "";
            }
        }catch (Exception e){
            Log.d("PARSE", "NULL Convert To String");
        }
        return data;
    }

}
