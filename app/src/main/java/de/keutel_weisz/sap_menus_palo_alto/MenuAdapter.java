package de.keutel_weisz.sap_menus_palo_alto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MenuAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final String[] values;

    public MenuAdapter(Context context, String[] values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View menuEntryView = inflater.inflate(R.layout.fragment_main_menu_entry, parent, false);
        TextView menuHeadingView = (TextView) menuEntryView.findViewById(R.id.fragment_main_tv_menu_heading);

        menuHeadingView.setText(values[position]);
        return menuEntryView;
    }
}
