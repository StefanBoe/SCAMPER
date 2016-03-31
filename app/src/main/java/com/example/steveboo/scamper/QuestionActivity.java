package com.example.steveboo.scamper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;


public class QuestionActivity extends ActionBarActivity implements OnClickListener  {

    //String Arrays mit den Fragen zu jeder Kategorie
    public String[] substituteQuestions = {
    "Welche Vorgehensweise kann ich ersetzen?",
    "Wie kann ich anders an die Problemstellung herangehen?",
    "Wie kann ein anderes Format verwendet werden?",
    "Wie können andere Personen eingebunden werden?",
    "Wie können die Regeln geändert werden?"
    };
    public String[] combineQuestions = {
    "Wie kann ich meine Vorgehensweise mit etwas anderem kombinieren?",
    "Wie kann ich Ideen kombinieren?",
    "Wie kann ich Ziele verknüpfen?",
    "Wie können zwei untergeordnete Ziele kombiniert werden?"
    };
    public String[] adaptQuestions = {
    "Wie kann ich eine ähnliche Idee zu einem Teil meiner Problemstellung hinzufügen?",
    "Wie kann ich eine ähnliche Vorgehensweise kopieren?",
    "Wie kann ein Prozess aus einem anderen Umfeld kopiert werden?",
    "Was kann aus der Natur kopiert werden?"
    };
    public String[] modifyQuestions = {
    "Was kann ich an meiner Idee abändern?",
    "Welchen Aspekt meiner Idee kann ich verstärken?",
    "Was würde einen Mehrwert bringen?",
    "In welcher Form könnte die Idee noch angewendet werden?",
    "Wie kann die Einstellung gegenüber der Idee verändert werden?",
    "Durch welche Erweiterungen kann ein untergeordnetes Ziel wichtiger werden?"
    };
    public String[] putQuestions = {
    "Wie kann ich meine Idee in einem anderen Kontext verwenden?",
    "Was muss ich verändern um die Idee anders zu verwenden?",
    "Welche ungenutzten Potenziale der Idee gibt es?",
    "Welche Teile der Idee können anders verwendet werden?"
    };
    public String[] eliminateQuestions = {
    "Was kann ich von meiner Idee entfernen um sie zu verbessern?",
    "Welche Ziele kann man entfernen?",
    "Welche Teile der Idee sind unnötig?",
    "Wie kann ich die Idee aufteilen um sie besser zu Strukturieren?"
    };
    public String[] reverseQuestions = {
    "Was kann ich an meiner Idee umkehren?",
    "Was kann ich an meiner Idee umgestalten?",
    "Wie kann ich die Idee umorganisieren damit sie besser funktioniert?",
    "Wie kann der Sinn der Idee umgekehrt werden?"
    };

    Idea idea ;
    IdeaStore store = new IdeaStore();
    String questionSwitch="Substitute";
    int switcher=1;
    SharedPreferences prefsOption;
    int numberQuestions=1;
    int questionNumber;
    String question;
    String flag;
    TextView titleText;
    TextView categoryText;
    TextView problemstellungText;
    EditText answerText;
    Button btn_next;
    String passedName;
    SharedPreferences.Editor editor;
    RelativeLayout helpOverlay;
    LinearLayout mainLinearLayout;
    TextView textHelp;
    TextSwitcher textSwitcherRandom;
    TextSwitcher textSwitcherQuestion;
    ArrayList<String> list = new ArrayList<String>();
    Scanner scanner = null;
    HelpOverlay help;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        btn_next = (Button)findViewById(R.id.btn_next);
        btn_next.setOnClickListener(this);
        btn_next.setText("Weiter");
        btn_next.setId(R.id.btn_next);
        titleText = (TextView) findViewById(R.id.textQuestionTitle);
        categoryText = (TextView) findViewById(R.id.textCategory);
        problemstellungText = (TextView) findViewById(R.id.textProblemstellung);
        helpOverlay = (RelativeLayout) findViewById(R.id.help_overlay);
        helpOverlay.setVisibility(View.INVISIBLE);
        mainLinearLayout = (LinearLayout) findViewById(R.id.mainLinearLayout);
        textHelp = (TextView) findViewById(R.id.helpText);
        answerText = (EditText) findViewById(R.id.answerTextfield);
        prefsOption = getSharedPreferences(MainActivity.PREFS_OPTION, MODE_PRIVATE);
        editor = this.getSharedPreferences(MainActivity.PREFS_OPTION, this.MODE_PRIVATE).edit();

