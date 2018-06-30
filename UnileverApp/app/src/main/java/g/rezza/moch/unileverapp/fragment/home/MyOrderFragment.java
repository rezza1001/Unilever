package g.rezza.moch.unileverapp.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import g.rezza.moch.unileverapp.MainActivity;
import g.rezza.moch.unileverapp.OrderDetailActivity;
import g.rezza.moch.unileverapp.R;
import g.rezza.moch.unileverapp.adapter.InvoiceAdapter;
import g.rezza.moch.unileverapp.adapter.MyOrderAdapter;
import g.rezza.moch.unileverapp.connection.PostManager;
import g.rezza.moch.unileverapp.database.OrderDB;
import g.rezza.moch.unileverapp.database.OutletDB;
import g.rezza.moch.unileverapp.holder.InvoiceOrderHolder;
import g.rezza.moch.unileverapp.holder.ListOrderHolder;
import g.rezza.moch.unileverapp.lib.ErrorCode;

/**
 * Created by rezza on 09/02/18.
 */

public class MyOrderFragment extends Fragment {
    private static final String TAG = "MyOrderFragment";

    private ListView lsvw_invoice_00;
    private MyOrderAdapter adapter;
    private ArrayList<ListOrderHolder> list = new ArrayList<>();
    private TextView txvw_empty_00;
    private OutletDB outletDB;

    public static Fragment newInstance() {
        Fragment frag   = new MyOrderFragment();
        Bundle args     = new Bundle();
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view       = inflater.inflate(R.layout.fragment_order_history, container, false);
        lsvw_invoice_00 = view.findViewById(R.id.lsvw_invoice_00);
        txvw_empty_00   = view.findViewById(R.id.txvw_empty_00);
        adapter         = new MyOrderAdapter(getActivity(), list);
        lsvw_invoice_00.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txvw_empty_00.setVisibility(View.GONE);
        outletDB = new OutletDB();
        outletDB.getMine(getActivity());
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
        PostManager post = new PostManager(getActivity());
        post.setApiUrl("Order/list/outletId/"+outletDB.outlet_id);
        post.execute("GET");
        post.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code, String message) {
                if (code == ErrorCode.OK){
                    JSONArray data = null;
                    try {
                        data = obj.getJSONArray("data");
                        if (data.length() == 0){
                            txvw_empty_00.setVisibility(View.VISIBLE);
                        }
                        for (int i=0; i<data.length(); i++){
                            JSONObject history = data.getJSONObject(i);
                            ListOrderHolder holder = new ListOrderHolder();
                            holder.order_id     = history.getString("order_id");
                            holder.order_date   = history.getString("order_date");
                            holder.order_payment_type   =history.getString("order_payment_type");
                            holder.note       = history.getString("order_note");
                            holder.discount   = history.getString("order_discount");
                            holder.total   = history.getString("total");
                            holder.status   = history.getString("order_status");
                            list.add(holder);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else {
                    txvw_empty_00.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
            }
        });


        adapter.setOnSelectedListener(new MyOrderAdapter.OnSelectedListener() {
            @Override
            public void OnSelect(ListOrderHolder event, int position) {
                Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                intent.putExtra("ORDER_ID",event.order_id);
                intent.putExtra("ORDER_DATE",event.order_date);
                intent.putExtra("PAYMENT",event.order_payment_type);
                intent.putExtra("DISCOUNT",event.discount);
                intent.putExtra("NOTE",event.note);
                startActivity(intent);
            }
        });

    }

}
