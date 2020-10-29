package com.example.BudgetDatabase;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class LstAdapter extends ArrayAdapter<String>
{
    int groupid;
    String[] item_list;
    ArrayList<String> desc;
    Context context;
    public LstAdapter(Context context, int vg, int id,  String[]  item_list)
    {
        super(context,vg, id, item_list);
        this.context=context;
        groupid=vg;
        this.item_list=item_list;

    }
    // Hold views of the ListView to improve its scrolling performance
    static class ViewHolder
    {
        public TextView textsno;
        public TextView textdate;
        public TextView textitem;
        public TextView textrate;

    }

    public View getView(int position, View convertView, ViewGroup parent)
    {

        View rowView = convertView;
        // Inflate the rowlayout.xml file if convertView is null
        if(rowView==null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView= inflater.inflate(groupid, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.textsno= (TextView) rowView.findViewById(R.id.txtsno);
            viewHolder.textdate= (TextView) rowView.findViewById(R.id.txtdate);
            viewHolder.textitem= (TextView) rowView.findViewById(R.id.txtitem);
            viewHolder.textrate= (TextView) rowView.findViewById(R.id.txtrate);
            rowView.setTag(viewHolder);

        }
        // Set text to each TextView of ListView item
        String[] items=item_list[position].split("__");
        ViewHolder holder = (ViewHolder) rowView.getTag();
        holder.textsno.setText(items[0]);
        holder.textdate.setText(items[1]);
        holder.textitem.setText(items[2]);
        holder.textrate.setText(items[3]);
        return rowView;
    }

}


