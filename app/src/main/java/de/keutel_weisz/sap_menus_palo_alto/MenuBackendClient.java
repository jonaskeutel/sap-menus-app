package de.keutel_weisz.sap_menus_palo_alto;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MenuBackendClient {
    final String LOG_TAG = getClass().getSimpleName();

    final String HOSTNAME = "217.160.126.98";
    final String ENDPOINT_WEEK_MENU = "sap-menus";
    final String ENDPOINT_TODAY_MENU = "today";
    final String ENDPOINT_TOMORROW_MENU = "tomorrow";
    final int PORT = 3000;

    List<DayMenu> weekMenu;

    enum STATUS_CODES {OK, WEEKEND, ERROR }



    public List<DayMenu> getWeekMenu() {

        // Lazy initialization
        if (weekMenu == null) {
            weekMenu = fetchWeekMenuFromServer();
        }

        return weekMenu;
    }

    private List<DayMenu> fetchWeekMenuFromServer() {
        // Make Call to Backend
        String endpoint = "http://" + HOSTNAME + ":" + PORT + "/" + ENDPOINT_TODAY_MENU;
        
        // when testing:
        //String endpoint = "http://" + "192.168.1.236:" + PORT + "/" + ENDPOINT_TODAY_MENU;
        String rawJSON = performGETRequest(endpoint);

        // Parse JSON and populate weekMenu
        List<DayMenu> weekMenu;
        if (rawJSON != null) {
            weekMenu = parseJSON(rawJSON);
        } else { // Server not reachable
            Log.e(LOG_TAG, "Could not get JSON. Server is not available.");
            weekMenu = new ArrayList<>();
        }

        return weekMenu;
    }


    private List<DayMenu> parseJSON(String rawJSON) {

        ArrayList<DayMenu> dayMenus = new ArrayList<>();
        try {
            JSONObject menuDataJSON = new JSONObject(rawJSON);
            JSONArray daysJSON = menuDataJSON.getJSONArray("content");
            int statusCode = menuDataJSON.getInt("statusCode");

            if (statusCode != STATUS_CODES.OK.ordinal()) {
                // TODO: Handle other status codes (e.g. weekend) accordingly
                Log.i(LOG_TAG, "Backend returned with status code: " + statusCode);
                return dayMenus;
            }

            // Parse each day
            for (int dayIndex = 0; dayIndex < daysJSON.length(); dayIndex++) {
                JSONArray dayMenuJSON = daysJSON.getJSONArray(dayIndex);

                DayMenu dayMenu = new DayMenu();

                // Parse each cafe
                for (int cafeIndex = 0; cafeIndex < dayMenuJSON.length(); cafeIndex++) {
                    JSONObject cafeJSON = dayMenuJSON.getJSONObject(cafeIndex);
                    int cafeId = cafeJSON.getInt("cafeId");

                    JSONArray categoriesJSON = cafeJSON.getJSONArray("categories");

                    // Parse each category
                    HashMap<String, List<MenuItem>> categoriesByCafe = new HashMap<>();
                    for (int categoryIndex = 0; categoryIndex < categoriesJSON.length(); categoryIndex++) {
                        JSONObject categoryJSON = categoriesJSON.getJSONObject(categoryIndex);
                        JSONArray menuItemsJSON = categoryJSON.getJSONArray("menuItems");
                        String categoryLabel = categoryJSON.getString("label");


                        // Parse each menuItem
                        List<MenuItem> menuItems = new ArrayList<>();
                        for (int menuItemIndex = 0; menuItemIndex < menuItemsJSON.length(); menuItemIndex++) {
                            JSONObject menuItemJSON = menuItemsJSON.getJSONObject(menuItemIndex);
                            String label = menuItemJSON.getString("label");
                            String description = menuItemJSON.getString("description");
                            MenuItem item = new MenuItem(label, description);
                            menuItems.add(item);
                        }
                        categoriesByCafe.put(categoryLabel, menuItems);
                        dayMenu.setCategoriesForCafe(categoriesByCafe, cafeId);
                    }
                }
                dayMenus.add(dayMenu);
            }


        } catch (JSONException ex) {
            Log.e(LOG_TAG, "Error parsing JSON.");
            ex.printStackTrace();
        }

        return dayMenus;
    }

    private String performGETRequest(String endpoint) {
        StringBuilder output = new StringBuilder();

        URL url;
        try {
            url = new URL(endpoint);
        } catch (MalformedURLException malformedURLException) {
            Log.e(LOG_TAG, "MalformedURLException (" + endpoint + ").");
            return null;
        }

        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }
        } catch (IOException ioException) {
            //Log.e(LOG_TAG, "IOException.");
            ioException.printStackTrace();
            return null;
        } finally {
            urlConnection.disconnect();
        }

        return output.toString();
    }

}
