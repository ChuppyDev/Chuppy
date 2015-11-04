package com.example.pansit.myapplication;

/**
 * Created by Pansit on 7/21/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class customgetfood_assit_Adapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<Food> foodItems;


    public customgetfood_assit_Adapter(Activity activity, ArrayList<Food> foodItems) {
        this.activity = activity;
        this.foodItems = foodItems;
    }

    @Override
    public int getCount() {
        return foodItems.size();
    }

    @Override
    public Object getItem(int location) {
        return foodItems.get(location);
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
            convertView = inflater.inflate(R.layout.fooddialog_assit_list_row, null);


        ImageView foodpic = (ImageView) convertView.findViewById(R.id.foodpic_assit_dialog);
        TextView foodname = (TextView) convertView.findViewById(R.id.foodname_assit_dialog);
        TextView foodres = (TextView) convertView.findViewById(R.id.foodres_assit_dialog);
        TextView foodtype = (TextView) convertView.findViewById(R.id.foodtype_assit_dialog);
        TextView foodcal = (TextView) convertView.findViewById(R.id.foodcal_assit_dialog);
        ImageButton foodclose = (ImageButton) convertView.findViewById(R.id.canclefood_assit);


        // getting movie data for the row
        final  Food m = foodItems.get(position);

        // thumbnail image
        foodpic.setImageResource(R.drawable.brown_rice);

        // title
        foodname.setText(m.getName());

        // restaurant
        foodres.setText("Restaurant: " + m.getRestaurant());

        // genre
        foodtype.setText(m.getType());

        // calories
        foodcal.setText(String.valueOf(m.getCalories()) + "Cal");

        foodclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                foodItems.remove(m);
                TabAssit.adapter = new customgetfood_assit_Adapter(activity, foodItems);
                TabAssit.dialogfoodlistview.setAdapter(TabAssit.adapter);

            }});

        return convertView;
    }

}
