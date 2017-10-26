package com.example.keithfawcett.escapecallgoldandroid;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.keithfawcett.escapecallgoldandroid.database.DataSource;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    private final int REQUEST_TAKE_PICTURE = 423;

    ListView settingsList;
    ImageButton addImageButton;
    Button saveButton;
    Button loadButton;
    Button startCall;

    String callerName = "";
    String callerNumber = "";
    int callerTimeInSeconds = 30;
    String callerImage = "";
    String callerRingTone = "";
    String callerTime = "";
    String customVoice = "";


    EditText nameEditText;
    EditText numberEditText;

    ArrayAdapter<String> arrayAdapter;

    String[] settings = new String[]{"Ringtone", "Set Timer", "Voice"};

    private static final int REQUEST_READ_PHOTO_PERMISSION = 400;

    private boolean permissionAccepted = false;
    private String [] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_READ_PHOTO_PERMISSION:
                permissionAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionAccepted ) finish();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_caller);

        mDataSource = new DataSource(this);
        mDataSource.open();


        ActivityCompat.requestPermissions(this, permissions, REQUEST_READ_PHOTO_PERMISSION);

        nameEditText = (EditText) findViewById(R.id.callerNameEditText);
        numberEditText = (EditText) findViewById(R.id.callerNumberEditText);
        numberEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        settingsList = (ListView) findViewById(R.id.settingsList);
        addImageButton = (ImageButton) findViewById(R.id.addImageButton);
        saveButton = (Button) findViewById(R.id.saveButton);
        loadButton = (Button) findViewById(R.id.loadButton);
        startCall = (Button) findViewById(R.id.startCustomCallButton);


        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder picture_choice = new AlertDialog.Builder(CustomCaller.this);
                picture_choice.setMessage("Would you like to choose a picture or take a picture?")
                        .setCancelable(false)
                        .setPositiveButton("Take Picture", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                // Ensure that there's a camera activity to handle the intent
                                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                                    // Create the File where the photo should go
                                    File photoFile = null;
                                    try {
                                        photoFile = createImageFile();
                                    } catch (IOException ex) {
                                        // Error occurred while creating the File
                                        Log.d("Saving Problem", "onOptionsItemSelected: ");
                                    }
                                    // Continue only if the File was successfully created
                                    if (photoFile != null) {
                                        Uri photoURI = FileProvider.getUriForFile(mContext,
                                                "com.example.android.fileprovider",
                                                photoFile);
                                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                        startActivityForResult(cameraIntent, REQUEST_TAKE_PICTURE);
                                    }
                                }
                            }
                        })
                        .setNegativeButton("Choose Picture", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Select Contact Image"), 5);
                            }
                        });

            AlertDialog alert = picture_choice.create();
                alert.show();



            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(nameEditText.getText().toString().equals("") || numberEditText.getText().toString().equals("")){
                    Toast.makeText(mContext, "Fill in all fields", Toast.LENGTH_SHORT).show();
                }else {
                    callerName = nameEditText.getText().toString();
                    callerNumber = numberEditText.getText().toString();
                    Callers deleteCaller = new Callers (null,callerName, callerNumber,callerTimeInSeconds, callerTime, callerRingTone, customVoice, callerImage);
                    mDataSource.deleteCaller(deleteCaller);
                    Callers myNewCaller = new Callers(null,callerName, callerNumber, callerTimeInSeconds, callerTime, callerRingTone, customVoice, callerImage);
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
                if(nameEditText.getText().toString().equals("") || numberEditText.getText().toString().equals("")){
                    Toast.makeText(mContext, "Fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    String callerImageString = callerImage.toString();
                    callerName = nameEditText.getText().toString();
                    callerNumber = numberEditText.getText().toString();
                    Intent intent = new Intent(mContext, Timer.class);
                    intent.putExtra(CallSettings.Extra_Final_Callers_Name, callerName);
                    intent.putExtra(CallSettings.Extra_Callers_Number, callerNumber);
                    intent.putExtra(CallSettings.Extra_Set_Timer, callerTimeInSeconds);
                    intent.putExtra(CallSettings.Extra_Image, callerImageString);
                    intent.putExtra(CallSettings.Extra_Ringtone, callerRingTone);
                    intent.putExtra(CallSettings.Extra_Custom_Voice, customVoice);

                    startActivity(intent);
                }
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
                callerNumber = callerInfo.getCallerNumber();
                callerTimeInSeconds = callerInfo.getCallerTimeCounter();
                callerImage = callerInfo.getCallerImage();
                nameEditText.setText(callerName);
                numberEditText.setText(callerNumber);
                customVoice = callerInfo.getCallerVoice();
                addImageButton.setImageURI(Uri.parse(callerImage));
                Toast.makeText(mContext,callerInfo.getCallerName(),Toast.LENGTH_SHORT).show();
            }
            else if(reqCode == 5){
                Log.d("Image data", data.getDataString());
                callerImage = data.getDataString();
                addImageButton.setImageURI(Uri.parse(callerImage));
            }else if(reqCode == REQUEST_TAKE_PICTURE && reqCode != RESULT_CANCELED) {
                if(callerImage != null) {

                    addImageButton.setImageBitmap(BitmapFactory.decodeFile(callerImage));

                    //  mImageView.setImageBitmap(BitmapFactory.decodeFile(mImageUri.getPath()));
                } else {
                    // Bitmap thumb = (Bitmap) data.getParcelableExtra("data");
                    addImageButton.setImageBitmap((Bitmap)data.getParcelableExtra("data"));
                }
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        callerImage = image.getAbsolutePath();
        return image;
    }
}
