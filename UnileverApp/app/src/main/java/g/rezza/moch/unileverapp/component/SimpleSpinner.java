package g.rezza.moch.unileverapp.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import g.rezza.moch.unileverapp.R;
import g.rezza.moch.unileverapp.holder.SpinerHolder;

/**
 * Created by rezza on 02/01/18.
 */

public class SimpleSpinner extends RelativeLayout {

    private Spinner spnr_00;
    private ArrayList<SpinerHolder> mList;

    public SimpleSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.component_simple_spinner,this,true);
        spnr_00 = (Spinner) findViewById(R.id.spnr_00);

        spnr_00.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mListener != null){
                    mListener.after();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void setChoosers(ArrayList<SpinerHolder> pList){
        SpinerAdapter customAdapter=new SpinerAdapter(getContext(), pList);
        spnr_00.setAdapter(customAdapter);
        mList = pList;
    }

    public void setTitle(String pText){
//        txvw_title_00.setText(pText);
    }



    public SpinerHolder getValue(){
        SpinerHolder x = (SpinerHolder) spnr_00.getSelectedItem();
        return x;
    }

    public void setValue(String pID){
        for (int i=0; i<mList.size(); i++){
            if (pID.equals(mList.get(i).key)){
                spnr_00.setSelection(i);
            }
        }

    }


    public ChangeListener mListener;
    public void setOnChangeListener(ChangeListener pListener){
        mListener = pListener;
    }
    public interface ChangeListener{
        public void after();
    }

    class SpinerAdapter extends BaseAdapter {

        Context context;
        ArrayList<SpinerHolder> values;
        LayoutInflater inflter;

        public SpinerAdapter(Context applicationContext, ArrayList<SpinerHolder> values) {
            this.context = applicationContext;
            this.values = values;
            inflter = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {
            return values.size();
        }

        @Override
        public Object getItem(int i) {
            return values.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflter.inflate(R.layout.adapter_simple_spinner, null);
            TextView names = (TextView) view.findViewById(R.id.txvw_00);
            names.setText(values.get(i).value);
            return view;
        }

    }
}
