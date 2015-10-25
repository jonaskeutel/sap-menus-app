package de.keutel_weisz.sap_menus_palo_alto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MenuAdapter extends ArrayAdapter<MenuItem> {

    private final Context context;
    private final MenuItem[] items;

    public MenuAdapter(Context context, MenuItem[] items) {
        super(context, -1, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View menuEntryView = inflater.inflate(R.layout.fragment_main_menu_entry, parent, false);
        TextView menuHeadingView = (TextView) menuEntryView.findViewById(R.id.fragment_main_tv_menu_heading);

        MenuItem item = items[position];

        menuHeadingView.setText(item.getLabel());
        return menuEntryView;
    }
}
