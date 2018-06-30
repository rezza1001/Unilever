package g.rezza.moch.unileverapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import g.rezza.moch.unileverapp.connection.PostManager;
import g.rezza.moch.unileverapp.database.BrandDB;
import g.rezza.moch.unileverapp.database.ChartDB;
import g.rezza.moch.unileverapp.database.OrderDB;
import g.rezza.moch.unileverapp.database.OutletDB;
import g.rezza.moch.unileverapp.fragment.HomeFragment;
import g.rezza.moch.unileverapp.lib.ErrorCode;
import g.rezza.moch.unileverapp.lib.LoadingScreen;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private ImageView   imvw_menu_00;
    private RelativeLayout rvly_cart_00;
    private TextView    txvw_store_00;
    private TextView    txvw_counter_00;
    private TextView    txvw_description_00;
    private FrameLayout container;
    private OutletDB    outletDB;
    private LoadingScreen loadingScreen;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imvw_menu_00  = (ImageView) findViewById(R.id.imvw_menu_00);
        rvly_cart_00  = (RelativeLayout) findViewById(R.id.rvly_cart_00);
        txvw_counter_00 = (TextView) findViewById(R.id.txvw_counter_00) ;
        container       = (FrameLayout)  findViewById(R.id.container);
        outletDB        = new OutletDB();
        loadingScreen   = new LoadingScreen(this);
        outletDB.getMine(this);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


         navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerLayout   = navigationView.getHeaderView(0);
        txvw_store_00        = (TextView) headerLayout.findViewById(R.id.txvw_store_00);

        txvw_store_00.setText(outletDB.outlet_name);

        navigationView.setNavigationItemSelectedListener(this);
        onNavigationItemSelected(navigationView.getMenu().getItem(0));

        ColorStateList myColorStateList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_pressed},
                        new int[]{}
                },
                new int[] {
                        getResources().getColor(R.color.colorPrimary),
                        getResources().getColor(R.color.colorPrimaryDark)
                }
        );
        navigationView.setItemIconTintList(myColorStateList);


        imvw_menu_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        rvly_cart_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ChartActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        inQuiry();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        onNavigationItemSelected(navigationView.getMenu().getItem(0));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("HOME", "RESUME");
//        onNavigationItemSelected(navigationView.getMenu().getItem(0));

        ChartDB chartDBCounter = new ChartDB();
        ArrayList<ChartDB> carts = chartDBCounter.getProducts(this);
        txvw_counter_00.setText(""+carts.size());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            getSupportActionBar().setTitle("");
            fragment = HomeFragment.newInstance(R.id.menu_home);
            fragmentTransaction.replace(container.getId(), fragment,"Home");
        }
        else if (id == R.id.nav_invoice){
            getSupportActionBar().setTitle("");
            fragment = HomeFragment.newInstance(HomeFragment.INVOICE);
            fragmentTransaction.replace(container.getId(), fragment,"Home");
        }
        else if (id == R.id.nav_myorder) {
            getSupportActionBar().setTitle("");
            fragment = HomeFragment.newInstance(HomeFragment.PRODUCT_ORDER);
            fragmentTransaction.replace(container.getId(), fragment,"Home");
        }
        else if (id == R.id.nav_profile) {
            getSupportActionBar().setTitle("");
            fragment = HomeFragment.newInstance(HomeFragment.PROFILE);
            fragmentTransaction.replace(container.getId(), fragment,"Home");
        }
        else if (id == R.id.nav_logout) {
            loadingScreen.setMessage("Please Wait...");
            loadingScreen.show();
            handler.sendEmptyMessageDelayed(-99, 1000);
        }

        fragmentTransaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what){
                case -99:
                    logout();
                    break;
            }
            return false;
        }
    });

    private void inQuiry(){

        PostManager post = new PostManager(this);
        post.setApiUrl("Product_Brand/inquiry");
        post.execute("GET");
        post.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code, String message) {

                if (code == ErrorCode.OK){
                    try {
                        BrandDB DB = new BrandDB();
                        DB.clearData(MainActivity.this);
                        JSONArray data = obj.getJSONArray("data");
                        for (int i=0; i<data.length(); i++){
                            JSONObject brand = data.getJSONObject(i);
                            DB.id    = brand.getString("brand_id");
                            DB.name  = brand.getString("brand_name");
                            DB.image = brand.getString("brand_image");
                            DB.status = brand.getString("brand_status");
                            DB.insert(MainActivity.this);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(MainActivity.this, message,Toast.LENGTH_SHORT).show();
                }

                onNavigationItemSelected(navigationView.getMenu().getItem(0));
            }

        });


    }



    private void logout(){
        loadingScreen.dimiss();
        OutletDB outletDB = new OutletDB();
        outletDB.clearData(this);

        ChartDB chartDB = new ChartDB();
        chartDB.clearData(this);

        OrderDB orderDB = new OrderDB();
        orderDB.clearData(this);

        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);

        this.finish();
    }
}
