package com.example.pansit.myapplication;

/**
 * Created by Pansit on 7/21/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class customdetail_home_Adapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<String> textname;
    private ArrayList<String> textnamecolor;
    private ArrayList<String> textnameval;
    private ArrayList<Integer> intpercen;

    public customdetail_home_Adapter(Activity activity, ArrayList<String> inputtextname, ArrayList<String> inputtextnamecolor, ArrayList<String> inputtextnameval
        ,ArrayList<Integer> inputintpercen    ) {
        this.activity = activity;
        this.textname = inputtextname;
        this.textnamecolor = inputtextnamecolor;
        this.textnameval = inputtextnameval;
        this.intpercen = inputintpercen;

    }

    @Override
    public int getCount() {
        return textname.size();
    }

    @Override
    public Object getItem(int location) {
        return textname.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.detail_of_listview_home, null);

        TextView detailtxt = (TextView) convertView.findViewById(R.id.headtext_listview_home);
        TextView valtxt = (TextView) convertView.findViewById(R.id.text1_listview_home);

        Button colordetail = (Button) convertView.findViewById(R.id.btn1_listview_home);


        final String  textdetail = textname.get(position);
        final String  textval = textnameval.get(position);
        final String  textcolor = textnamecolor.get(position);
        final int   percen = intpercen.get(position);

        if(position==0)
        {
            if(percen >=80 )
            {
                valtxt.setTextColor(Color.parseColor("#FFB01F1B"));

            }
        }
        // thumbnail image
        colordetail.setBackgroundColor(Color.parseColor(textcolor));

        // detailtxt
        detailtxt.setText(textdetail);

        // valtxt
        valtxt.setText(textval);

       // percenttxt.setText(textpercen);

        return convertView;
    }

}
