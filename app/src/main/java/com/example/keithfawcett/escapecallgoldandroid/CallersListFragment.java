package com.example.keithfawcett.escapecallgoldandroid;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class CallersListFragment extends ListFragment {

    public static ArrayAdapter<Callers> adapter;

    interface CallersListListener{
        void itemClicked(int id);
        void itemLongClick(int id);
    }

    private CallersListListener listener;



    public CallersListFragment(){

    }

    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {

                listener.itemLongClick(arg2);
                adapter.notifyDataSetChanged();
                Toast.makeText(getActivity(), "On long click listener" + arg2, Toast.LENGTH_LONG).show();

                return true;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        adapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                LoadContact.callers
        );

        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (CallersListListener) activity;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if(listener != null){

            listener.itemClicked(position);
        }
    }





}
