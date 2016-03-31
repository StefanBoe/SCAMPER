package com.example.stefan.scamper;


import android.view.View.OnClickListener;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends ActionBarActivity implements OnClickListener {

    SharedPreferences prefs;
    RelativeLayout helpOverlay;
    LinearLayout mainLinearLayout;
    TextView textView;
    HelpOverlay help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        prefs = getSharedPreferences(MainActivity.PREFS_OPTION, MODE_PRIVATE);
        helpOverlay = (RelativeLayout) findViewById(R.id.help_overlay);
        helpOverlay.setVisibility(View.INVISIBLE);
        mainLinearLayout = (LinearLayout) findViewById(R.id.mainLinearLayout);
        textView = (TextView) findViewById(R.id.helpText);
       help = new HelpOverlay(helpOverlay);
        Button btn_save = (Button)findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);

        setStartChecked();
        setSeekBar();



    }
    // Um das helpOverlay auszublenden
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //HelpOverlay ausblenden falls sichtbar
        help.setInvisible(this);
        return true;
    }

    //Seekbar Wert setzen
    public void setSeekBar(){
        //  Seekbar value auslesen und in der TextView4 ausgeben lassen
        final SeekBar questionsSeekBar = (SeekBar)findViewById(R.id.questionsSeekBar);
        // Anzahl an Fragen aus den Einsellungen auslesen
        int numberQuestions = Integer.parseInt(prefs.getString("NumberQuestions","1"));
        // Wert setzten
        questionsSeekBar.setProgress((numberQuestions-1)*33);
        Integer i = questionsSeekBar.getProgress();
        TextView t = (TextView) findViewById(R.id.textViewProgress);
        i = i / 33 + 1;
        //Text unter der Seekbar setzen
        t.setText(Html.fromHtml(i.toString() + "/4"));

        //Change listener für die seekbar erstellen
        questionsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            //Sobald der Wert verändert wird, wird auch der Text verändert
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Test anzeige fuer progress
                Integer i = questionsSeekBar.getProgress();
                TextView t = (TextView) findViewById(R.id.textViewProgress);
                i = i / 33 + 1;
                t.setText(Html.fromHtml(i.toString() + "/4"));

            }

        });
    }

    //Radiobutton Aktivieren
    public void setStartChecked(){
        RadioGroup rGroup = (RadioGroup)findViewById(R.id.radioGroup);

        //Aus den Einstellungen auslesen welche Startoption gewählt war
        //Bei einem Fehler wird Normal ausgewählt
        String onStartSelected = prefs.getString("StartPreferences","Normal");

        //Aktiviert Radiobutton in Radiogroup je nachdem welcher vorher aktiviert war
        switch(onStartSelected){
            case "Random":
                rGroup.check(R.id.radio1);
                break;
            case "Normal":
                rGroup.check(R.id.radio3);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //Zeigt das Hilfe Fenster bei klick auf Hilfe Action Bar Item
        if (id == R.id.action_help) {

            textView.setText(getText(R.string.tf_help_settings));
            help.helpOverlay(getApplicationContext());
        }
        return super.onOptionsItemSelected(item);

    }

    //Einstellungen speichern
    public void saveSettings(){
        SeekBar questionsSeekBar = (SeekBar)findViewById(R.id.questionsSeekBar);
        RadioGroup rGroup = (RadioGroup)findViewById(R.id.radioGroup);

        //Shared Preferences editor laden
        SharedPreferences.Editor editor = this.getSharedPreferences(MainActivity.PREFS_OPTION, this.MODE_PRIVATE).edit();
        int id = rGroup.getCheckedRadioButtonId();
        String radioOption="Normal";

        //im Fehlerfall wird Normal gespeichert
        if (id == -1){
            radioOption="Normal";
        }

        //RadioButton Einstellung in radioOption Variable speichern
        switch(id){
            case R.id.radio1:
                radioOption="Random";
                break;
            case R.id.radio3:
                radioOption="Normal";
                break;
        }

        // Seekbar Wert in progressOption Variable speichern
        int progressOption = questionsSeekBar.getProgress()/33+1;

        //Beide Variablen in den Editor übergeben
        editor.putString("NumberQuestions", String.valueOf(progressOption));
        editor.putString("StartPreferences", radioOption);

        //Editor speichern
        editor.apply();
        //Nachricht anzeigen
        Toast.makeText(this,"Einstellungen gespeichert!",Toast.LENGTH_LONG).show();
    }


    //Button Funktion
    public void onClick(View v){
        switch(v.getId()){
            //Bei klick auf Speichern-Button, Einstellungen speichern
            case R.id.btn_save:
                saveSettings();
                super.onBackPressed();
                break;
        }
    }

}
