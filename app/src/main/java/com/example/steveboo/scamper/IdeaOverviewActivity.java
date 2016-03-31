package com.example.steveboo.scamper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class IdeaOverviewActivity extends ActionBarActivity implements OnClickListener{

    private ArrayAdapter<String> listOverviewAdapter ;
    IdeaStore store = new IdeaStore();
    String selectedQuestion="";
    String selectedAnswer="";
    String passedName="";
    RelativeLayout helpOverlay;
    RelativeLayout mainRelativeLayout;
    TextView textView;
    HelpOverlay help;
    Button btn_start_again;
    Idea idea;
    int index;
    //sub test
    static final ArrayList<HashMap<String,String>> list =
            new ArrayList<HashMap<String,String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idea_overview);
        Button btn_idea_delete = (Button)findViewById(R.id.btn_delete);
        btn_idea_delete.setOnClickListener(this);
        btn_start_again = (Button) findViewById(R.id.btn_start_again);
        btn_start_again.setOnClickListener(this);
        helpOverlay = (RelativeLayout) findViewById(R.id.help_overlay);
        helpOverlay.setVisibility(View.INVISIBLE);
        mainRelativeLayout = (RelativeLayout) findViewById(R.id.mainRelativeLayout);
        textView = (TextView) findViewById(R.id.helpText);
        help = new HelpOverlay(helpOverlay);

        //Übergebenen Namen aus der letzten Activity erhalten
        passedName = getIntent().getStringExtra("name");



    }

    //Funktion für delete Dialog
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
                        //zurück zur letzten Activity
                        onBackPressed();
                        //Aktuelle Activity beenden damit man nicht wieder in diese Acitivty reinspringen kann
                        finish();
                        //Benachrichtigung nachdem gelöscht wurde
                        Toast.makeText(getApplicationContext(),"Problemstellung gelöscht!",Toast.LENGTH_LONG).show();

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_idea_overview, menu);
        return true;
    }



    //onResume überschrieben, sodass bei jedem restart der Activity fillOverviewList() ausgeführt wird
    @Override
    protected void onResume() {
        super.onResume();
        fillOverviewList();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //Einstellungs Activity starten bei click auf Settings Symbol in der action bar
        if (id == R.id.action_settings) {
            Intent intentSettings=new Intent(this, SettingsActivity.class);
            startActivity(intentSettings);
        }

        //Zeigt das Hilfe Fenster bei klick auf Hilfe Action Bar Item
        if (id == R.id.action_help) {
            HelpOverlay help = new HelpOverlay(helpOverlay);
            textView.setText(getText(R.string.tf_help_idea_overview));
            help.helpOverlay(getApplicationContext());
        }
        return super.onOptionsItemSelected(item);
    }
    // Um das helpOverlay auszublenden
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //HelpOverlay ausblenden falls sichtbar
        help.setInvisible(this);
        return true;
    }

    //Funktion um die Liste zu füllen
    public void fillOverviewList(){

        //Liste leeren falls noch etwas darin steht
        list.clear();
        final ListView ideaOverviewListView = (ListView)findViewById(R.id.ideaOverviewList);
        //Idee mit dem übergebenen Namen erhalten
        idea = store.getIdea(passedName);

        if(idea.isDone()){
            btn_start_again.setText("Alle Fragen beantwortet");
            btn_start_again.setBackgroundResource(R.drawable.btn_grey_selector);
            btn_start_again.setOnClickListener(null);
        }

        TextView textViewIdeaName = (TextView)findViewById(R.id.textViewIdeaName);
        //Namen der Idee anzeigen lassen
        textViewIdeaName.setText(passedName);

            //Zeigt Keine Frage und Keine Antwort an wenn die Idee keine Antworten und Fragen enthält
            if(idea.getSize()==0) {
                //für die Aktuelle Idee wird Keine Frage und Keine Antwort als Frage und Antwortpaar erstellt
                //damit es im weiteren Verlauf nicht zu fehlern kommt
                idea.addpair("Keine Frage", "Keine Antwort");
            }


        //Liste füllen
          for(int i=0 ; i<idea.getSize(); i++){
                HashMap<String,String> temp = new HashMap<String,String>();
                temp.put("question", idea.getQuestion(i));
                temp.put("answer", idea.getAnswer(i));
                list.add(temp);
            }
        //Adapter der Liste.
        SimpleAdapter adapter = new SimpleAdapter(
                this,
                list,
                R.layout.custom_row_view,
                new String[] {"question","answer"},
                new int[] {R.id.text1,R.id.text2}
        );
        //adapter und listener um mit der Liste interagieren zu können
        ideaOverviewListView.setAdapter(adapter);
        ideaOverviewListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //falls die Idee leer ist (Wird durch Keine Antwort und Keine Fragedargestellt)
                if (list.get(0).values().contains("Keine Antwort")) {
                    //Toast anzeigen lassen, sodass der Nutzer weiß das die Frage nicht bearbeitet werden kann
                    Toast.makeText(getApplicationContext(), "Leere Frage kann nicht bearbeitet werden", Toast.LENGTH_LONG).show();
                } else {

                startEdit(position);
                }
            }
        });

        //Kontextmenü für diese Liste hinzufügen
        registerForContextMenu(ideaOverviewListView);

    }

    private void startEdit(int position){
        //Idee aus dem Speicher holen
        Idea idea = store.getIdea(passedName);
        //Frage und Antwort an der aktuellen Position setzen
        selectedQuestion = idea.getQuestion(position);
        selectedAnswer = idea.getAnswer(position);
        index = position;

        // Bearbeiten Activity starten. Es werden Informationen an die nächste Activity übergeben (putExtra)
        Intent intentQuestionEdit = new Intent(getApplicationContext(), QuestionEditActivity.class);
        intentQuestionEdit.putExtra("question", selectedQuestion);
        intentQuestionEdit.putExtra("answer", selectedAnswer);
        intentQuestionEdit.putExtra("ideaName", passedName);
        intentQuestionEdit.putExtra("index", index);
        startActivity(intentQuestionEdit);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.ideaOverviewList) {
            if(list.get(0).values().contains("Keine Antwort")) {}
            else{
                //Menüpunkt Löschen einfügen, an der position 0, mit der id 0
                menu.add(Menu.NONE, 0, 0, "Löschen");
                //Menüpunkt Bearbeiten einfügen an der position 1, mit der id 1
                menu.add(Menu.NONE, 1, 1, "Bearbeiten");
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        final ListView ideaListView = (ListView)findViewById(R.id.ideaOverviewList);
        Object listItem = ideaListView.getItemAtPosition(info.position);
        switch(item.getItemId()) {
            //erster Punkt des Menüs
            case 0:

                //Frage und Antwort aus der Idee löschen
                idea.removePair(info.position);
                //bearbeitete Idee speichern
                store.update(idea);
                //Liste aktualisieren
                fillOverviewList();
                return true;

            //zweiter Punkt des Menüs
            //Problemstellung bearbeiten
            case 1:
            //Frage bearbeiten
            startEdit(info.position);
                return true;

            //return im Fehlerfall
            default:
                return super.onContextItemSelected(item);
        }
    }

    //onBackpressed überschrieben, sodass nicht die letzte Activity gestartet wird sondern immer IdeaActivity
    @Override public void onBackPressed(){
        Intent intentStart=new Intent(this, IdeaActivity.class);
        startActivity(intentStart);
    }


    public void onClick(View v){
        switch(v.getId()){
            //Bei klick auf Löschen Button delete Dialog starten
            case R.id.btn_delete:
                deleteDialog(passedName);
                break;
            //Bei klick auf weiter Question Activity starten und Extras übergeben
            case R.id.btn_start_again:
                Intent intentStart=new Intent(this, QuestionActivity.class);
                intentStart.putExtra("ideaName",passedName);
                intentStart.putExtra("flag","letzteFrage");
                startActivity(intentStart);

                break;
        }
    }

}
