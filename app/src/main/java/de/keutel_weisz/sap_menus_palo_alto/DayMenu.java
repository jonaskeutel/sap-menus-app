package de.keutel_weisz.sap_menus_palo_alto;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * This class represents the menu for a single day.
 * It contains various menu items for each cafe.
 */
public class DayMenu {

    HashMap<Integer, List<MenuItem>> menuItemsByCafe;

    public DayMenu() {
        menuItemsByCafe = new HashMap<>();
    }

    public void setMenuItemsForCafe(List<MenuItem> menuItems, int cafeId){
        menuItemsByCafe.put(cafeId, menuItems);
    }




    public Date getDate() {
        return null;
    }

    public int[] getCafeIds() {
        return null;
    }

    /**
     * Returns all menu items that are available for
     * in a specific cafe.
     *
     * @param cafeId cafe of interest
     * @return list of menu items.
     */
    public List<MenuItem> getMenuItemsByCafe(int cafeId) {
        return menuItemsByCafe.get(cafeId);
    }
}
