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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import g.rezza.moch.unileverapp.R;
import g.rezza.moch.unileverapp.adapter.ProductOrderAdapter;
import g.rezza.moch.unileverapp.connection.PostManager;
import g.rezza.moch.unileverapp.database.ChartDB;
import g.rezza.moch.unileverapp.database.OrderDB;
import g.rezza.moch.unileverapp.database.OutletDB;
import g.rezza.moch.unileverapp.database.ProductDB;
import g.rezza.moch.unileverapp.holder.MyOrderHolder;
import g.rezza.moch.unileverapp.lib.ErrorCode;
import g.rezza.moch.unileverapp.lib.Parse;

/**
 * Created by rezza on 09/02/18.
 */

public class ProductOrderFragment extends Fragment {
    private static final String TAG = "ProductOrderFragment";
    private ListView                    lsvw_order_00;
    private ProductOrderAdapter         adapter;
    private ArrayList<MyOrderHolder>    list = new ArrayList<>();
    private Button                      bbtn_action_00;
    private TextView                    txvw_total_00;
    private TextView                    txvw_empty_00;
    private RelativeLayout              rvly_bottom_00;

    public static Fragment newInstance() {
        Fragment frag   = new ProductOrderFragment();
        Bundle args     = new Bundle();
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view       = inflater.inflate(R.layout.fragment_product_order, container, false);
        lsvw_order_00   = view.findViewById(R.id.lsvw_order_00);
        adapter         = new ProductOrderAdapter(getActivity(),list);
        bbtn_action_00  = view.findViewById(R.id.bbtn_action_00);
        txvw_total_00   = view.findViewById(R.id.txvw_total_00);
        txvw_empty_00   = view.findViewById(R.id.txvw_empty_00);
        rvly_bottom_00   = view.findViewById(R.id.rvly_bottom_00);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lsvw_order_00.setAdapter(adapter);
        txvw_empty_00.setVisibility(View.GONE);
        initData();
        initListener();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void initListener(){
        bbtn_action_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData();
            }
        });

        adapter.setOOnChangeQtyListener(new ProductOrderAdapter.OnChangeQtyListener() {
            @Override
            public void OnSelect(MyOrderHolder product, int position) {
                ChartDB chartDB = new ChartDB();
                chartDB.getProduct(getActivity(), product.product);
                chartDB.qty = product.qty;
                chartDB.update(getActivity(), " "+ ChartDB.FIELD_PRODUCT_ID+" = '"+product.product+"'");
                processTotal();
            }
        });

        adapter.setAfterDeleteListener(new ProductOrderAdapter.afterDeleteListener() {
            @Override
            public void afterChange(MyOrderHolder event, int position) {
                list.remove(position);
                adapter.notifyDataSetChanged();
                processTotal();
            }
        });
    }
    private void initData(){
        ChartDB chartDB = new ChartDB();
        ArrayList<ChartDB> charts = chartDB.getProducts(getActivity());
        for (ChartDB chart: charts){
            ProductDB productDB = new ProductDB();
            productDB.getProduct(getActivity(),chart.product_id );
            MyOrderHolder holder = new MyOrderHolder();
            holder.product      = chart.product_id;
            holder.description  = productDB.name;
            holder.img_url      = productDB.photo_link+"/"+productDB.photo;
            holder.qty          = chart.qty;
            holder.price        = productDB.selling_price;
            holder.max_qty      = 10;
            list.add(holder);

        }
        adapter.notifyDataSetChanged();
        processTotal();
    }

    private void processTotal(){
        ChartDB chartDB = new ChartDB();
        ArrayList<ChartDB> charts = chartDB.getProducts(getActivity());
        if (charts.size() == 0){
            txvw_empty_00.setVisibility(View.VISIBLE);
            rvly_bottom_00.setVisibility(View.GONE);
        }
        else {
            txvw_empty_00.setVisibility(View.GONE);
        }
        long total = 0;
        for (ChartDB chart: charts){
            ProductDB productDB = new ProductDB();
            productDB.getProduct(getActivity(),chart.product_id );
            long price = chart.qty * productDB.selling_price;
            total = total + price;
        }
        txvw_total_00.setText("Rp. "+ Parse.toCurrnecy(total+""));
    }

    private void sendData(){
        PostManager post = new PostManager(getActivity());
        post.setApiUrl("purchase/order");
        JSONObject send = new JSONObject();
        try {
            OutletDB outletDB = new OutletDB();
            outletDB.getMine(getActivity());

            send.put("request_type","5");
            JSONObject data = new JSONObject();

            JSONObject order_info = new JSONObject();
            order_info.put("username", outletDB.username);
            order_info.put("outlet_id", outletDB.outlet_id);
            order_info.put("order_disc", "0");
            order_info.put("order_pay_type", "Bank Transfer");
            order_info.put("order_notes", "");
            data.put("order_info",order_info);

            JSONArray product_info = new JSONArray(getDetialOrder());
            data.put("product_info", product_info);

            send.put("data",data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        post.setData(send);
        post.execute("POST");
        post.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code, String message) {
                if (code == ErrorCode.OK){
                    OrderDB orderDB = new OrderDB();
                    orderDB.order_pay_type = "Bank Transfer";
                    orderDB.order_detail = getDetialOrder();
                    try {
                        orderDB.id          = obj.getString("order_id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    orderDB.insert(getActivity());

                    ChartDB chartDB = new ChartDB();
                    chartDB.clearData(getActivity());

                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    Fragment fragment = SalesOrderFragment.newInstance(orderDB.id);
                    fragmentTransaction.replace(ProductOrderFragment.this.getId(), fragment,"sales_order");
                    fragmentTransaction.detach(fragment);
                    fragmentTransaction.attach(fragment);
                    fragmentTransaction.commit();
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String getDetialOrder(){
        ChartDB chartDB = new ChartDB();
        ArrayList<ChartDB> charts = chartDB.getProducts(getActivity());
        JSONArray product_info = new JSONArray();
        for (ChartDB chart: charts){
            ProductDB productDB = new ProductDB();
            productDB.getProduct(getActivity(),chart.product_id );
            JSONObject product_item  = new JSONObject();
            try {
                product_item.put("product_id", productDB.id);
                product_item.put("product_qty", chart.qty);
                product_item.put("product_pricelist", productDB.pricelist);
                product_item.put("product_discount", productDB.discount);
                product_item.put("product_selling_price", productDB.selling_price);
                product_info.put(product_item);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return product_info.toString();
    }

}
