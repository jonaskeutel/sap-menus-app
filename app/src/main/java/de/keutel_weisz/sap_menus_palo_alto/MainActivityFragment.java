package de.keutel_weisz.sap_menus_palo_alto;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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

        new ApiCaller().execute("today");

        return view;
    }

    private class ApiCaller extends AsyncTask<String, Void, String[]>{
        private static final String TAG = "ApiCaller";

        @Override
        public String[] doInBackground(String... s) {

            String urlString = "http://217.160.126.98:3000/" + s[0];

            Log.d(TAG, urlString);

            HttpURLConnection urlConnection;
            URL url;
            JSONObject object;
            String[] result;

            try
            {
                url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inStream = urlConnection.getInputStream();
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
                String temp, response = "";
                while ((temp = bReader.readLine()) != null)
                    response += temp;
                bReader.close();
                inStream.close();
                urlConnection.disconnect();
                object = (JSONObject) new JSONTokener(response).nextValue();

                // cafe 8
                JSONObject today = (JSONObject) object.get("0");
                JSONObject cafe8 = (JSONObject) today.get("cafe8");

                result = new String[cafe8.length()];
                for (int i = 0; i < cafe8.length(); i++) {
                    result[i] = (String) cafe8.get("" + i);
                }
            }
            catch (Exception e)
            {
                Log.e(TAG, e.toString());
                return new String[]{e.toString()};
            }


            return result;
        }

        public void onPostExecute(String[] res) {
            MenuAdapter adapter = new MenuAdapter(getContext(), res);
            menuView.setAdapter(adapter);
        }
    }
}
