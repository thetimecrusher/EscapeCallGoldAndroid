package com.example.keithfawcett.escapecallgoldandroid;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class CallSettings extends AppCompatActivity {

    Context mContext = this;

    int selectedPosition = 0;
    String callerName = "";
    static String voice = "Voice Off";
    Spinner spinner;


    ArrayAdapter<CharSequence> adapter;
    String callerImage = "android.resource://com.example.keithfawcett.escapecallreleasecandidateandroid/" + R.drawable.defaultcallerimage;


    public final static String Extra_Final_Callers_Name = "com.example.keithfawcett.escapecallreleasecandidateandroid.FINAL_CALLERS_NAME";
    public final static String Extra_Set_Timer = "com.example.keithfawcett.escapecallreleasecandidateandroid.SET_TIMER";
    public final static String Extra_Image = "com.example.keithfawcett.escapecallreleasecandidateandroid.IMAGE";
    public final static String Extra_Ringtone = "com.example.keithfawcett.escapecallreleasecandidateandroid.RINGTONE";
    public final static String Extra_Custom_Voice = "com.example.keithfawcett.escapecallreleasecandidateandroid.CUSTOM_VOICE";

    private final int PICK_CONTACT = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_settings);

        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);


        final ListView listView = (ListView) findViewById(R.id.timesList);
        Button startCallButton = (Button) findViewById(R.id.startCallButton);

        spinner = (Spinner) findViewById(R.id.spinner);

        adapter = ArrayAdapter.createFromResource(mContext, R.array.TimersArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0){
                    voice = "Voice Off";
                }else if(i == 1){
                    voice = "Male";
                }else{
                    voice = "Female";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final String[] timerTimers = {"30 seconds", "1 Minute", "5 Minutes", "10 Minutes", "15 Minutes", "20 Minutes", "30 Minutes", "45 Minutes"};
        final int[] timeInSeconds = {30,60,300,600,900,1200,1800,2700,3600};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                mContext,
                android.R.layout.simple_list_item_activated_1,
                timerTimers
        );

        listView.setAdapter(arrayAdapter);

        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        listView.setItemChecked(0,true);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                selectedPosition = i;
            }
        });

        startCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listView.getSelectedItem();
                Intent intent = new Intent(mContext, Timer.class);
                intent.putExtra(Extra_Final_Callers_Name, callerName);
                intent.putExtra(Extra_Set_Timer, timeInSeconds[selectedPosition]);
                intent.putExtra(Extra_Ringtone, "titania");
                intent.putExtra(Extra_Image, callerImage);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_CONTACT){
            if(resultCode == RESULT_OK){
                Uri contactData = data.getData();
                Cursor c = getContentResolver().query(contactData, null, null, null, null);

                if(c.moveToFirst()){
                    String name = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                    callerName = name;
                    Toast.makeText(this, "You've picked:" + name, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
