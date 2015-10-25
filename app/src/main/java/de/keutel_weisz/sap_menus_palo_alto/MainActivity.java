package de.keutel_weisz.sap_menus_palo_alto;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    static final String LOG_TAG = "MAIN_ACTIVITY";

    final int[] CAFE_IDS = {1, 3, 8};
    final int NUM_CAFES = CAFE_IDS.length;

    FloatingActionButton refreshButton;

    CafeMenuPagerAdapter cafeMenuPagerAdapter;
    ViewPager cafeMenuViewPager;

    MenuBackendClient backendClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        refreshButton = (FloatingActionButton) findViewById(R.id.activity_main_btn_refresh);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                (new ApiCallerTask()).execute("today");
            }
        });


        cafeMenuPagerAdapter = new CafeMenuPagerAdapter(getSupportFragmentManager());
        cafeMenuViewPager = (ViewPager) findViewById(R.id.activity_main_cafe_menu_viewpager);
        cafeMenuViewPager.setAdapter(cafeMenuPagerAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, "Mein erster Toast!", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class CafeMenuPagerAdapter extends FragmentPagerAdapter {

        CafeMenuFragment[] fragments;

        public CafeMenuPagerAdapter(FragmentManager fm) {
            super(fm);
            fragments = new CafeMenuFragment[NUM_CAFES];

        }

        @Override
        public int getCount() {
            return NUM_CAFES;
        }

        @Override
        public Fragment getItem(int position) {
            CafeMenuFragment fragment = new CafeMenuFragment();
            fragment.setCafeId(CAFE_IDS[position]);
            return fragment;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            CafeMenuFragment cafeMenuFragment = (CafeMenuFragment) super.instantiateItem(container, position);

            // Keep reference of newly instantiated fragment in array to make it accessible later
            fragments[position] = cafeMenuFragment;

            return cafeMenuFragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Café " + CAFE_IDS[position];
        }

    }


    private class ApiCallerTask extends AsyncTask<String, Void, List<DayMenu>> {
        private static final String TAG = "ApiCaller";

        @Override
        public List<DayMenu> doInBackground(String... s) {
            backendClient = new MenuBackendClient();
            List<DayMenu> weekMenu = backendClient.getWeekMenu();


            return weekMenu;
        }

        public void onPostExecute(List<DayMenu> res) {
            if (res.isEmpty()) {
                Toast.makeText(MainActivity.this, "Couldn't get menu. Weekend!", Toast.LENGTH_LONG).show();
                return;
            }

            for (int cafeIndex = 0; cafeIndex < NUM_CAFES; cafeIndex++) {
                CafeMenuFragment cafeFragment = cafeMenuPagerAdapter.fragments[cafeIndex];
                if (cafeFragment != null) {
                    cafeFragment.updateMenuItems(
                            res.get(0).getMenuItemsByCafe(CAFE_IDS[cafeIndex]));
                }
            }

        }

    }
}
