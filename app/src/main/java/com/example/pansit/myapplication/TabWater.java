package com.example.pansit.myapplication;



import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.ContextThemeWrapper;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Pansit on 6/2/2015.
 */
public class TabWater extends Fragment {

    Button drinkButton;
    TextView waterPerDay,statusTxt,name,volume,multiplier;
    ImageView rabbit;
    String itemSelected;
    String[] cupsize;
    int count = 1;
    int itemPosition = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //View view = inflater.inflate(R.layout.tab_water,null);
        final Context context = new ContextThemeWrapper(getActivity(), R.style.Theme_AppCompat_Light);//set theme for fragment
        LayoutInflater localInflater = inflater.cloneInContext(context);
        View view = localInflater.inflate(R.layout.tab_water, container, false);
        //View view = inflater.inflate(R.layout.tab_water,container,false);
        rabbit = (ImageView)view.findViewById(R.id.rabbit);
        cupsize = new String[] {"100","150","250" , "600" , "400"};
        drinkButton = (Button)view.findViewById(R.id.drinkButton);
        waterPerDay = (TextView)view.findViewById(R.id.waterPerDayTxt);
        statusTxt = (TextView)view.findViewById(R.id.statusTxt);
        final DataKeeper data =  ((NewHome)getActivity()).getData();

        waterPerDay.setText(data.getWaterConsumed() + "/" + data.getWaterPerDay());
        if(data.getWaterConsumed() < 0.25 * data.getWaterPerDay()){
            Drawable myDrawable = getResources().getDrawable(R.drawable.rabbit_water_no);
            rabbit.setImageDrawable(myDrawable);
            statusTxt.setText("Status : Low");
        }
        else if(data.getWaterConsumed() < 0.75 * data.getWaterPerDay()){
            Drawable myDrawable = getResources().getDrawable(R.drawable.rabbit_water_half);
            rabbit.setImageDrawable(myDrawable);
            statusTxt.setText("Status : Medium");
        }
        else if(data.getWaterConsumed() <= data.getWaterPerDay()){
            Drawable myDrawable = getResources().getDrawable(R.drawable.rabbit_water_full);
            rabbit.setImageDrawable(myDrawable);
            statusTxt.setText("Status : Full");
        }
        else{
            Drawable myDrawable = getResources().getDrawable(R.drawable.rabbit_water_over);
            rabbit.setImageDrawable(myDrawable);
            statusTxt.setText("Status : Over");
        }
        drinkButton.setOnClickListener(new View.OnClickListener() {




            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                count = 1;
                dialog.setContentView(R.layout.dialog_drinkwater);

                name = (TextView)dialog.findViewById(R.id.waterTxt);

                volume = (TextView)dialog.findViewById(R.id.volumeTxt);
                multiplier = (TextView)dialog.findViewById(R.id.multiplierTxt);
                Button confirm = (Button)dialog.findViewById(R.id.confirm_button);
                Button close = (Button)dialog.findViewById(R.id.button_close_water);
                name.setText("Water glass");


                ViewPager viewPager = (ViewPager)dialog.findViewById(R.id.view_pager);
                ImagePagerAdapter adapter = new ImagePagerAdapter();
                viewPager.setAdapter(adapter);
                //noinspection deprecation
                viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        multiplier.setText("");
                        count = 1;
                    }

                    @Override
                    public void onPageSelected(int position) {
                        volume.setText(cupsize[position]);
                        itemPosition = position;
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });

