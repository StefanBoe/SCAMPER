package com.example.steveboo.scamper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.util.ArrayList;


public class IdeaActivity extends ActionBarActivity implements OnClickListener {
    RelativeLayout helpOverlay;
    RelativeLayout mainRelativeLayout;
    TextView textView;
    private ArrayAdapter<String> listAdapter;
    HelpOverlay help;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idea);


        Button btn_new = (Button) findViewById(R.id.btn_new);
        btn_new.setOnClickListener(this);
        helpOverlay = (RelativeLayout) findViewById(R.id.help_overlay);
        helpOverlay.setVisibility(View.INVISIBLE);
        mainRelativeLayout = (RelativeLayout) findViewById(R.id.mainRelativeLayout);
        textView = (TextView) findViewById(R.id.helpText);
        help = new HelpOverlay(helpOverlay);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_idea, menu);
        return true;
    }

    //onResume überschrieben, sodass bei jedem restart der Activity fillList() ausgeführt wird
    @Override
    protected void onResume() {
        super.onResume();
        fillList();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //Starte Settings activity bei klick auf Settings Action Bar Item
        if (id == R.id.action_settings) {
            Intent intentSettings=new Intent(this, SettingsActivity.class);
            startActivity(intentSettings);
        }

        //Zeigt das Hilfe Fenster bei klick auf Hilfe Action Bar Item
        if (id == R.id.action_help) {
            textView.setText(getText(R.string.tf_help_idea));
            help.helpOverlay(getApplicationContext());
        }

        return super.onOptionsItemSelected(item);
    }
    // Um das helpOverlay auszublenden
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //HelpOverlay ausblenden falls sichtbar
        help.setInvisible(getApplicationContext());
        return true;
    }

    //Funktion zum füllen der Liste
    public void fillList(){

        IdeaStore store = new IdeaStore();
        final ArrayList<String> ideaList = new ArrayList<String>();

        //Liste der Ideen laden
        ArrayList<Idea> list = store.load();

        //Liste mit Namen der Ideen füllen
        for(int i=0; i<store.load().size();i++){
            ideaList.add(list.get(i).getName());
        }

        //einen Adapter für ListView setzen damit die Items in der Liste geklickt werden können
        final ListView ideaListView = (ListView)findViewById(R.id.ideaList);
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ideaList);
        ideaListView.setAdapter(listAdapter);
        ideaListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Bei click auf bestimmte Position IdeaOverviewActivity starten
                Object listItem = ideaListView.getItemAtPosition(position);
                Intent intentOverview;
                intentOverview = new Intent(view.getContext(), IdeaOverviewActivity.class);
                //Namen der Idee als Intent extra übergeben damit der Name in der nächsten Activity bekannt ist
                intentOverview.putExtra("name", listItem.toString());
                //Activity starten
                startActivity(intentOverview);

            }
        });

        //Kontextmenü für diese Liste hinzufügen
        registerForContextMenu(ideaListView);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.ideaList) {
                //Menüpunkt Löschen einfügen, an der position 0, mit der id 0
                menu.add(Menu.NONE,0,0,"Löschen");
                //Menüpunkt Bearbeiten einfügen an der position 1, mit der id 1
                menu.add(Menu.NONE,1,1,"Bearbeiten");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        final ListView ideaListView = (ListView)findViewById(R.id.ideaList);
        Object listItem = ideaListView.getItemAtPosition(info.position);

        switch(item.getItemId()) {
            //erster Punkt des Menüs
            case 0:
                //Dialog um den Listenpunkt zu löschen
                deleteDialog(listItem.toString());
                return true;

            //zweiter Punkt des Menüs
            //Problemstellung bearbeiten
            case 1:

                Intent intentOverview;
                intentOverview = new Intent(getApplicationContext(), IdeaOverviewActivity.class);
                //Namen der Idee als Intent extra übergeben damit der Name in der nächsten Activity bekannt ist
                intentOverview.putExtra("name", listItem.toString());
                //Activity starten
                startActivity(intentOverview);

                return true;

            //return im Fehlerfall
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void deleteDialog(final String deleteName) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        //Einstellungen des Dialoges
        alertDialog
                .setCancelable(false)
                        //Nein option für den dialog
                .setNegativeButton("Nein", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                        //Ja option für den Dialog
                .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        IdeaStore store = new IdeaStore();
                        //Idee mit dem übergebenen Namen löschen
                        store.remove(deleteName);
                        //Benachrichtigung nachdem gelöscht wurde
                        Toast.makeText(getApplicationContext(),"Problemstellung gelöscht!",Toast.LENGTH_LONG).show();
                        fillList();
                    }
                });
        //Nachricht des Dialoges erstellen
        alertDialog.setMessage("Problemstellung löschen?");
        //Titel des Dialoges erstellen
        alertDialog.setTitle("Scamper");
        //Dialog anzeigen
        alertDialog.show();
    }
    @Override
    public void onClick(View v) {
        //Wenn der Button gedrückt wird MainActivity starten
        Intent intentStart=new Intent(this, MainActivity.class);
        startActivity(intentStart);
    }
}
