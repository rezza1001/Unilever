package g.rezza.moch.unileverapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import g.rezza.moch.unileverapp.R;
import g.rezza.moch.unileverapp.component.ListOrder;
import g.rezza.moch.unileverapp.component.TextKeyValue;
import g.rezza.moch.unileverapp.database.OrderDB;
import g.rezza.moch.unileverapp.database.OutletDB;
import g.rezza.moch.unileverapp.database.ProductDB;
import g.rezza.moch.unileverapp.holder.OrderPayment;

/**
 * Created by rezza on 09/02/18.
 */

public class SalesOrderFragment extends Fragment {
    private static final String TAG = "SalesOrderFragment";

    private TextKeyValue txkv_order_00;
    private TextKeyValue txkv_date_order_00;
    private TextKeyValue txkv_outlet_00;
    private TextKeyValue txkv_date_order_01;
    private TextKeyValue txkv_term_00;
    private TextKeyValue txkv_limit_00;
    private TextKeyValue txkv_payment_00;
    private TextKeyValue txkv_note_00;
    private ListOrder    lsvw_order_00;

    public static Fragment newInstance(String orderid) {
        Fragment frag   = new SalesOrderFragment();
        Bundle args     = new Bundle();
        args.putString("ORDERID", orderid);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view       = inflater.inflate(R.layout.fragment_order_sales, container, false);
        txkv_order_00           = view.findViewById(R.id.txkv_order_00);
        txkv_date_order_00      = view.findViewById(R.id.txkv_date_order_00);
        txkv_outlet_00          = view.findViewById(R.id.txkv_outlet_00);
        lsvw_order_00           = view.findViewById(R.id.lsvw_order_00);
        txkv_date_order_01      = view.findViewById(R.id.txkv_date_order_01);
        txkv_term_00            = view.findViewById(R.id.txkv_term_00);
        txkv_limit_00           = view.findViewById(R.id.txkv_limit_00);
        txkv_payment_00         = view.findViewById(R.id.txkv_payment_00);
        txkv_note_00            = view.findViewById(R.id.txkv_note_00);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txkv_order_00.setKey("No. Order");
        txkv_date_order_00.setKey("Tgl. Order");
        txkv_date_order_01.setKey("Tgl. Order");
        txkv_outlet_00.setKey("Outlet");
        txkv_term_00.setKey("Term of Payment");
        txkv_limit_00.setKey("Credit Limit");
        txkv_payment_00.setKey("Cara Pembayaran");
        txkv_note_00.setKey("Keterangan");


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    private void initData() {
        OutletDB outletDB = new OutletDB();
        outletDB.getMine(getActivity());

        OrderDB orderDB = new OrderDB();
        Log.d(TAG, "ORDER ID "+getArguments().getString("ORDERID"));
        orderDB.getOrder(getActivity(), getArguments().getString("ORDERID"));

        txkv_order_00.setValue(getArguments().getString("ORDERID"));
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format2 = new SimpleDateFormat("dd MMM YYYY");
        try {
            calendar.setTime(format1.parse(orderDB.created_at));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        txkv_date_order_00.setValue(format2.format(calendar.getTime()));
        txkv_date_order_01.setValue(format2.format(calendar.getTime()));
        txkv_outlet_00.setValue(outletDB.outlet_name);
        txkv_term_00.setValue("7 Hari");
        txkv_limit_00.setValue("Rp. 2.000.000");
        txkv_payment_00.setValue(orderDB.order_pay_type);
        txkv_note_00.setValue(orderDB.order_notes);
        Log.d(TAG, orderDB.order_detail);

        try {
            ArrayList<OrderPayment> orders = new ArrayList<>();
            JSONArray data_detail = new JSONArray(orderDB.order_detail);
            for (int i=0; i<data_detail.length(); i++){
                JSONObject data     = data_detail.getJSONObject(i);
                ProductDB productDB = new ProductDB();
                productDB.getProduct(getActivity(),data.getString("product_id"));

                OrderPayment order  = new OrderPayment();
                order.product       = productDB.brand;
                order.description   = productDB.name;
                order.qty           = data.getInt("product_qty");
                order.price         = data.getLong("product_selling_price");
                orders.add(order);
            }
            lsvw_order_00.create(orders);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}
