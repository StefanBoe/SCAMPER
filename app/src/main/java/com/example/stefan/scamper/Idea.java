package com.example.stefan.scamper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// implements Serializable damit das Objekt später als Datei gespeichert und geladen werden kann
public class Idea implements Serializable {
    private String name="NoName";
    public ArrayList<String> question = new ArrayList<String>();
    public ArrayList<String> answer = new ArrayList<String>();

    //Variablen um da zu starten wo man aufgehört hat
    public int maxNumberQuestions = 7;
    public int currentNumberQuestions = 0;
    public String latestQuestion = "";
    private String latestTitle = "";
    private boolean done = false;

    //Liste mit Nummern der Fragen die schon gefragt wurden
    List<Integer> questionArray = new ArrayList<Integer>();

    //Konstruktor
    public Idea(){
    }

    //Konstruktor mit Namen der Idee
    public Idea(String ideaName){

        //Idee wird NoName gennannt falls der String ideaName leer ist
        if(ideaName == null || ideaName.isEmpty()) {
            name = "NoName";
        }
        //sonst wird der Ideename durch den String ideaName benannt
        else {
            name=ideaName;
        }
    }

    //Abfrage ob alle Fragen beantwortet sind
    public boolean isDone(){
        return done;
    }
    public void setDone(boolean setDone){
        done=setDone;
    }
    //Funktion um Frage und Antwort der Idee hinzuzufügen
    public void addpair(String pairQuestion, String pairAnswer){
        Integer i;
        i = getSize();

        //Frage und Antwort jeweils an letzter Position in der jeweiligen ArrayList speichern
        question.add(i, pairQuestion);
        answer.add(i, pairAnswer);
    }

    //Funktion um Frage und Antwort paar zu entfernen
    public void removePair(int index){
        question.remove(index);
        answer.remove(index);
    }
    //Namen der Idee ändern
    public void setName(String ideaName) {
        if(ideaName == null || ideaName.isEmpty()) {
            name = "NoName";
        }
        else {
            name = ideaName;
        }
    }

    //Nummer der gestellten Fragen hinzufügen
    public void addInQuestionArray(int question){
        questionArray.add(question);
    }

    // zuletzt gestellte Frage setzen
    public void setLatestQuestion(String question){
        latestQuestion = question;
    }

    //letzter Titel (SCAMPER Kategorie) setzten
    public void setLatestTitle(String title) {latestTitle = title; }

    //setzten wie viele Fragen in der aktuellen Idee gestellt wurden
    public void setCurrentNumberQuestions(int newCurrent){ currentNumberQuestions = newCurrent; }

    //setzen wie viele Fragen insgesamt gestellt werden können
    // (7 Anzahl der Kategorien * Anzahl der Fragen die in den Optionen eingestellt wurde)
    public void setMaxNumberQuestions(int numberQuestions){ maxNumberQuestions=7 * numberQuestions; }

    // Funktion zum erhöhen der Anzahl der gestellten Fragen
    public void addToCurrentNumberQuestion(){
        currentNumberQuestions++;
    }

    //Rückgabe vom Idee Namen
    public String getName(){ return name; }

    //Funktion zum testen ob maximale Anzahl an Fragen gestellt wurde
    public boolean maxQuestions(){
        if(currentNumberQuestions>=maxNumberQuestions){
            return true;
        }
        else{
            return false;
        }
    }

    // Nummern der Fragen löschen
    public void clearArray(){
        questionArray.clear();
    }

    // größe des Frage Array zurückgeben
    public int getSize(){
        return question.size();
    }

    //Liste mit den Nummern der Fragen zurückgeben
    public List<Integer> getQuestionArray(){
        return questionArray;
    }

    //Maximale Fragenanzahl zurückgeben
    public int getMaxNumberQuestions(){
        return maxNumberQuestions;
    }

    // Die aktuelle Anzahl an gestellten Fragen zurückgeben
    public int getCurrentNumberQuestions(){
        return currentNumberQuestions;
    }

    // zuletzt gestellte Frage zurückgeben
    public String getlatestQuestion(){
        return latestQuestion;
    }

    //letzten Titel (SCAMPER Kategorie) zurückgeben
    public String getLatestTitle(){return latestTitle; }

    //Frage aus der List an der Stelle des indexes zurückgeben
    public String getQuestion(int index){

        //empty rückgabe falls der Index größer ist als die Größe der Liste
        if(getSize()<index) {
            return "empty";
        }
        else{
            return question.get(index);
        }
    }

    //Antwort aus der List an der Stelle des indexes zurückgeben
    public String getAnswer(int index){

        //empty rückgabe falls der Index größer ist als die Größe der Liste
        if(answer.size()<index) {
            return "empty";
        }
        else{
            return answer.get(index);
        }
    }
    }

