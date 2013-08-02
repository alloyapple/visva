package vn.zgome.game.streetknight.core.util;

import vn.zgome.game.streetknight.core.GameOS;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class ProcessTextUltil {

	public static String cutString(GameOS game, String text, int wiBound, BitmapFont font)
   {		 
		 LabelStyle style = new LabelStyle(font, Color.WHITE);
		 Label demo  = new Label("123",style);
       String result = " ";
       String word = "";
       String charTemp;
       String fullWord = "";
       String fullWordTemp = "";
       int ln = text.length();
       
       for (int i = 0; i < ln; i++)
       {
           charTemp = text.substring(i,i+1);
           if (charTemp.equals(" "))
           {
               fullWordTemp = fullWord + word;               
               demo.setText(fullWordTemp);               
               if (demo.getTextBounds().width > wiBound)
               {                   
                   result += fullWord+" \n ";
                   fullWord = "";
                   fullWord = fullWord + word + " ";
                   word = "";                                      
               }
               else
               {
                   fullWord = fullWord + word + " ";
                   word = "";
                   if (i == ln - 1)
                   {                       
                       result += fullWord+" ";
                   }
               }
           }
           else
           {
               word += charTemp;
               if (i == ln - 1)
               {
                   fullWordTemp = fullWord + word;
                   demo.setText(fullWordTemp);
                   if (demo.getTextBounds().width > wiBound)
                   {
                       result += fullWord +" \n ";
                       fullWord = "";
                       fullWord = fullWord + word;
                       result += fullWord+" ";
                       word = "";
                   }
                   else
                   {
                       fullWord += word;
                       result += fullWord;
                   }
               }
           }
       }
       return result;
   }
}
