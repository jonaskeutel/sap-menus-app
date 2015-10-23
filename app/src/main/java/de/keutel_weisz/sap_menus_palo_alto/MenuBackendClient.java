package de.keutel_weisz.sap_menus_palo_alto;

import android.util.Log;

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
import java.util.List;

/**
 * Created by michael on 22/10/15.
 */
public class MenuBackendClient {
    final String LOG_TAG = getClass().getSimpleName();

    final String HOSTNAME = "217.160.126.98";
    final String ENDPOINT_WEEK_MENU = "sap-menus";
    final String ENDPOINT_TODAY_MENU = "today";
    final String ENDPOINT_TOMORROW_MENU = "tomorrow";
    final int PORT = 3000;

    List<DayMenu> weekMenu;

    /**
     * Self Test and Demo
     *
     * @param args command-line args
     */
    public static void main(String args[]) {
        MenuBackendClient client = new MenuBackendClient();

        // Print menu for the entire week
        List<DayMenu> weekMenu = client.getWeekMenu();
        for (DayMenu dayMenu : weekMenu) {
            System.out.println("Date: " + dayMenu.getDate());
            for (int cafeId : dayMenu.getCafeIds()) {
                System.out.println("\tCafe: " + cafeId);
                for (MenuItem menuItem : dayMenu.getMenuItemsByCafe(cafeId)) {
                    System.out.println("\t\t" + menuItem.getName());
                }
            }
        }

    }

    public List<DayMenu> getWeekMenu() {

        // Lazy initialization
        if (weekMenu == null) {
            weekMenu = fetchWeekMenuFromServer();
        }

        return weekMenu;
    }

    private List<DayMenu> fetchWeekMenuFromServer(){
        // Make Call to Backend
        String endpoint = "http://" + HOSTNAME + ":" +  PORT + "/" + ENDPOINT_WEEK_MENU;
        String rawJSON = performGETRequest(endpoint);

        // Parse JSON and populate weekMenu
        List<DayMenu> weekMenu = null;
        if(rawJSON != null) {
            weekMenu = parseJSON(rawJSON);
        }

        return weekMenu;
    }


    private List<DayMenu> parseJSON(String rawJSON) {
        ArrayList<DayMenu> dayMenus = new ArrayList<DayMenu>();
        try {
            JSONObject json = new JSONObject(rawJSON);

//            JSONObject week1JSON = json.getJSONObject("0");
//            JSONObject cafe1JSON = week1JSON.getJSONObject("cafe1");
//            System.out.println(cafe1JSON.getString("0"));

        } catch (JSONException ex) {
        //    Log.e(LOG_TAG, "Error parsing JSON.");
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
            String line = null;
            while ((line = reader.readLine()) != null ) {
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
