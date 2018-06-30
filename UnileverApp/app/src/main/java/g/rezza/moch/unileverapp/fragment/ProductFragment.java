package g.rezza.moch.unileverapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import g.rezza.moch.unileverapp.R;
import g.rezza.moch.unileverapp.Static.App;
import g.rezza.moch.unileverapp.adapter.ProductGridAdapter;
import g.rezza.moch.unileverapp.adapter.ProductListAdapter;
import g.rezza.moch.unileverapp.adapter.ProductOrderAdapter;
import g.rezza.moch.unileverapp.connection.PostManager;
import g.rezza.moch.unileverapp.database.ChartDB;
import g.rezza.moch.unileverapp.database.ProductDB;
import g.rezza.moch.unileverapp.holder.MyOrderHolder;
import g.rezza.moch.unileverapp.holder.ProductHolder;
import g.rezza.moch.unileverapp.lib.ErrorCode;

/**
 * Created by rezza on 09/02/18.
 */

public class ProductFragment extends Fragment {
    private static final String TAG = "ProductFragment";

    private ListView lsvw_product_00;
    private GridView grvw_product_00;
    private ProductListAdapter adapter ;
    private ProductGridAdapter adapter_grid;
    private ArrayList<ProductHolder> list = new ArrayList<>();
    private LinearLayout    bbtn_list_00;
    private ImageView       imvw_option_00;
    private TextView        txvw_option_00;

    private int option = App.MENU_GRID;

    public static Fragment newInstance() {
        Fragment frag   = new ProductFragment();
        Bundle args     = new Bundle();
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view       = inflater.inflate(R.layout.fragment_product, container, false);
        lsvw_product_00 = view.findViewById(R.id.lsvw_product_00);
        adapter         = new ProductListAdapter(getActivity(), list);
        adapter_grid    = new ProductGridAdapter(getActivity(), list);
        bbtn_list_00    = view.findViewById(R.id.bbtn_list_00);
        imvw_option_00  = view.findViewById(R.id.imvw_option_00);
        txvw_option_00  = view.findViewById(R.id.txvw_option_00);
        grvw_product_00 = view.findViewById(R.id.grvw_product_00);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lsvw_product_00.setAdapter(adapter);
        grvw_product_00.setAdapter(adapter_grid);
        initListener();
        initData();
        switchOption();
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
        bbtn_list_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (option == App.MENU_LIST){
                    option = App.MENU_GRID;
                }
                else {
                    option = App.MENU_LIST;
                }
                switchOption();
            }
        });

        adapter_grid.setOnSelectedListener(new ProductGridAdapter.OnSelectedListener() {
            @Override
            public void OnSelect(ProductHolder event, int position) {
                ChartDB chartDB = new ChartDB();
                if (event.inChart == 1){
                    chartDB.deleteData(getActivity(),event.product_id);
                    list.get(position).inChart = 0;
                }
                else {
                    chartDB.product_id = event.product_id;
                    chartDB.insert(getContext());
                    list.get(position).inChart = 1;
                }

                adapter_grid.notifyDataSetChanged();
                adapter.notifyDataSetChanged();
                Log.d(TAG,"IN Chart : "+chartDB.getProducts(getContext()).size());
            }
        });

        adapter.setOnSelectedListener(new ProductListAdapter.OnSelectedListener() {
            @Override
            public void OnSelect(ProductHolder event, int position) {
                ChartDB chartDB = new ChartDB();
                if (event.inChart == 1){
                    chartDB.deleteData(getActivity(),event.product_id);
                    list.get(position).inChart = 0;
                }
                else {
                    chartDB.product_id = event.product_id;
                    chartDB.insert(getContext());
                    list.get(position).inChart = 1;
                }

                adapter.notifyDataSetChanged();
                adapter_grid.notifyDataSetChanged();
                Log.d(TAG,"IN Chart : "+chartDB.getProducts(getContext()).size());
            }
        });
    }

    private void switchOption(){
        if (option == App.MENU_LIST){
            grvw_product_00.setVisibility(View.GONE);
            lsvw_product_00.setVisibility(View.VISIBLE);
            txvw_option_00.setText("GRID");
            imvw_option_00.setImageResource(R.drawable.ic_grid);
        }
        else {
            grvw_product_00.setVisibility(View.VISIBLE);
            lsvw_product_00.setVisibility(View.GONE);
            txvw_option_00.setText("LIST");
            imvw_option_00.setImageResource(R.drawable.ic_list);
        }
    }

    private void initData(){

        PostManager post = new PostManager(getActivity());
        post.setApiUrl("product/inquiry");
        post.execute("GET");
        post.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code, String message) {

                if (code == ErrorCode.OK){
                    try {
                        ProductDB DB = new ProductDB();
                        DB.clearData(getActivity());
                        JSONArray data = obj.getJSONArray("data");
                        for (int i=0; i<data.length(); i++){
                            JSONObject prod = data.getJSONObject(i);
                            ProductDB productDB = new ProductDB();
                            productDB.id            = prod.getString("product_id");
                            productDB.category_l1   = prod.getString("category_level_1");
                            productDB.category_l2   = prod.getString("category_level_2");
                            productDB.category_l3   = prod.getString("category_level_3");
                            productDB.brand         = prod.getString("brand_name");
                            productDB.brand_id      = prod.getString("brand_id");
                            productDB.sku           = prod.getString("product_sku");
                            productDB.name          = prod.getString("product_name");
                            productDB.pricelist     = prod.getLong("product_pricelist");
                            productDB.selling_price = prod.getLong("selling_price");
                            productDB.discount      = prod.getDouble("product_discount");
                            productDB.photo_link    = prod.getString("product_photo_link");
                            productDB.photo         = prod.getString("product_photo");
                            productDB.insert(getActivity());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                ProductDB productDB = new ProductDB();
                ArrayList<ProductDB> products = productDB.getProducts(getActivity());
                for(ProductDB prod: products){
                    ProductHolder holder = new ProductHolder();
                    holder.product_id    = prod.id;
                    holder.description   = prod.name;
                    holder.product       = prod.brand;
                    holder.img_url       = prod.photo_link+"/"+prod.photo;
                    holder.price         = prod.selling_price;

                    ChartDB chartDB = new ChartDB();
                    chartDB.getProduct(getContext(),holder.product_id);
                    if (chartDB.product_id.isEmpty()){
                        holder.inChart = 0;
                    }
                    else {
                        holder.inChart = 1;
                    }

                    list.add(holder);
                }
                adapter.notifyDataSetChanged();
                adapter_grid.notifyDataSetChanged();
            }
        });

    }

}
