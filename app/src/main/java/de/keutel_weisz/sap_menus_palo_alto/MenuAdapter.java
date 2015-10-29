package de.keutel_weisz.sap_menus_palo_alto;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;


public class MenuAdapter extends BaseExpandableListAdapter {

    private final Context context;

    private List<String> categoryLabels;
    private HashMap<String, List<MenuItem>> listChildData;

    public MenuAdapter(Context context, List<String> categoryLabels,
                       HashMap<String, List<MenuItem>> listChildData) {
        this.context = context;
        this.categoryLabels = categoryLabels;
        this.listChildData = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listChildData.get(this.categoryLabels.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final MenuItem menuItem = (MenuItem) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fragment_main_menu_entry, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.fragment_main_tv_menu_heading);

        txtListChild.setText(menuItem.getLabel());
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listChildData.get(this.categoryLabels.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return categoryLabels.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listChildData.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fragment_cafemenu_listview_header, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
