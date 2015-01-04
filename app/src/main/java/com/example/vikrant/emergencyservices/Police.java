package com.example.vikrant.emergencyservices;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;


public class Police extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_police, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this,Settings.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private static final String PREFS_NAME="CONTACTS";
        private String numberOne,numberTwo,numberThree;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_police, container, false);

            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
            numberOne=settings.getString(getString(R.string.numberOne_key),getString(R.string.default_value));
            numberTwo=settings.getString(getString(R.string.numberTwo_key),getString(R.string.default_value));
            numberThree=settings.getString(getString(R.string.numberThree_key),getString(R.string.default_value));
            String message="The sender has invoked"+
                    " hospital emergency";
            String numbers=numberOne+';'+numberTwo+';'+numberThree;

            //Call button
            Button police_call = (Button) rootView.findViewById(R.id.police_call);
            police_call.setOnClickListener(new View.OnClickListener(){
                @Override
            public void onClick(View v){
                    Uri number = Uri.parse("tel:100");
                    startActivity(new Intent(Intent.ACTION_CALL,number));
                }
            });

            //Map button
            Button police_map = (Button) rootView.findViewById(R.id.police_map);
            police_map.setOnClickListener(new View.OnClickListener(){
                @Override
            public void onClick(View v){
                    Uri location = Uri.parse("geo:0,0?q=Police");
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);

                    //Verify the intent resolves
                    PackageManager packageManager = v.getContext().getPackageManager();
                    List<ResolveInfo> activities = packageManager.queryIntentActivities(mapIntent, 0);
                    boolean isIntentSafe = activities.size() > 0;

                    // Start an activity if it's safe
                    if (isIntentSafe) {
                        startActivity(mapIntent);
                    }
                }
            });

            //Message button
            Button police_message = (Button) rootView.findViewById(R.id.police_message);
            police_message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    try {
                        String numbers = numberOne+';'+numberTwo+';'+numberThree;
                        String message = "The sender has invoked"+
                                " police emergency";
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(numbers, null, message, null, null);
                        Toast.makeText(v.getContext(), "SMS Sent!",
                                Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(v.getContext(),
                                "SMS failed, please try again later!",
                                Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            });

            return rootView;
        }
    }
}
