package g.rezza.moch.unileverapp.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

import g.rezza.moch.unileverapp.ProductActivity;
import g.rezza.moch.unileverapp.R;
import g.rezza.moch.unileverapp.adapter.MenuGridAdapter;
import g.rezza.moch.unileverapp.database.BrandDB;
import g.rezza.moch.unileverapp.holder.MenuGridHolder;
import g.rezza.moch.unileverapp.lib.ScrollableGridView;

/**
 * Created by rezza on 09/02/18.
 */

public class HomeMainFragment extends Fragment {
    private static final String TAG = "HomeMainFragment";

    private ScrollableGridView  grvw_data_10;
    private MenuGridAdapter     adapter;
    private ImageView           imvw_banner_00;
    private ScrollView          scvw_body_00;
    private ArrayList<MenuGridHolder> list = new ArrayList<>();

    public static Fragment newInstance() {
        Fragment frag   = new HomeMainFragment();
        Bundle args     = new Bundle();
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view       = inflater.inflate(R.layout.fragment_home_main, container, false);
        grvw_data_10    = view.findViewById(R.id.grvw_data_10);
        adapter         = new MenuGridAdapter(getActivity(), list);
        imvw_banner_00  = view.findViewById(R.id.imvw_banner_00);
        scvw_body_00    = view.findViewById(R.id.scvw_body_00);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        grvw_data_10.setAdapter(adapter);
        createData();
        scvw_body_00.smoothScrollTo(0,0);
        Glide.with(getActivity()).load("http://202.154.3.188/commerce/production/uploads/AD-0000010.jpeg").into(imvw_banner_00);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerListener();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void createData(){
        BrandDB brandDB = new BrandDB();
        HashMap<String, String> mapBrands = new HashMap<>();
        ArrayList<BrandDB> brands =  brandDB.get(getActivity());
       for (BrandDB brand: brands){
           MenuGridHolder menu = new MenuGridHolder();
           menu.id      = brand.id;
           menu.image   = "http://202.154.3.188/commerce/production/uploads/"+brand.image;
           menu.name    = brand.name;
           list.add(menu);
       }
        adapter.notifyDataSetChanged();

        // SPECIAL

    }

    private void registerListener(){
        adapter.setOnSelectedItemListener(new MenuGridAdapter.OnSelectedItemListener() {
            @Override
            public void selectedItem(MenuGridHolder holder, int position) {
//                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//                Fragment fragment = HomeFragment.newInstance(HomeFragment.PRODUCT);
//                fragmentTransaction.replace(HomeMainFragment.this.getId(), fragment,"sproduct_order");
//                fragmentTransaction.detach(fragment);
//                fragmentTransaction.attach(fragment);
//                fragmentTransaction.commit();

                Intent intent = new Intent(getActivity(), ProductActivity.class);
                intent.putExtra("BRAND_ID", holder.id);
                intent.putExtra("BRAND", holder.name);
                getActivity().startActivity(intent);

            }
        });
    }

}
