package g.rezza.moch.unileverapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import g.rezza.moch.unileverapp.R;
import g.rezza.moch.unileverapp.fragment.home.HomeMainFragment;
import g.rezza.moch.unileverapp.fragment.home.InvoiceFragment;
import g.rezza.moch.unileverapp.fragment.home.MyOrderFragment;
import g.rezza.moch.unileverapp.fragment.home.ProfileFragment;
import it.sephiroth.android.library.bottomnavigation.BottomNavigation;

/**
 * Created by rezza on 09/02/18.
 */

public class HomeFragment extends Fragment implements BottomNavigation.OnMenuItemSelectionListener {
    private static final String TAG = "HomeFragment";
    private BottomNavigation navigation;
    private FrameLayout container;

    public static final int PRODUCT_ORDER   = R.id.menu_myorder;
    public static final int INVOICE         = R.id.menu_invoice;
    public static final int PROFILE         = R.id.menu_profile;

    public static Fragment newInstance(int position) {
        Fragment frag   = new HomeFragment();
        Bundle args     = new Bundle();
        args.putInt("MENU", position);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup pcontainer, Bundle savedInstanceState) {
        View view       = inflater.inflate(R.layout.fragment_home, pcontainer, false);
        navigation      = (BottomNavigation) view.findViewById(R.id.navigation);
        this.container  = (FrameLayout)      view.findViewById(R.id.container_00);
        navigation.setOnMenuItemClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated "+ getArguments().getInt("MENU",0));
        onMenuItemSelect(getArguments().getInt("MENU"),  0, true);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    private void clearFrg(){
        if (getActivity().getSupportFragmentManager().getFragments() != null && getActivity().getSupportFragmentManager().getFragments().size() > 0) {
            for (int i = 0; i < getActivity().getSupportFragmentManager().getFragments().size(); i++) {
                Fragment mFragment = getActivity().getSupportFragmentManager().getFragments().get(i);
                if (mFragment != null) {
                    getActivity().getSupportFragmentManager().beginTransaction().remove(mFragment).commit();
                }
            }
        }
    }

    @Override
    public void onMenuItemSelect(int i, int i1, boolean b) {
        Log.d(TAG, "MENU "+ i);
        Fragment fragment = null;
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

        switch (i){
            case R.id.menu_home:
                navigation.setSelectedIndex(0, true);
                fragment = HomeMainFragment.newInstance();
                fragmentTransaction.replace(container.getId(), fragment,"main_home");
                break;
            case R.id.menu_myorder:
                navigation.setSelectedIndex(1, true);
                fragment = MyOrderFragment.newInstance();
                fragmentTransaction.replace(container.getId(), fragment,"my_order");
                break;
            case R.id.menu_invoice:
                navigation.setSelectedIndex(2, true);
                fragment = InvoiceFragment.newInstance();
                fragmentTransaction.replace(container.getId(), fragment,"invoice");
                break;
            case R.id.menu_profile:
                navigation.setSelectedIndex(3, true);
                fragment = ProfileFragment.newInstance();
                fragmentTransaction.replace(container.getId(), fragment,"main_profile");
                break;

            default:
//                clearFrg();
                fragment = HomeMainFragment.newInstance();
                fragmentTransaction.replace(container.getId(), fragment,"main_default");
                break;
        }
        fragmentTransaction.detach(fragment);
        fragmentTransaction.attach(fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onMenuItemReselect(int i, int i1, boolean b) {

    }
}
