package com.example.keithfawcett.escapecallgoldandroid;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class CustomCaller extends AppCompatActivity {


    DataSource mDataSource;
    Context mContext = this;

    public final static String Extra_Settings_Picker = "com.example.keithfawcett.escapecallreleasecandidate.SETTINGS_PICKER";

    public final static String Extra_Callers_Name = "com.example.keithfawcett.escapecallreleasecandidate.CALLERS_NAME";
    public final static String Extra_Set_Timer = "com.example.keithfawcett.escapecallreleasecandidate.SET_TIMER";

    public final static String Extra_Set_Timer_Seconds = "com.example.keithfawcett.escapecallreleasecandidate.SET_TIMER_SECONDS";

    public final static String Extra_Ringtone = "com.example.keithfawcett.escapecallreleasecandidate.RINGTONE";
    public final static String ACTION_UPDATE_LIST = "com.example.keithfawcett.escapecallreleasecandidate.ACTION_UPDATE_LIST";
    public final static String EXTRA_RECORDING = "com.example.keithfawcett.escapecallreleasecandidate.EXTRA_RECORDING";


    public static final String Callers_Extras = "com.example.keithfawcett.escapecallreleasecandidate.CALLERS_EXTRAS";

    ListView settingsList;
    ImageButton addImageButton;
    Button saveButton;
    Button loadButton;
    Button startCall;

    String callerName = "";
    int callerTimeInSeconds = 30;
    Uri callerImage = Uri.parse("android.resource://com.example.keithfawcett.escapecallbeta/" + R.drawable.defaultcallerimage);
    String callerRingTone = "";
    String callerTime = "";
    String customVoice = "";


    EditText nameEditText;

    ArrayAdapter<String> arrayAdapter;

    String[] settings = new String[]{"Ringtone", "Set Timer", "Voice"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_caller);

        mDataSource = new DataSource(this);
        mDataSource.open();



        nameEditText = (EditText) findViewById(R.id.callerNameEditText);
        settingsList = (ListView) findViewById(R.id.settingsList);
        addImageButton = (ImageButton) findViewById(R.id.addImageButton);
        saveButton = (Button) findViewById(R.id.saveButton);
        loadButton = (Button) findViewById(R.id.loadButton);
        startCall = (Button) findViewById(R.id.startCustomCallButton);


        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Contact Image"), 5);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(nameEditText.getText().toString().equals("")){
                    Toast.makeText(mContext, "Fill in all fields", Toast.LENGTH_SHORT).show();
                }else {
                    callerName = nameEditText.getText().toString();
                    Callers myNewCaller = new Callers(null,callerName,callerTimeInSeconds, callerTime, callerRingTone, customVoice, callerImage.toString());
                    mDataSource.addCaller(myNewCaller);
                    Intent updateIntent = new Intent(ACTION_UPDATE_LIST);
                    sendBroadcast(updateIntent);
                    Toast.makeText(mContext, "Caller Saved", Toast.LENGTH_SHORT).show();
                }


            }
        });

        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, LoadContact.class);
                startActivityForResult(intent,3);

            }
        });

        startCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String callerImageString = callerImage.toString();
                callerName = nameEditText.getText().toString();
                Intent intent = new Intent(mContext, Timer.class);
                intent.putExtra(CallSettings.Extra_Final_Callers_Name, callerName);
                intent.putExtra(CallSettings.Extra_Set_Timer, callerTimeInSeconds);
                intent.putExtra(CallSettings.Extra_Image, callerImageString);
                intent.putExtra(CallSettings.Extra_Ringtone, callerRingTone);
                intent.putExtra(CallSettings.Extra_Custom_Voice, customVoice);

                startActivity(intent);
            }
        });

        settingsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 2){
                    Intent intent = new Intent(mContext, Recorder.class);
//                    intent.putExtra(Extra_Settings_Picker, i);
                    startActivityForResult(intent, i);
                }else {
                    Intent intent = new Intent(mContext, SelectData.class);
                    intent.putExtra(Extra_Settings_Picker, i);
                    startActivityForResult(intent, i);
                }
            }
        });

        arrayAdapter = new ArrayAdapter<>(
                mContext,
                android.R.layout.simple_list_item_activated_1,
                settings
        );

        settingsList.setAdapter(arrayAdapter);


    }


    public void onActivityResult(int reqCode, int resCode, Intent data){
        if(resCode == RESULT_OK) {
            if(reqCode == 0){
                callerRingTone = data.getStringExtra(Extra_Ringtone);
                Toast.makeText(mContext, callerRingTone + " Selected", Toast.LENGTH_SHORT).show();
            }
            else if (reqCode == 1){
                callerTime = data.getStringExtra(Extra_Set_Timer);
                callerTimeInSeconds = data.getIntExtra(Extra_Set_Timer_Seconds, 30);
                Toast.makeText(mContext, callerTime + " Selected", Toast.LENGTH_SHORT).show();
            }else if (reqCode == 2){
                customVoice = data.getStringExtra(EXTRA_RECORDING);
//                Toast.makeText(mContext, customVoice + " Selected", Toast.LENGTH_SHORT).show();
            }
           else if(reqCode == 3){
                Callers callerInfo = (Callers) data.getSerializableExtra(Callers_Extras);
                callerName = callerInfo.getCallerName();
                callerTimeInSeconds = callerInfo.getCallerTimeCounter();
                callerImage = Uri.parse(callerInfo.getCallerImage());
                nameEditText.setText(callerName);
                customVoice = callerInfo.getCallerVoice();
                addImageButton.setImageURI(callerImage);
                Toast.makeText(mContext,callerInfo.getCallerName(),Toast.LENGTH_SHORT).show();
            }
            else if(reqCode == 5){
                callerImage = data.getData();
                addImageButton.setImageURI(data.getData());
            }
        }
    }
}
