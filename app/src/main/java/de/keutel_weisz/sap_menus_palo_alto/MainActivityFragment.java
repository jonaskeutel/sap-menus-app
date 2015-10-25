package de.keutel_weisz.sap_menus_palo_alto;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    ListView menuView;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main, container, false);

        menuView = (ListView) view.findViewById(R.id.fragment_main_lv_menu);

        //String[] menuEntries = {"Burger", "Panini", "Salad"};
        //MenuAdapter adapter = new MenuAdapter(getContext(), menuEntries);

        //menuView.setAdapter(adapter);

        refreshMenu();

        return view;
    }

    public void refreshMenu() {
        new ApiCaller().execute("today");
    }

    private class ApiCaller extends AsyncTask<String, Void, List<DayMenu>> {
        private static final String TAG = "ApiCaller";

        @Override
        public List<DayMenu> doInBackground(String... s) {

            String urlString = "http://217.160.126.98:3000/" + s[0];

            Log.d(TAG, urlString);

            HttpURLConnection urlConnection;
            URL url;
            JSONObject object;
            String[] result;

            MenuBackendClient backendClient = new MenuBackendClient();
            List<DayMenu> weekMenu = backendClient.getWeekMenu();


            return weekMenu;
        }

        public void onPostExecute(List<DayMenu> res) {
            if (res.isEmpty()) {
                Toast.makeText(getContext(), "Couldn't get menu. Weekend!", Toast.LENGTH_LONG).show();
                return;
            }
            MenuItem[] items = Arrays.copyOf(res.get(0).getMenuItemsByCafe(1).toArray(), res.get(0).getMenuItemsByCafe(1).toArray().length, MenuItem[].class);
            MenuAdapter adapter = new MenuAdapter(getContext(), items);
            menuView.setAdapter(adapter);
        }
    }
}
