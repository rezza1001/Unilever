package g.rezza.moch.unileverapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import g.rezza.moch.unileverapp.R;
import g.rezza.moch.unileverapp.component.SimpleSpinner;
import g.rezza.moch.unileverapp.database.ChartDB;
import g.rezza.moch.unileverapp.holder.MyOrderHolder;
import g.rezza.moch.unileverapp.holder.SpinerHolder;
import g.rezza.moch.unileverapp.lib.Parse;


/**
 * Created by rezza on 11/02/18.
 */

public class ProductOrderAdapter extends ArrayAdapter<MyOrderHolder> {

    private static String TAG = "ProductOrderAdapter";
    private LayoutInflater mInflater;

    public ProductOrderAdapter(Context context, ArrayList<MyOrderHolder> values) {
        super(context, R.layout.adapter_product_order, values);
        mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        final MyOrderHolder Event = getItem(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_product_order, parent, false);
            holder = new Holder();
            holder.txvw_price_00        = (TextView)  convertView.findViewById(R.id.txvw_price_00);
            holder.txvw_description_00  = (TextView)  convertView.findViewById(R.id.txvw_description_00);
            holder.imvw_product_00      = (ImageView) convertView.findViewById(R.id.imvw_product_00);
            holder.imvw_remove_00       = (ImageView) convertView.findViewById(R.id.imvw_remove_00);
            holder.spnr_qty_00          = (SimpleSpinner) convertView.findViewById(R.id.spnr_qty_00);
            holder.rvly_delete_00       = (RelativeLayout) convertView.findViewById(R.id.rvly_delete_00);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.txvw_price_00.setText("Rp. "+ Parse.toCurrnecy(Event.price));
        holder.txvw_description_00.setText(Event.description);
        Glide.with(getContext()).load(Event.img_url).into(holder.imvw_product_00);

        ArrayList<SpinerHolder> spnrs = new ArrayList<>();
        for (Integer data :Event.getQtys()){
            spnrs.add(new SpinerHolder(data,data));
        }
        holder.spnr_qty_00.setChoosers(spnrs);
        holder.spnr_qty_00.setOnChangeListener(new SimpleSpinner.ChangeListener() {
            @Override
            public void after() {
                Event.qty = Integer.parseInt(holder.spnr_qty_00.getValue().value);
                if (mQtyListener != null){
                    mQtyListener.OnSelect(Event, position);
                }
            }
        });

        holder.spnr_qty_00.setValue(Event.qty+"");

        holder.rvly_delete_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChartDB chartDB = new ChartDB();
                chartDB.deleteData(getContext(), Event.product);
                if (mListener != null){
                    mListener.afterChange(Event, position);
                }
            }
        });

        return convertView;
    }

    /** View holder for the views we need access to */
    private static class Holder {
        public ImageView imvw_product_00;
        public ImageView imvw_remove_00;
        public TextView txvw_description_00;
        public TextView txvw_price_00;
        public SimpleSpinner spnr_qty_00;
        public RelativeLayout rvly_delete_00;
    }

    private afterDeleteListener mListener;
    public void setAfterDeleteListener(afterDeleteListener pListener){
        mListener = pListener;
    }
    public interface afterDeleteListener{
        public void afterChange(MyOrderHolder event, int position);
    }

    private OnChangeQtyListener mQtyListener;
    public void setOOnChangeQtyListener(OnChangeQtyListener pListener){
        mQtyListener = pListener;
    }
    public interface OnChangeQtyListener{
        public void OnSelect(MyOrderHolder product, int position);
    }
}
