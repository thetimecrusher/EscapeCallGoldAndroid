package com.example.keithfawcett.escapecallgoldandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.example.keithfawcett.escapecallgoldandroid.database.DataSource;

import java.util.ArrayList;
import java.util.List;

public class LoadContact extends AppCompatActivity implements CallersListFragment.CallersListListener {

    public static final ArrayList<Callers> callers = new ArrayList<>();

    private DataSource mDataSource;
    Context mContext = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_contact);

        mDataSource = new DataSource(this);
        mDataSource.open();

        List<Callers> loadArray = mDataSource.getAllItems();


        callers.clear();
        for(int i = 0; i < loadArray.size(); i++){
            callers.add(loadArray.get(i));
            CallersListFragment.adapter.notifyDataSetChanged();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }



    @Override
    protected void onPause() {
        super.onPause();
        mDataSource.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDataSource.open();
    }

    @Override
    public void itemClicked(int position) {
        Intent intent = new Intent();
        intent.putExtra(CustomCaller.Callers_Extras, callers.get(position));
            setResult(RESULT_OK, intent);
        finish();


    }

    @Override
    public void itemLongClick(int id) {
        Callers delete = callers.get(id);
        mDataSource.deleteCaller(delete);


        List<Callers> loadArray = mDataSource.getAllItems();
        callers.clear();
        for(int i = 0; i < loadArray.size(); i++){
            callers.add(loadArray.get(i));
            CallersListFragment.adapter.notifyDataSetChanged();
        }
    }
}
