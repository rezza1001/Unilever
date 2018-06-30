package g.rezza.moch.unileverapp.holder;

/**
 * Created by rezza on 11/01/18.
 */

public class KeyValueHolder {
    public String key;
    public String value;

    public KeyValueHolder(String pKey, String pValue){
        key = pKey;
        value = pValue;
    }

    public KeyValueHolder(String pKey, int pValue){
        key = pKey;
        value = pValue +"";
    }

}
