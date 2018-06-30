package g.rezza.moch.unileverapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import g.rezza.moch.unileverapp.R;
import g.rezza.moch.unileverapp.database.ChartDB;
import g.rezza.moch.unileverapp.holder.ProductHolder;
import g.rezza.moch.unileverapp.lib.Parse;


/**
 * Created by rezza on 11/02/18.
 */

public class ProductGridAdapter extends ArrayAdapter<ProductHolder> {

    private static String TAG = "ProductOrderAdapter";
    private LayoutInflater mInflater;

    public ProductGridAdapter(Context context, ArrayList<ProductHolder> values) {
        super(context, R.layout.adapter_product_grid, values);
        mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        final ProductHolder Event = getItem(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_product_grid, parent, false);
            holder = new Holder();
            holder.txvw_price_00    = (TextView)  convertView.findViewById(R.id.txvw_price_00);
            holder.txvw_description_00    = (TextView)  convertView.findViewById(R.id.txvw_description_00);
            holder.imvw_product_00  = (ImageView) convertView.findViewById(R.id.imvw_product_00);
            holder.bbtn_action_00   = (Button) convertView.findViewById(R.id.bbtn_action_00);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.txvw_price_00.setText("Rp. "+ Parse.toCurrnecy(Event.price));
        holder.txvw_description_00.setText(Event.description);
        Glide.with(getContext()).load(Event.img_url).into(holder.imvw_product_00);

//        if (Event.inChart == 0){
//            holder.bbtn_action_00.setBackgroundResource(R.drawable.button_signin);
//            holder.bbtn_action_00.setText(getContext().getResources().getString(R.string.add_to_chart));
//        }
//        else {
//            holder.bbtn_action_00.setBackgroundResource(R.drawable.button_remove);
//            holder.bbtn_action_00.setText(getContext().getResources().getString(R.string.remove_from_chart));
//        }

        holder.bbtn_action_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null){
                    mListener.OnSelect(Event,position);
                }
            }
        });

        return convertView;
    }

    /** View holder for the views we need access to */
    private static class Holder {
        protected ImageView imvw_product_00;
        protected TextView txvw_description_00;
        protected TextView txvw_price_00;
        protected Button bbtn_action_00;

    }

    private OnSelectedListener mListener;
    public void setOnSelectedListener(OnSelectedListener pListener){
        mListener = pListener;
    }
    public interface OnSelectedListener{
        public void OnSelect(ProductHolder event, int position);
    }
}
