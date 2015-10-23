package de.keutel_weisz.sap_menus_palo_alto;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    Button myButton;
    ListView menuView;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main, container, false);

        menuView = (ListView) view.findViewById(R.id.fragment_main_lv_menu);

        String[] menuEntries = {"Burger", "Panini", "Salad"};
        MenuAdapter adapter = new MenuAdapter(getContext(), menuEntries);

        menuView.setAdapter(adapter);

        return view;
    }
}
