package de.keutel_weisz.sap_menus_palo_alto;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class CafeMenuFragment extends Fragment {
    private final String LOG_TAG = getClass().getSimpleName();
    private int cafeId;

    MenuAdapter adapter;
    ExpandableListView menuView;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_cafemenu, container, false);
        menuView = (ExpandableListView) rootView.findViewById(R.id.fragment_cafemenu_lv_menu);

        // If menu is available (i.e. after API has been called), fill list
        MainActivity parent = (MainActivity) getActivity();
        if(parent.backendClient.getWeekMenu() != null ) {
            // TODO deal with different methods (week vs. today vs. tomorrow)
            HashMap<String,List<MenuItem>> categories = parent.backendClient.getWeekMenu().get(0).getCategoriesForCafe(cafeId);
            updateMenuItems(categories);
        }

        return rootView;
    }





    public void updateMenuItems (HashMap<String, List<MenuItem>> categories) {

        List<String> categoryLabels = new ArrayList<>();
        for(String key : categories.keySet()) {
            categoryLabels.add(key);
        }

        adapter = new MenuAdapter(getContext(), categoryLabels, categories);
        menuView.setAdapter(adapter);
    }

    public int getCafeId() {
        return cafeId;
    }

    public void setCafeId(int cafeId) {
        this.cafeId = cafeId;
    }


}