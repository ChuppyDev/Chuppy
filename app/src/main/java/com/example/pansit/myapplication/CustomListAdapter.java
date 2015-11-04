package com.example.pansit.myapplication;

/**
 * Created by Pansit on 7/21/2015.
 */
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<Food> foodItems;


    public CustomListAdapter(Activity activity, ArrayList<Food> foodItems) {
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
            convertView = inflater.inflate(R.layout.list_row, null);

        ImageView thumbNail = (ImageView) convertView.findViewById(R.id.thumbnail);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView restaurant = (TextView) convertView.findViewById(R.id.restaurant);
        TextView genre = (TextView) convertView.findViewById(R.id.genre);
        TextView year = (TextView) convertView.findViewById(R.id.calories);

        // getting movie data for the row
        Food m = foodItems.get(position);

        // thumbnail image
        switch (m.getType()) {
            case "fast food":
                thumbNail.setImageResource(R.drawable.food_fastfood);
                break;
            case "Meat":
                thumbNail.setImageResource(R.drawable.food_meat);
                break;
            case "Fruit":
                thumbNail.setImageResource(R.drawable.food_fruit);
                break;
            case "Alcohol":
                thumbNail.setImageResource(R.drawable.food_alcohol);
                break;
            case "Noodles":
                thumbNail.setImageResource(R.drawable.food_noodles);
                break;
            case "noodle":
                thumbNail.setImageResource(R.drawable.food_noodles);
                break;
            default:
                thumbNail.setImageResource(R.drawable.food_meat);
                break;
        }

        // title
        title.setText(m.getName());

        // restaurant
        restaurant.setText("Restaurant: " + m.getRestaurant());

        // genre
        genre.setText(m.getType());

        // calories
        year.setText(String.valueOf(m.getCalories()) + "Cal");

        return convertView;
    }

}
