package de.keutel_weisz.sap_menus_palo_alto;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;


public class CafeMenuFragment extends Fragment {
    private final String LOG_TAG = getClass().getSimpleName();
    private int cafeId;

    MenuAdapter adapter;
    ListView menuView;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_cafemenu, container, false);
        menuView = (ListView) rootView.findViewById(R.id.fragment_cafemenu_lv_menu);

        // If menu is available (i.e. after API has been called), fill list accordingly
        MainActivity parent = (MainActivity) getActivity();
        if (parent.backendClient != null) {
            List<MenuItem> items = parent.backendClient.getWeekMenu().get(0).getMenuItemsByCafe(getCafeId());
            updateMenuItems(items);
        }

        return rootView;
    }


    public void updateMenuItems(List<MenuItem> menuItems) {
        Object[] menuItemObjects = menuItems.toArray();
        MenuItem[] menuItemsArray = Arrays.copyOf(menuItemObjects, menuItemObjects.length, MenuItem[].class);
        adapter = new MenuAdapter(getContext(), menuItemsArray);
        menuView.setAdapter(adapter);
    }

    public int getCafeId() {
        return cafeId;
    }

    public void setCafeId(int cafeId) {
        this.cafeId = cafeId;
    }


}