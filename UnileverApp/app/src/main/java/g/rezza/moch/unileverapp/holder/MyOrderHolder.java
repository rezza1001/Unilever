package g.rezza.moch.unileverapp.holder;

import java.util.ArrayList;

public class MyOrderHolder {
    public String product;
    public String name;
    public String description;
    public String img_url;
    public long price = 0;
    public int qty = 1;
    public int max_qty = 0;
    private ArrayList<Integer> qtys = new ArrayList<>();

    public ArrayList<Integer> getQtys(){
        qtys.clear();
        for (int i=3; i<=max_qty; i++){
            qtys.add(i);
        }
        return qtys;
    }
}
