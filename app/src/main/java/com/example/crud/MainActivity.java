package com.example.crud;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    AlertDialog.Builder dialog;
    FloatingActionButton fab;
    List<Data> itemList = new ArrayList<Data>();
    Adapter adapter;
    Button btn_add;
    DbHelper SQLite = new DbHelper(this);
    public  static  final String TAG_ID = "id";
    public static  final String TAG_NAME = "name";
    public  static final String TAG_ADDRESS = "address";
    @Override
    protected  void  onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SQLite = new DbHelper(getApplicationContext());
        listView = findViewById(R.id.list_view);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddEdit.class);
                startActivity(intent);
            }
        });
        adapter = new Adapter(MainActivity.this, itemList);
        listView.setAdapter(adapter);
        //long press list view to show edit and delete
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                //TODO Auto-Generated Method Stub
                final String idx = itemList.get(position).getId();
                final String name = itemList.get(position).getName();
                final String address = itemList.get(position).getAddress();
                final CharSequence[] dialogitem = {"Edit", "Delete"};
                dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setCancelable(true);
                dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-Generated Method Stub
                        switch (which){
                            case 0:
                                Intent intent = new Intent(MainActivity.this, AddEdit.class);
                                intent.putExtra(TAG_ID, idx);
                                intent.putExtra(TAG_NAME, name);
                                intent.putExtra(TAG_ADDRESS, address);
                                startActivity(intent);
                                break;

                            case 1:
                                SQLite.delete(Integer.parseInt(idx));
                                itemList.clear();
                                getAllData();
                                break;
                        }
                    }
                }).show();
                return false;
            }
        });
        getAllData();
    }
    private void getAllData(){
        ArrayList<HashMap<String, String>> row = SQLite.getAllData();
        for (int i = 0; i < row.size(); i++){
            String id = row.get(i).get(TAG_ID);
            String name = row.get(i).get(TAG_NAME);
            String address = row.get(i).get(TAG_ADDRESS);
            Data data = new Data(id, name, address);
            data.setId(id);
            data.setName(name);
            data.setAddress(address);
            itemList.add(data);
        }
        adapter.notifyDataSetChanged();
    }
    @Override
    protected void onResume(){
        super.onResume();
        itemList.clear();
        getAllData();
    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        //Inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();
        if (id == R.id.action_settings){
        }
        return super.onOptionsItemSelected(item);
    }
}