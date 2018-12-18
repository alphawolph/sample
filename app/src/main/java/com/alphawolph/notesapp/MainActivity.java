package com.alphawolph.notesapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> notesArray;
    static ArrayAdapter arrayAdapter;
    SharedPreferences sharedPreferences;
    HashSet<String> set;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getApplicationContext().getSharedPreferences("com.alphawolph.notesapp", Context.MODE_PRIVATE);

        notesArray = new ArrayList<>();
        ListView notes = findViewById(R.id.notesList);

        set = (HashSet<String>) sharedPreferences.getStringSet("notes", null);

        if(set == null)
        {
            notesArray.add("First Note");
        }
        else
        {
            notesArray = new ArrayList(set);
        }




        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notesArray);
        notes.setAdapter(arrayAdapter);

        notes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), addNotes.class);
                intent.putExtra("noteId",position);
                startActivity(intent);
            }
        });

        notes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final int delete = position;

                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("ARE YOU SURE?")
                        .setMessage("DO YOU REALLY WANT TO DELETE THE NOTE?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                set.remove(delete);
                                notesArray.remove(delete);
                                arrayAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("NO", null)
                        .show();

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.alphawolph.notesapp", Context.MODE_PRIVATE);
                HashSet<String> set = new HashSet<>(MainActivity.notesArray);
                sharedPreferences.edit().putStringSet("notes", set).apply();


                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.addNote)
        {
            Intent intent = new Intent(getApplicationContext(), addNotes.class);
            startActivity(intent);
            return true;

        }
        return false;


    }

}
