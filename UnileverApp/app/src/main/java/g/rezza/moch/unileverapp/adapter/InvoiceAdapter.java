package g.rezza.moch.unileverapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import g.rezza.moch.unileverapp.R;
import g.rezza.moch.unileverapp.holder.InvoiceOrderHolder;


/**
 * Created by rezza on 11/02/18.
 */

public class InvoiceAdapter extends ArrayAdapter<InvoiceOrderHolder> {

    private static String TAG = "InvoiceAdapter";
    private LayoutInflater mInflater;

    public InvoiceAdapter(Context context, ArrayList<InvoiceOrderHolder> values) {
        super(context, R.layout.adapter_invoice, values);
        mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        final InvoiceOrderHolder Event = getItem(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_invoice, parent, false);
            holder = new Holder();
            holder.txvw_date_00         = (TextView)  convertView.findViewById(R.id.txvw_date_00);
            holder.txvw_orderid_00      = (TextView)  convertView.findViewById(R.id.txvw_orderid_00);
            holder.txvw_invoice_00      = (TextView)  convertView.findViewById(R.id.txvw_invoice_00);
            holder.txvw_status_00       = (TextView)  convertView.findViewById(R.id.txvw_status_00);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format2 = new SimpleDateFormat("dd MMM yyyy");
        try {
            calendar.setTime(format1.parse(Event.invoice_date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.txvw_date_00.setText(format2.format(calendar.getTime()));
        holder.txvw_orderid_00.setText(Event.order_id);
        holder.txvw_invoice_00.setText(Event.invoice_id);
        holder.txvw_status_00.setText(Event.order_status);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null){
                    mListener.OnSelect(Event, position);
                }
            }
        });

        return convertView;
    }

    /** View holder for the views we need access to */
    private static class Holder {
        public TextView txvw_orderid_00;
        public TextView txvw_invoice_00;
        public TextView txvw_date_00;
        public TextView txvw_status_00;

    }

    private OnSelectedListener mListener;
    public void setOnSelectedListener(OnSelectedListener pListener){
        mListener = pListener;
    }
    public interface OnSelectedListener{
        public void OnSelect(InvoiceOrderHolder event, int position);
    }
}
