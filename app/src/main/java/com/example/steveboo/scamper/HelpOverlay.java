package com.example.steveboo.scamper;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HelpOverlay {
    RelativeLayout helpOverlayLayout;


    HelpOverlay(RelativeLayout relativeLayout){
        helpOverlayLayout = relativeLayout;
    }

    //Hilfe Fenster einblenden
    private void helpSlideIn(Context context) {
        //Die Animation zum einblenden Auswählen
        Animation animFadein = AnimationUtils.loadAnimation(context, R.anim.slide_in);
        //Fenster sichtbar machen
        helpOverlayLayout.setVisibility(View.VISIBLE);
        //Animation abspielen
        helpOverlayLayout.startAnimation(animFadein);
    }

    //Hilfe Fenster ausblenden
    private void helpSlideOut(Context context){
        //Die Animation zum ausblenden Auswählen
        Animation animFadeout = AnimationUtils.loadAnimation(context, R.anim.slide_out);
        helpOverlayLayout.startAnimation(animFadeout);
        //Fenster unsichtbar machen
        helpOverlayLayout.setVisibility(View.INVISIBLE);

    }

    //Funktion die das Hilfe Fenster ein- oder ausblendet
    public void helpOverlay(Context context){
        if(!isVisible()) {
            //einblenden falls es nicht sichtbar ist
            helpSlideIn(context);
        }
        else{
            //ausblenden falls es sichtbar ist
            helpSlideOut(context);
        }
    }

    //Funktion um Hilfe Fenster
  public void setInvisible(Context context){
      if(isVisible()) {
                  helpSlideOut(context);
                    }
    }

    //Testen ob Hilfe Fenster sichtbar ist
    public boolean isVisible(){
        if(helpOverlayLayout.getVisibility() == View.INVISIBLE){
            return false;
        }
        else {
            return true;
        }
    }
}