                        /*
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        count++;
                        multiplier.setText("x" + count);
                    }
                });
*/
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(((NewHome)getActivity()).achievements.get(3).isActive() && ((NewHome)getActivity()).achievements.get(3).addValue()){
                            showToast("\"FIRST DRINK WATER\" Achievement Unlocked");
                            data.addexp(30);
                        }
                        data.addWaterConsumed(count * Integer.parseInt(cupsize[itemPosition]));
                        if(data.waterNeedPerDay() >= 0) {
                            waterPerDay.setText(data.getWaterConsumed() + "/" + data.getWaterPerDay());
                        }
                        else{
                            statusTxt.setText("Status : Full");
                            if(((NewHome)getActivity()).achievements.get(7).isActive() && ((NewHome)getActivity()).achievements.get(7).addValue()){
                                showToast("\"FIRST WATER IN DAY\" Achievement Unlocked");
                                data.addexp(10);
                            }
                            waterPerDay.setText(data.getWaterConsumed() + "/" + data.getWaterPerDay());
                        }
                        ((NewHome) getActivity()).setData(data);
                        if(count == 1){
                            showToast("You drink water " + cupsize[itemPosition] + "ml.");
                        }
                        else{
                            showToast("You drink water " + cupsize[itemPosition] + "ml. x" + count);
                        }
                        if(data.getWaterConsumed() < 0.25 * data.getWaterPerDay()){
                            Drawable myDrawable = getResources().getDrawable(R.drawable.rabbit_water_no);
                            rabbit.setImageDrawable(myDrawable);
                            statusTxt.setText("Status : Low");
                        }
                        else if(data.getWaterConsumed() < 0.75 * data.getWaterPerDay()){
                            Drawable myDrawable = getResources().getDrawable(R.drawable.rabbit_water_half);
                            rabbit.setImageDrawable(myDrawable);
                            statusTxt.setText("Status : Medium");
                        }
                        else if(data.getWaterConsumed() <= data.getWaterPerDay()){
                            Drawable myDrawable = getResources().getDrawable(R.drawable.rabbit_water_full);
                            rabbit.setImageDrawable(myDrawable);
                            statusTxt.setText("Status : Full");
                        }
                        else{
                            Drawable myDrawable = getResources().getDrawable(R.drawable.rabbit_water_over);
                            rabbit.setImageDrawable(myDrawable);
                            statusTxt.setText("Status : Over");
                        }

                        dialog.dismiss();
                    }
                });

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });
                dialog.show();
                /*
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());

                alertDialog.setTitle("Choose size of cup");
                alertDialog.setSingleChoiceItems(cupsize, position, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        itemSelected = cupsize[which];
                        position = which;
                    }

                });
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        data.addWaterConsumed(200 * (position+1));
                        if(data.waterNeedPerDay() >= 0) {
                            waterPerDay.setText(data.getWaterConsumed() + "/" + data.getWaterPerDay());
                        }
                        else{
                            data.setWaterIsReach();
                            statusTxt.setText("Status : Full");
                            if(((NewHome)getActivity()).achievements.get(0).isActive() && ((NewHome)getActivity()).achievements.get(0).addValue()){
                                showToast("\"FIRST WATER IN DAY\" Achievement Unlocked");
                            }
                            waterPerDay.setText(data.getWaterConsumed() + "/" + data.getWaterPerDay());
                        }
                        ((NewHome) getActivity()).setData(data);
                        showToast("You drink water " + (200 * (position+1)) + " ml.");
                        dialog.dismiss();


                    }
                });
                alertDialog.setNegativeButton("Cancel", null);
                alertDialog.create();
                alertDialog.show();
*/

            }

        });

        return view;

    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((NewHome) activity).onSectionAttached(3);
    }
    public void showToast(CharSequence text){
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }
    private class ImagePagerAdapter extends PagerAdapter {
        private int[] mImages = new int[] {
                R.drawable.cone,
                R.drawable.glass150,
                R.drawable.glass250,
                R.drawable.bottle600,
                R.drawable.freesizeglass
        };

        @Override
        public int getCount() {
            return mImages.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {

            return view == ((ImageView) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            Context context = new ContextThemeWrapper(getActivity(), R.style.Theme_AppCompat_Light);
            ImageView imageView = new ImageView(context);
            int padding = context.getResources().getDimensionPixelSize(
                    R.dimen.padding_medium);
            imageView.setPadding(padding, padding, padding, padding);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setImageResource(mImages[position]);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    count++;
                    multiplier.setText("x" + count);

                }
            });
            ((ViewPager) container).addView(imageView, 0);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            count=1;
            multiplier.setText("");
            ((ViewPager) container).removeView((ImageView) object);
        }

    }
}
