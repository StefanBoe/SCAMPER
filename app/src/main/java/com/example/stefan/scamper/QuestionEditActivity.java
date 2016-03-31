package com.example.stefan.scamper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
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


public class QuestionEditActivity extends ActionBarActivity implements OnClickListener  {

    IdeaStore store = new IdeaStore();
    String passedName;
    int index=-1;
    EditText answerEditText;
    TextView questionText;
    RelativeLayout helpOverlay;
    LinearLayout mainLinearLayout;
    TextView textView;
    HelpOverlay help;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_edit);
        TextView titleText = (TextView) findViewById(R.id.textEditQuestionTitle);
        questionText = (TextView) findViewById(R.id.textEditQuestion);
        answerEditText = (EditText)findViewById(R.id.answerEditTextfield);
        textView = (TextView) findViewById(R.id.helpText);
        titleText.setText(Html.fromHtml("<p>SCAMPER</p>"));
        Button btn_save = (Button)findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);

        //Übergebene Extras laden
        Intent myIntent = getIntent();
        String question = myIntent.getStringExtra("question");
        String answer = myIntent.getStringExtra("answer");
        passedName = myIntent.getStringExtra("ideaName");
        index = myIntent.getIntExtra("index", -1);

        //Zu bearbeitende Frage und Antwort laden
        questionText.setText(question);
        answerEditText.setText(answer);

        helpOverlay = (RelativeLayout) findViewById(R.id.help_overlay);
        helpOverlay.setVisibility(View.INVISIBLE);
        mainLinearLayout = (LinearLayout) findViewById(R.id.mainLinearLayout);

        help = new HelpOverlay(helpOverlay);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_idea, menu);
        return true;
    }

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
            textView.setText(getText(R.string.tf_help_question_edit));
            help.helpOverlay(getApplicationContext());
        }

        return super.onOptionsItemSelected(item);
    }


    // Um das Keyboard und helpOverlay auszublenden
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
    public void onClick(View v) {
        switch (v.getId()) {
            //Bei klick auf Speichern Button, neue Antwort speichern
            case R.id.btn_save:
                //Bei einem Fehler error ins Log
                if(index==-1){
                    Log.e("error","some passing error");
                }

                else {
                    //Bei leerem Textfeld wird Nachricht angezeigt
                    if(answerEditText.getText().toString().equals("")){
                        Toast.makeText(this, "Bitte eine Antwort eingeben", Toast.LENGTH_LONG).show();
                    }


                    else {
                        //Idee laden
                        Idea idea = store.getIdea(passedName);
                        //zu bearbeitendes Frage und Antwort paar löschen
                        idea.removePair(index);
                        //neues paar hinzufügen
                        idea.addpair(questionText.getText().toString(), answerEditText.getText().toString());
                        //Idee speichern
                        store.update(idea);
                        Toast.makeText(this,"Antwort gespeichert!",Toast.LENGTH_LONG).show();
                        super.onBackPressed();
                    }
                }
                break;
        }
    }
}
