package com.example.steveboo.scamper;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class IdeaStore {
    // Konstruktor
    IdeaStore(){}

    //Testfunktion die alle Daten löscht indem eine neue Liste erstellt und gespeichert wird
    public void deleteAll(){
        ArrayList<Idea> list = new ArrayList<>();
        save(list);
    }

    //Speicherfunktion
    public static void save(ArrayList<Idea> list){
        ObjectOutput out;
        try {
            //Neues File Objekt mit dem Pfad des External Storage und "savedScamper.data" als Dateiname
            File outFile = new File(Environment.getExternalStorageDirectory(), "savedScamper.data");
            out = new ObjectOutputStream(new FileOutputStream(outFile));
            //Die Liste mit den Idee Objekten wird gespeichtert
            out.writeObject(list);
            out.close();
            Log.e("File", "Saved");
        } catch (Exception e) {
            Log.e("File", "Not Saved");
            e.printStackTrace();}
    }

    //Ladefunktion
    public static ArrayList<Idea> load(){

        //Neues File Objekt mit dem Pfad des External Storage und "savedScamper.data" als Dateiname
        File in = new File(Environment.getExternalStorageDirectory(), "savedScamper.data");
        InputStream fileIn = null;

        //Neue Arrayliste aus Idea Objekten
        ArrayList<Idea> ideas = new ArrayList<>();

        try {
            //Versuche Arrayliste der Idea Objekte zu füllen aus der gespeicherten Datei mit FileInputStream
            fileIn = new FileInputStream(in);
            ObjectInputStream ois = new ObjectInputStream(fileIn);
            ideas = (ArrayList<Idea>) ois.readObject();

            ois.close();
        }
        catch (Exception e){e.printStackTrace();}
        //Rückgabe der Arrayliste
        return ideas;
    }

    //Updatefunktion
    //Übergabe des Idea Objektes das gespeichert werden soll
    public void update(Idea idea){
            //Idea Objekt löschen falls es schon vorhanden ist
            remove(idea.getName());
            //Liste laden
            ArrayList<Idea> list = load();
            //Idee der Liste hinzufügen
            list.add(idea);
            save(list);
    }

    //Löschfunktion
    public void remove(String ideaName){
        //Arrayliste der Idea Objekte laden
        ArrayList<Idea> list = load();
        //Index des Idea Objektes in der Liste
        int index = getIdeaIndex(ideaName,list);

        //Bei Fehler ins Log schreiben
        if(index == -1)
        {
            Log.e("remove","Idee nicht gefunden");
        }
        else {
            //Lösche Idea Objekt mit dem Index der vorher ermittelt wurde
            list.remove(index);
            //Speichere die modifizierte Liste
            save(list);
        }

    }

    // Gibt den Index von von dem ideaName in der ArrayListe list zurück und -1 falls es nicht gefunden wird
    public int getIdeaIndex (String ideaName, ArrayList<Idea> list){
        for (int i=0; i<list.size();i++){
            if(list.get(i).getName().equals(ideaName))
            {
                return i;
            }
        }

        return -1;

    }

    //gibt das Idea Objekt zurück mit dem Namen ideaName
    public Idea getIdea(String ideaName){
        //Liste laden
        ArrayList<Idea> list = load();
        //Liste durchlaufen bis der Name gefunden wird
        for (int i=0; i<list.size();i++){
            if(list.get(i).getName().equals(ideaName))
            {
                return list.get(i);
            }
        }

        //gibt neue Idee zurück falls die Idee vorher nicht gefunden wurde
        Log.e("getIdea","return null");
        return new Idea();
    }

    //Funktion zum testen ob der Name in der Liste vorkommt
    public boolean hasIdea(String ideaName){
        ArrayList<Idea> list =load();
        for (int i=0; i<list.size();i++){
            if(list.get(i).getName().equals(ideaName))
            {
                return true;
            }
        }
        return false;
    }


}
