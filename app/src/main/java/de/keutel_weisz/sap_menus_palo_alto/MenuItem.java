package de.keutel_weisz.sap_menus_palo_alto;

/**
 * This class represents a single concrete menu item, i.e. a meal
 * such  as "Spaghetti Bolognese with Meatballs". It contains
 * detailed information such as a description and a set of attributes
 * defining the properties of the meal (e.g. vegetarian, no lactose, etc.)
 */
public class MenuItem {

    private String label;
    private String description;

    public MenuItem(String label, String description) {
        this.label = label;
        this.description = description;

    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }
}
