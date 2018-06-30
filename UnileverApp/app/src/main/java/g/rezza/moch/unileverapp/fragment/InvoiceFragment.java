package g.rezza.moch.unileverapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import g.rezza.moch.unileverapp.R;
import g.rezza.moch.unileverapp.adapter.InvoiceAdapter;
import g.rezza.moch.unileverapp.component.ListOrder;
import g.rezza.moch.unileverapp.component.TextKeyValue;
import g.rezza.moch.unileverapp.database.OrderDB;
import g.rezza.moch.unileverapp.database.OutletDB;
import g.rezza.moch.unileverapp.database.ProductDB;
import g.rezza.moch.unileverapp.holder.InvoiceOrderHolder;
import g.rezza.moch.unileverapp.holder.OrderPayment;

/**
 * Created by rezza on 09/02/18.
 */

public class InvoiceFragment extends Fragment {
    private static final String TAG = "InvoiceFragment";

    private ListView lsvw_invoice_00;
    private InvoiceAdapter adapter;
    private ArrayList<InvoiceOrderHolder> list = new ArrayList<>();
    private TextView txvw_empty_00;

    public static Fragment newInstance() {
        Fragment frag   = new InvoiceFragment();
        Bundle args     = new Bundle();
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view       = inflater.inflate(R.layout.fragment_invoice, container, false);
        lsvw_invoice_00 = view.findViewById(R.id.lsvw_invoice_00);
        txvw_empty_00   = view.findViewById(R.id.txvw_empty_00);
        adapter         = new InvoiceAdapter(getActivity(), list);
        lsvw_invoice_00.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txvw_empty_00.setVisibility(View.GONE);
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
        OrderDB orderDB = new OrderDB();
        ArrayList<OrderDB> orders = orderDB.getOrders(getActivity());
        for (OrderDB order:orders){
            InvoiceOrderHolder holder = new InvoiceOrderHolder();
            holder.id = order.id;
            holder.date = order.created_at;
            list.add(holder);
        }
        if (orders.size()  == 0){
            txvw_empty_00.setVisibility(View.VISIBLE);
        }
        adapter.notifyDataSetChanged();


    }

}
