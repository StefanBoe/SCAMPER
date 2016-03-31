package com.example.stefan.scamper;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;


public class MainActivity extends ActionBarActivity implements OnClickListener{


    public static final String PREFS_OPTION = "MyPrefOptions";
    public IdeaStore store = new IdeaStore();
    RelativeLayout helpOverlay;
    LinearLayout mainLinearLayout;
    TextView textView;
    HelpOverlay help;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn_start = (Button)findViewById(R.id.btn_start);
        btn_start.setOnClickListener(this);
        Button btn_projects = (Button)findViewById(R.id.btn_projects);
        btn_projects.setOnClickListener(this);
        helpOverlay = (RelativeLayout) findViewById(R.id.help_overlay);
        helpOverlay.setVisibility(View.INVISIBLE);
        mainLinearLayout = (LinearLayout) findViewById(R.id.mainLinearLayout);
        textView = (TextView) findViewById(R.id.helpText);
        help = new HelpOverlay(helpOverlay);

        //createShowcase Funktion ausführen nach einem kurzen Delay
        //Dies ist nötig um der Action Bar Zeit zu geben geladen zu werden. Sonst kommt es zu einer NullPointer Exception
        Handler myHandler = new Handler();
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                createShowcase();
            }
        }, 500);

    }

    // Um das Keyboard und helpOverlay auszublenden wenn irgendwo außerhalb des Keyboard geklickt wird
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Keyboard ausblenden
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        //HelpOverlay ausblenden falls sichtbar
        help.setInvisible(this);
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }


    public void createShowcase(){

        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(200);

        //Neue Sequenz mit der ID "ID1" damit diese Sequenz nur beim ersten Start der App angezeigt wird
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this,"ID1");

        sequence.setConfig(config);

        //Ein Sequenz Item hinzufügen
        sequence.addSequenceItem(new View(this),
                "\n \n\n Willkommen! Hier ein paar kurze Tipps zum Umgang mit der SCAMPER-APP. ", "Ok");

        if(findViewById(R.id.action_help)!=null)
            sequence.addSequenceItem(findViewById(R.id.action_help),
                    "Hier erhalten Sie Informationen, falls Sie diese benötigen.", "Ok");

        if(findViewById(R.id.action_settings)!=null)
            sequence.addSequenceItem(findViewById(R.id.action_settings),
                    "Hier können Einstellungen vorgenommen werden.", "Ok");

        sequence.addSequenceItem(findViewById(R.id.btn_start),
                "Sobald Sie eine Problemstellung eingegeben haben, können Sie hier beginnen.", "Ok");

        sequence.addSequenceItem(findViewById(R.id.btn_projects),
                "Hier sehen Sie alle bisher erstellen Problemstellungen", "Ok");
        sequence.start();

    }
    //Startoption prüfen
    public String checkStart(){
        //Einstellungen aus den Shared Preferences laden
        SharedPreferences prefs = getSharedPreferences(MainActivity.PREFS_OPTION, MODE_PRIVATE);
        //Startoption zurückgeben. Bei einem Fehler startoption Normal zurückgeben
        return prefs.getString("StartPreferences", "Normal");
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //Starte Settings activity bei klick auf Settings Action Bar Item
        if (id == R.id.action_settings) {
            Intent intentSettings=new Intent(this, SettingsActivity.class);
            startActivity(intentSettings);
        }

        //Zeigt das Hilfe Fenster bei klick auf Hilfe Action Bar Item
        if (id == R.id.action_help) {
            textView.setText(getText(R.string.tf_help_main));
            help.helpOverlay(getApplicationContext());

        }

        return super.onOptionsItemSelected(item);
    }




    @Override
    public void onClick(View v) {

        switch(v.getId()){
            //Bei blick auf Starten Button
            case R.id.btn_start:

                EditText textIdeaName = (EditText)findViewById(R.id.textIdeaName);
                //input = Eingabe aus dem Textfeld
                String input = textIdeaName.getText().toString();

                //Wenn die Eingabe leer ist Nachricht anzeigen lassen
                if((input.isEmpty())){
                    Toast.makeText(this, "Bitte eine Problemstellung eingeben", Toast.LENGTH_LONG).show();
                }

                else {

                    //Falls es die Idee schon gibt Nachricht anzeigen lassen
                    if (store.hasIdea(input)) {
                        Toast.makeText(this, "Idee Existiert bereits. Bitte etwas anderes eingeben", Toast.LENGTH_LONG).show();
                    }

                    else {
                    //Idee mit der Eingabe als Namen erstellen
                    Idea currentIdea = new Idea(input);
                    //Neuen intent starten mit extras
                    Intent intentStart = new Intent(this, QuestionActivity.class);
                    intentStart.putExtra("ideaName", currentIdea.getName());
                    intentStart.putExtra("flag", checkStart());
                    startActivity(intentStart);
                    }
                }
                break;

            // Intent IdeaActivity starten wenn auf Alle Problemstellungen geklickt wird
            case R.id.btn_projects:
                Intent intentProjects=new Intent(this, IdeaActivity.class);
                startActivity(intentProjects);

                break;

        }



    }
}
