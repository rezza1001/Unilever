package g.rezza.moch.unileverapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import g.rezza.moch.unileverapp.R;
import g.rezza.moch.unileverapp.holder.MenuGridHolder;

/**
 * Created by Rezza on 11/9/17.
 */

public class MenuGridAdapter extends ArrayAdapter<MenuGridHolder> {
    public static final String TAG = "MenuGridAdapter";

    private ArrayList<MenuGridHolder> holders = new ArrayList<>();
    public static final int RESOURCE = R.layout.adapter_menu_grid;


    public MenuGridAdapter(@NonNull Context context, ArrayList<MenuGridHolder> pElement) {
        super(context, RESOURCE, pElement);
        holders = pElement;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        try {
            if (view == null) {
                view = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(RESOURCE, null);
            }
            ImageView imvw_image_10 = (ImageView)   view.findViewById(R.id.imvw_image_00);

            final MenuGridHolder holder = holders.get(position);
            if (holder != null){
                Glide.with(getContext()).load(holder.image).into(imvw_image_10);
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnSelectedItemListener != null){
                        mOnSelectedItemListener.selectedItem(holder, position);
                    }
                }
            });
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
        }

        return view;
    }

        /*
     * Register Listener
     */

    private OnSelectedItemListener mOnSelectedItemListener;
    public void setOnSelectedItemListener(OnSelectedItemListener pOnSelectedItemListener){
        mOnSelectedItemListener = pOnSelectedItemListener;
    }

    /*
     * Interface Listener
     */

    public interface OnSelectedItemListener{
        public void selectedItem(MenuGridHolder holder, int position);
    }
}