        //Anzahl der Fragen aus den Einstellungs Shared Preferences laden
        numberQuestions = Integer.parseInt(prefsOption.getString("NumberQuestions", "1"));

        //Das HelpOverlay erstellen wenn auf Hilfe in der Actionbar gedrückt wird
        help = new HelpOverlay(helpOverlay);
        Intent myIntent = getIntent();
        //Namen aus dem Intent Extra laden
        passedName = myIntent.getStringExtra("ideaName");

        //aktuelle Problemstellung anzeigen
        problemstellungText.setText(passedName);
        idea = new Idea(passedName);
        //Maximale Fragenanzahl setzen
        idea.setMaxNumberQuestions(numberQuestions);
        //Flag für die Startoption laden
        flag = myIntent.getStringExtra("flag");
        //Startoption mit dem Flag
        startOption(flag);





        textSwitcherQuestion = (TextSwitcher) findViewById(R.id.textSwitcherQuestion);
        //In und Out Animation setzen
        textSwitcherQuestion.setInAnimation(this, R.anim.slide_in_right);
        textSwitcherQuestion.setOutAnimation(this, R.anim.slide_out_left);
        //Einstellungen für das Frage Textview erstellen
        textSwitcherQuestion.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(getApplicationContext());
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(Color.GRAY);
                textView.setTextSize(18);
                return textView;
            }
        });


        textSwitcherQuestion.setText(question);
        //Zufallswort Textfeld
        textSwitcherRandom = (TextSwitcher) findViewById(R.id.textSwitcherRandom);
        //In und Out Animation setzen
        textSwitcherRandom.setInAnimation(this, R.anim.slide_in_right);
        textSwitcherRandom.setOutAnimation(this, R.anim.slide_out_left);
        //Einstellungen für das Zufallswort Textview erstellen
        textSwitcherRandom.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(getApplicationContext());
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(Color.BLACK);
                textView.setTextSize(22);
                return textView;
            }
        });

        loadWords();

        Handler myHandler = new Handler();
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                createShowcase();
            }
        }, 500);
    }

    //Laden der Wortliste
    public void loadWords(){
        //Scanner um die Wortliste aus der txt Datei einzulesen
        scanner = new Scanner(getResources().openRawResource(R.raw.word),"UTF-8");
        while (scanner.hasNext()){
            //alle Wörter in die Liste schreiben
            list.add(scanner.next());
        }
        //Scanner schließen
        scanner.close();
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


    //Switch case um die Startoption aus den Optionen umzusetzen
    public void startOption(String flag){
        switch (flag){
            // Option Random damit eine Frage aus einer Zufälligen Kategorie gestellt wird
            case "Random":
                //Alle möglichen Kategorien
                String[] qSwitch={"Substitute","Combine","Adapt","Modify","Put","Eliminate","Reverse"};
                questionSwitch=qSwitch[random(qSwitch.length)];
                //aktuelle Anzahl an Fragen anpassen
                idea.addToCurrentNumberQuestion();
                //Text anzeigen lassen
                setAllText();
                break;

            // Wenn Bearbeitung fortgesetzt wird
            case "letzteFrage":
               // Idee laden
                idea = store.getIdea(passedName);
                //letzte Frage laden
                String latestQuestion = idea.getlatestQuestion();
                //Kategorie der Frage laden
                questionSwitch = idea.getLatestTitle();

                //Text anzeigen lassen falls es keinen Fehler gibt
               if(latestQuestion!="" && questionSwitch!="") {
                    setAllText();
                }

               //Bei einem Fehler Idee Normal starten
                else{
                   idea.setCurrentNumberQuestions(0);
                   Log.e("Start Option","falsch");
                   startOption("Normal");
                }
                break;

            //Normaler start mit der Kategorie Substitute
            case "Normal":
                questionSwitch = "Substitute";
                idea.addToCurrentNumberQuestion();
                setAllText();
                break;
        }
    }


    // Um Code in den einzelnen Kategorien zu kürzen
    public void shrinkElse(){
        switcher = 1;
        //bereits gestellte Fragen löschen
        idea.clearArray();
        //Anzahl der gestellten Fragen anpassen
        idea.addToCurrentNumberQuestion();
    }


    public void setAllText(){
        switch (questionSwitch){
            case "Substitute":
                    //Title wird gesetzt
                    titleText.setText(Html.fromHtml("<p><strong>S</strong>CAMPER</p>"));
                    //Text für das Hilfefenster setzen
                    textHelp.setText(getText(R.string.substitute));
                    //letzte Kategorie Speichern
                    idea.setLatestTitle(questionSwitch);
                    //Kategorie Text setzen
                    categoryText.setText(questionSwitch);
                    //Eine random Frage aus allen substitute Fragen wird ausgewaehlt
                    questionNumber = random(substituteQuestions.length);

                //Zähler damit bei einem Fehler aus der While gesprungen werden kann
                i=0;
                //Damit keine Frage doppelt gefragt wird
                while (idea.getQuestionArray().contains(questionNumber)){
                    questionNumber = random(substituteQuestions.length);
                    //Zähler damit bei 20 while durchläufen die while beendet wird
                    i++;
                    if(i>20)
                        idea.clearArray();
                }

                //Nummer der gestellten Frage in Liste eintragen
                idea.addInQuestionArray(questionNumber);
                    //Frage aus dem Array mit allen Fragen laden
                    question = substituteQuestions[questionNumber];
                    //Letzte Fragen setzen
                    idea.setLatestQuestion(question);

                    //Wenn alle Fragen in einer Kategorie beantwortet sind
                    if(switcher==numberQuestions){
                        //Abfrage damit nicht mehr Fragen beantwortet werden als nötig
                        if(idea.maxQuestions()){
                            //Nachricht anzeigen
                            Toast.makeText(this,"Alle Fragen beantwortet!",Toast.LENGTH_LONG).show();

                            //Dem Button eine andere ID geben um speichern zu können
                            btn_next.setId(R.id.btn_save);
                            btn_next.setText("Speichern");
                        }
                        //Falls insgesamt noch nicht alle Fragen beantwortet sind wird zur nächsten Kategorie gewechselt
                        else {
                            //Nächste Kategorie Combine
                            questionSwitch = "Combine";
                            //Verkürzungsfunktion
                            shrinkElse();
                        }
                    }
                    //Nächste Frage in gleicher Kategorie
                    else{
                        //switcher anpassen
                        switcher++;
                        //Aktuelle Anzahl an Fragen anpassen
                        idea.addToCurrentNumberQuestion();
                    }

                break;
            case "Combine":

                    titleText.setText(Html.fromHtml("<p>S<strong>C</strong>AMPER</p>"));
                //Text für das Hilfefenster setzen
                textHelp.setText(getText(R.string.combine));
                    idea.setLatestTitle(questionSwitch);
                    categoryText.setText(questionSwitch);
                    questionNumber = random(combineQuestions.length);
                    i=0;
                    while(idea.getQuestionArray().contains(questionNumber)){
                        questionNumber = random(combineQuestions.length);
                        i++;
                        if(i>20)
                            idea.clearArray();
                    }
                    idea.addInQuestionArray(questionNumber);
                    question = combineQuestions[questionNumber];
                idea.setLatestQuestion(question);

                    if(switcher==numberQuestions){
                        if(idea.maxQuestions()){
                            Toast.makeText(this,"Alle Fragen beantwortet!",Toast.LENGTH_LONG).show();
                            btn_next.setId(R.id.btn_save);
                            btn_next.setText("Speichern");
                        }
                        else {
                            questionSwitch = "Adapt";
                            shrinkElse();
                        }
                    }
                    else{
                        switcher++;
                        idea.addToCurrentNumberQuestion();
                    }

                break;
            case "Adapt":

                    titleText.setText(Html.fromHtml("<p>SC<strong>A</strong>MPER</p>"));
                //Text für das Hilfefenster setzen
                textHelp.setText(getText(R.string.adapt));
                idea.setLatestTitle(questionSwitch);
                categoryText.setText(questionSwitch);
                    questionNumber = random(adaptQuestions.length);
                    i=0;
                    while(idea.getQuestionArray().contains(questionNumber)){
                        questionNumber = random(adaptQuestions.length);
                        i++;
                        if(i>20)
                            idea.clearArray();
                    }
                    idea.addInQuestionArray(questionNumber);
                    question = adaptQuestions[questionNumber];
                idea.setLatestQuestion(question);
                    if (switcher == numberQuestions) {
                        if(idea.maxQuestions()){
                            Toast.makeText(this,"Alle Fragen beantwortet!",Toast.LENGTH_LONG).show();
                            btn_next.setId(R.id.btn_save);
                            btn_next.setText("Speichern");
                        }
                        else {
                            questionSwitch = "Modify";
                            shrinkElse();
                        }

                    } else {
                        switcher++;
                        idea.addToCurrentNumberQuestion();
                    }

                break;
            case "Modify":

                titleText.setText(Html.fromHtml("<p>SCA<strong>M</strong>PER</p>"));
                //Text für das Hilfefenster setzen
                textHelp.setText(getText(R.string.modify));
                idea.setLatestTitle(questionSwitch);
                categoryText.setText(questionSwitch);
                questionNumber = random(modifyQuestions.length);
                i=0;
                while(idea.getQuestionArray().contains(questionNumber)){
                    questionNumber = random(modifyQuestions.length);
                    i++;
                    if(i>20)
                        idea.clearArray();
                }
                idea.addInQuestionArray(questionNumber);
                question = modifyQuestions[questionNumber];
                idea.setLatestQuestion(question);
                if (switcher == numberQuestions) {
                    if(idea.maxQuestions()){
                        Toast.makeText(this,"Alle Fragen beantwortet!",Toast.LENGTH_LONG).show();
                        btn_next.setId(R.id.btn_save);
                        btn_next.setText("Speichern");
                    }
                    else {
                        questionSwitch = "Put";
                        shrinkElse();
                    }
                } else {
                    switcher++;
                    idea.addToCurrentNumberQuestion();
                }

                break;
            case "Put":

                titleText.setText(Html.fromHtml("<p>SCAM<strong>P</strong>ER</p>"));
                //Text für das Hilfefenster setzen
                textHelp.setText(getText(R.string.put));
                idea.setLatestTitle(questionSwitch);
                //hier nicht question switch, da nur "Put" als titel nicht ausreicht
                categoryText.setText("Put to another use");
                questionNumber = random(putQuestions.length);
                i=0;
                while(idea.getQuestionArray().contains(questionNumber)){
                    questionNumber = random(putQuestions.length);
                    i++;
                    if(i>20)
                        idea.clearArray();
                }
                idea.addInQuestionArray(questionNumber);
                question = putQuestions[questionNumber];
                idea.setLatestQuestion(question);
                if (switcher == numberQuestions) {
                    if(idea.maxQuestions()){
                        Toast.makeText(this,"Alle Fragen beantwortet!",Toast.LENGTH_LONG).show();
                        btn_next.setId(R.id.btn_save);
                        btn_next.setText("Speichern");
                    }
                    else {
                        questionSwitch = "Eliminate";
                        shrinkElse();
                    }
                } else {
                    switcher++;
                    idea.addToCurrentNumberQuestion();
                }

                break;
            case "Eliminate":

                titleText.setText(Html.fromHtml("<p>SCAMP<strong>E</strong>R</p>"));
                //Text für das Hilfefenster setzen
                textHelp.setText(getText(R.string.eliminate));
                idea.setLatestTitle(questionSwitch);
                categoryText.setText(questionSwitch);
                questionNumber = random(eliminateQuestions.length);
                i=0;
                while(idea.getQuestionArray().contains(questionNumber)){
                    questionNumber = random(eliminateQuestions.length);
                    i++;
                    if(i>20)
                        idea.clearArray();
                }
                idea.addInQuestionArray(questionNumber);
                question = eliminateQuestions[questionNumber];
                idea.setLatestQuestion(question);
                if (switcher == numberQuestions) {
                    if(idea.maxQuestions()){
                        Toast.makeText(this,"Alle Fragen beantwortet!",Toast.LENGTH_LONG).show();
                        btn_next.setId(R.id.btn_save);
                        btn_next.setText("Speichern");
                    }
                    else {
                        questionSwitch = "Reverse";
                        shrinkElse();
                    }
                } else {
                    switcher++;
                    idea.addToCurrentNumberQuestion();
                }
                break;
            case "Reverse":

                titleText.setText(Html.fromHtml("<p>SCAMPE<strong>R</strong></p>"));
                //Text für das Hilfefenster setzen
                textHelp.setText(getText(R.string.reverse));
                idea.setLatestTitle(questionSwitch);
                categoryText.setText(questionSwitch);
                questionNumber = random(reverseQuestions.length);
                i=0;
                while(idea.getQuestionArray().contains(questionNumber)){
                    questionNumber = random(reverseQuestions.length);
                    i++;
                    if(i>20)
                        idea.clearArray();
                }
                idea.addInQuestionArray(questionNumber);
                question = reverseQuestions[questionNumber];
                idea.setLatestQuestion(question);
                if (switcher == numberQuestions) {

                    if(idea.maxQuestions()){
                        Toast.makeText(this,"Alle Fragen beantwortet!",Toast.LENGTH_LONG).show();
                        btn_next.setId(R.id.btn_save);
                        btn_next.setText("Speichern");
                        idea.setDone(true);
                    }
                    else {
                        questionSwitch = "Substitute";
                        shrinkElse();
                    }
                } else {
                    switcher++;
                    idea.addToCurrentNumberQuestion();
                }
                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_question, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //Zeigt das Hilfe Fenster bei klick auf Hilfe Action Bar Item
        if (id == R.id.action_help) {
            //Wenn HelpOverlay sichtbar ist wieder Unsichtbar machen und umgekehrt
            help.helpOverlay(getApplicationContext());

        }

        //Wenn Würfel Button gedrückt wird
        if(id == R.id.action_dice){
            //Falls Zufallswort mit dem next Button invisible gesetzt wurde
            textSwitcherRandom.setVisibility(View.VISIBLE);
            //Text anzeigen lassen in HTML aus der Liste der Wörter mit zufallsgenerator
            textSwitcherRandom.setText(Html.fromHtml(list.get(random(list.size()))));
            //Nachdem man weiter gedrückt hat wieder die normale out animation
            textSwitcherRandom.setOutAnimation(this, R.anim.slide_out_left);
        }

        return super.onOptionsItemSelected(item);
    }

    //Gibt eine Zufallszahl mit size als Maximum
    public int random(int size){
        Random rand = new Random();
        int randomNumber=rand.nextInt(size);
        return randomNumber;
    }

    //onBackpressed überschreiben damit Idee gespeichert wird
    @Override public void onBackPressed(){

        EditText answerText = (EditText) findViewById(R.id.answerTextfield);
        //Falls nichts im Antwortfeld steht
        if(answerText.getText().toString().equals("")) {
            //setAllText damit die Fragen die als nächstes gefragt werden würde gespeichert wird
            setAllText();
            //Fragen zähler um eins zurücksetzen, da er in setAllText erhöht wurde
            idea.setCurrentNumberQuestions(idea.getCurrentNumberQuestions()-1);
            store.update(idea);
        }
        else {
            //Falls etwas im Textfeld steht wird Frage und Antwortpaar hinzugefügt
            idea.addpair(question, answerText.getText().toString());
            //setAllText damit die die Fragen die als nächstes gefragt werden würde gespeichert wird
            setAllText();
            //Idee speichern
            store.update(idea);

        }
        //Nachricht um zu zeigen, dass gespeichert wurde
        Toast.makeText(this, "Gespeichert", Toast.LENGTH_LONG).show();
        //Neue Aktivität IdeaOverviewActivity starten mit Namen als Extra
        //Damit man in der Idee bleibt und eine Übersicht über die Idee erhält
        Intent intentStart=new Intent(this, IdeaOverviewActivity.class);
        intentStart.putExtra("name", passedName);
        startActivity(intentStart);
        //Aktivität beende damit man nicht wieder in die bearbeitung springen kann wenn man auf zurück klickt
        finish();
    }

    public void createShowcase(){

        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(200);

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this,"ID2");

        sequence.setConfig(config);

        sequence.addSequenceItem(new View(this),
                "\n \n\n Hier beginnt der wichtigste Teil der App. Es werden Fragen gestellt, die Sie dazu anregen sollen auf neue Gedanken zu kommen", "Ok");

        sequence.addSequenceItem(new View(this),
                "\n \n\n Die Fragen müssen dabei aber abstrakt gesehen werden. Sie werden nicht immer auf die aktuelle Problemstellung anwendbar sein.", "Ok");

        if(findViewById(R.id.action_dice)!=null)
            sequence.addSequenceItem(findViewById(R.id.action_dice),
                    "Hier kann ein Zufallswort angezeigt werden, dass Sie auf neue Gedanken bringen kann", "Ok");

        sequence.addSequenceItem(findViewById(R.id.btn_next),
                    "Hier kommen Sie zur nächsten Frage. Wenn Sie keine Antwort eingegeben haben, wird diese Frage übersprungen", "Ok");

        sequence.addSequenceItem(new View(this),
                "\n \n\n Sobald Sie keine Fragen mehr beantworten wollen, kann die Problemstellung mit dem \"Zurück\"-Button gespeichert werden", "Ok");
        sequence.start();

    }
    @Override
    public void onClick(View v) {

        switch(v.getId()){

            //Speichern Button
            case R.id.btn_save:
                onBackPressed();
                break;
            //Weiter Button
            case R.id.btn_next:
                //Wenn textfeld leer ist
                if(answerText.getText().toString().equals("")) {
                    //Anzeige wenn nichts eingegeben und Frage übersprungen wird
                    Toast.makeText(this, "Keine Antwort eingegeben. Frage wird übersprungen", Toast.LENGTH_LONG).show();
                } else {
                    //Frage und Antwort zur Idee hinzufügen
                    idea.addpair(question, answerText.getText().toString());
                    //Idee speichern
                    store.update(idea);
                    // Antwort Textfeld leeren
                    answerText.setText("");
                }

                //Zufallswort ausblenden falls es sichtbar ist
                if(textSwitcherRandom.getVisibility()==View.VISIBLE) {

                    textSwitcherRandom.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_out_left));
                    //Zufallswort unsichtbar machen, man muss bei der neuen Frage erstmal auf Zufallswort klicken
                    textSwitcherRandom.setVisibility(View.INVISIBLE);
                    //Out animation auf null setzen. Sonst wird altes Wort nachdem man auf weiter gedrückt hat, und wieder ein Zufallswort will, wieder angezeigt
                    textSwitcherRandom.setOutAnimation(null);
                }
                //setAllText um alles für die nächste Frage vorzubereiten
                setAllText();
                //alte Frage ausblenden und neue einblenden
                textSwitcherQuestion.setText(question);
                break;

        }
    }
}
