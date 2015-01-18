package com.gurusolution.android.hangman2.ui;

import java.util.HashMap;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gurusolution.android.hangman2.R;
import com.gurusolution.android.hangman2.constant.GlobalDef;
import com.gurusolution.android.hangman2.utils.GameSetting;
import com.gurusolution.android.hangman2.utils.SoundEffect;
import com.gurusolution.android.hangman2.utils.StringUtility;

public class EnterWordToGuessScreen extends Activity implements GlobalDef {
    /** Called when the activity is first created. */
    public static final int DIALOG_CONFIRMATION     = 0x02;
    public static final int DIALOG_NEW_WORD         = 0x03;
    public Dialog           alertEnterWordDialog;
    public Dialog           confirmationDialog;
    public Dialog           newWordDialog;
    public ImageButton      alertContinueButtonOK;
    public Button           btnGameOK;
    public Button           btnGameCancel;
    private ImageButton     btn_clear;
    private ImageButton     btn_continue;
    private ImageButton     btn_menu;
    private ImageButton     btn_new;
    private TextView        txt_playername;
    private EditText        edt_word;
    private String          enter_word              = "";

    /**
     * @return the enter_word
     */
    public String getEnter_word() {
        return enter_word;
    }

    /**
     * @param enter_word
     *            the enter_word to set
     */
    public void setEnter_word(String enter_word) {
        this.enter_word = enter_word;
    }

    public void setOnKeyBackClick() {
        if (enter_word.length() > 0) {
            this.enter_word = this.enter_word.substring(0, enter_word.length() - 1);
        }
        edt_word.setText(getEnter_word());
    }

    public void setOnAlphabetClick(String alphabet) {
        this.enter_word += alphabet;
        edt_word.setText(getEnter_word());
    }

    private ImageView                   char_a;
    private ImageView                   char_b;
    private ImageView                   char_c;
    private ImageView                   char_d;
    private ImageView                   char_e;
    private ImageView                   char_f;
    private ImageView                   char_g;
    private ImageView                   char_h;
    private ImageView                   char_i;
    private ImageView                   char_j;
    private ImageView                   char_k;
    private ImageView                   char_l;
    private ImageView                   char_m;
    private ImageView                   char_n;
    private ImageView                   char_o;
    private ImageView                   char_p;
    private ImageView                   char_q;
    private ImageView                   char_r;
    private ImageView                   char_s;
    private ImageView                   char_t;
    private ImageView                   char_u;
    private ImageView                   char_v;
    private ImageView                   char_w;
    private ImageView                   char_x;
    private ImageView                   char_y;
    private ImageView                   char_z;
    private ImageView                   char_del;
    private HashMap<Integer, ImageView> mCharButtons;
    String                              str_player1      = "";
    String                              str_player2      = "";
    private int                         NUMBER_OF_TRIES  = 0;
    private boolean                     player1Challenge = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_word_screen);
        initControl();
        Typeface tf = Typeface.createFromAsset(this.getAssets(), "fonts/VTK.ttf");
        // txt_playername.setTypeface(tf);
        edt_word.setTypeface(tf);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            str_player1 = extras.getString(PLAYER1);
            str_player2 = extras.getString(PLAYER2);
            player1Challenge = extras.getBoolean(PLAYER1_CHALLENGE);
            if (player1Challenge) {
                txt_playername.setText(getString(R.string.enter_word_for_play_name_to_guess, str_player2));
            } else {
                txt_playername.setText(getString(R.string.enter_word_for_play_name_to_guess, str_player1));
            }
        }
        getMapCharButtons();
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        SoundEffect.loadSound(this);
        btn_clear.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                NUMBER_OF_TRIES = 0;
                setEnter_word("");
                edt_word.setText("");
            }
        });
        btn_continue.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (getEnter_word().equals("")) {
                    Toast.makeText(EnterWordToGuessScreen.this, getString(R.string.enter_word_to_continue), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intentGameBoard = new Intent(EnterWordToGuessScreen.this, GameBoardScreen.class);
                    intentGameBoard.putExtra(PLAYER1, str_player1);
                    intentGameBoard.putExtra(PLAYER2, str_player2);
                    intentGameBoard.putExtra(CHALLENGE, getEnter_word());
                    intentGameBoard.putExtra(PLAYER1_CHALLENGE, player1Challenge);
                    startActivity(intentGameBoard);
                    finish();
                }
            }
        });
        btn_menu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialog(DIALOG_CONFIRMATION);
            }
        });
        btn_new.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialog(DIALOG_NEW_WORD);
            }
        });
    }

    public void initControl() {
        btn_clear = (ImageButton) findViewById(R.id.clear_text);
        btn_continue = (ImageButton) findViewById(R.id.btn_continue);
        btn_menu = (ImageButton) findViewById(R.id.btn_menu);
        btn_new = (ImageButton) findViewById(R.id.btn_new);
        txt_playername = (TextView) findViewById(R.id.txt_player_name);
        edt_word = (EditText) findViewById(R.id.txt_enter_word);
        char_a = (ImageView) findViewById(R.id.char_a);
        char_b = (ImageView) findViewById(R.id.char_b);
        char_c = (ImageView) findViewById(R.id.char_c);
        char_d = (ImageView) findViewById(R.id.char_d);
        char_e = (ImageView) findViewById(R.id.char_e);
        char_f = (ImageView) findViewById(R.id.char_f);
        char_g = (ImageView) findViewById(R.id.char_g);
        char_h = (ImageView) findViewById(R.id.char_h);
        char_i = (ImageView) findViewById(R.id.char_i);
        char_j = (ImageView) findViewById(R.id.char_j);
        char_k = (ImageView) findViewById(R.id.char_k);
        char_l = (ImageView) findViewById(R.id.char_l);
        char_m = (ImageView) findViewById(R.id.char_m);
        char_n = (ImageView) findViewById(R.id.char_n);
        char_o = (ImageView) findViewById(R.id.char_o);
        char_p = (ImageView) findViewById(R.id.char_p);
        char_q = (ImageView) findViewById(R.id.char_q);
        char_r = (ImageView) findViewById(R.id.char_r);
        char_s = (ImageView) findViewById(R.id.char_s);
        char_t = (ImageView) findViewById(R.id.char_t);
        char_u = (ImageView) findViewById(R.id.char_u);
        char_v = (ImageView) findViewById(R.id.char_v);
        char_w = (ImageView) findViewById(R.id.char_w);
        char_x = (ImageView) findViewById(R.id.char_x);
        char_y = (ImageView) findViewById(R.id.char_y);
        char_z = (ImageView) findViewById(R.id.char_z);
        char_del = (ImageView) findViewById(R.id.delete);

        edt_word.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!StringUtility.isEmpty(edt_word))
                    char_del.setVisibility(View.VISIBLE);
                else
                    char_del.setVisibility(View.GONE);
            }
        });

    }

    public void getMapCharButtons() {
        mCharButtons = new HashMap<Integer, ImageView>();
        mCharButtons.put(CHAR_A, char_a);
        mCharButtons.put(CHAR_B, char_b);
        mCharButtons.put(CHAR_C, char_c);
        mCharButtons.put(CHAR_D, char_d);
        mCharButtons.put(CHAR_E, char_e);
        mCharButtons.put(CHAR_F, char_f);
        mCharButtons.put(CHAR_G, char_g);
        mCharButtons.put(CHAR_H, char_h);
        mCharButtons.put(CHAR_I, char_i);
        mCharButtons.put(CHAR_J, char_j);
        mCharButtons.put(CHAR_K, char_k);
        mCharButtons.put(CHAR_L, char_l);
        mCharButtons.put(CHAR_M, char_m);
        mCharButtons.put(CHAR_N, char_n);
        mCharButtons.put(CHAR_O, char_o);
        mCharButtons.put(CHAR_P, char_p);
        mCharButtons.put(CHAR_Q, char_q);
        mCharButtons.put(CHAR_R, char_r);
        mCharButtons.put(CHAR_S, char_s);
        mCharButtons.put(CHAR_T, char_t);
        mCharButtons.put(CHAR_U, char_u);
        mCharButtons.put(CHAR_V, char_v);
        mCharButtons.put(CHAR_W, char_w);
        mCharButtons.put(CHAR_X, char_x);
        mCharButtons.put(CHAR_Y, char_y);
        mCharButtons.put(CHAR_Z, char_z);
        mCharButtons.put(CHAR_DEL, char_del);
        for (Integer num : mCharButtons.keySet()) {
            ImageView b = (ImageView) mCharButtons.get(num);
            b.setTag(num);
            b.setOnClickListener(mButtonClick);
        }

    }

    private OnClickListener mButtonClick = new OnClickListener() {

                                             @Override
                                             public void onClick(View v) {
                                                 int keyId = (Integer) v.getTag();
                                                 if (keyId == CHAR_DEL) {
                                                     if (NUMBER_OF_TRIES != 0) {
                                                         setOnKeyBackClick();
                                                         NUMBER_OF_TRIES--;
                                                     }

                                                 } else {
                                                     if (NUMBER_OF_TRIES < 10) {
                                                         setOnAlphabetClick(ALPHABET[keyId - 1].toString());
                                                         NUMBER_OF_TRIES++;
                                                     }
                                                 }
                                             }

                                         };

    @Override
    protected Dialog onCreateDialog(int id) {

        switch (id) {
        case DIALOG_CONFIRMATION:
            confirmationDialog = new Dialog(this, R.style.Theme_CustomDialog) {
                @Override
                public boolean onKeyDown(int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_MENU) {
                        return true;
                    }
                    return super.onKeyDown(keyCode, event);
                }
            };
            confirmationDialog.setContentView(R.layout.confirmation_dialog);
            confirmationDialog.setCancelable(false);
            btnGameOK = (Button) confirmationDialog.findViewById(R.id.gameok);
            btnGameCancel = (Button) confirmationDialog.findViewById(R.id.gamecancel);
            btnGameOK.setOnClickListener(onGameOk);
            btnGameCancel.setOnClickListener(onGameCancel);
            return confirmationDialog;
        case DIALOG_NEW_WORD:
            newWordDialog = new Dialog(this, R.style.Theme_CustomDialog) {
                @Override
                public boolean onKeyDown(int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_MENU) {
                        return true;
                    }
                    return super.onKeyDown(keyCode, event);
                }
            };
            newWordDialog.setContentView(R.layout.confirmation_dialog);
            newWordDialog.setCancelable(false);
            btnGameOK = (Button) newWordDialog.findViewById(R.id.gameok);
            btnGameCancel = (Button) newWordDialog.findViewById(R.id.gamecancel);
            btnGameOK.setOnClickListener(onNewWordOk);
            btnGameCancel.setOnClickListener(onNewWordCancel);
            return newWordDialog;
        default:
            break;
        }
        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {

        super.onPrepareDialog(id, dialog);
        TextView txt_info;
        TextView txt_new_word;
        switch (id) {
        case DIALOG_CONFIRMATION:
            txt_info = (TextView) confirmationDialog.findViewById(R.id.txtinfo);
            txt_new_word = (TextView) confirmationDialog.findViewById(R.id.txt_new_word);
            txt_info.setVisibility(View.VISIBLE);
            txt_new_word.setVisibility(View.GONE);
            break;
        case DIALOG_NEW_WORD:
            txt_info = (TextView) newWordDialog.findViewById(R.id.txtinfo);
            txt_new_word = (TextView) newWordDialog.findViewById(R.id.txt_new_word);
            txt_info.setVisibility(View.GONE);
            txt_new_word.setVisibility(View.VISIBLE);
            break;
        default:
            break;
        }
    }

    private Button.OnClickListener onNewWordOk        = new ImageButton.OnClickListener() {

                                                          @Override
                                                          public void onClick(View arg0) {
                                                              NUMBER_OF_TRIES = 0;
                                                              newWordDialog.dismiss();
                                                              setEnter_word("");
                                                              edt_word.setText("");
                                                          }
                                                      };
    private Button.OnClickListener onNewWordCancel    = new ImageButton.OnClickListener() {

                                                          @Override
                                                          public void onClick(View arg0) {
                                                              newWordDialog.dismiss();
                                                          }
                                                      };
    private Button.OnClickListener onContinueDialogOk = new ImageButton.OnClickListener() {

                                                          @Override
                                                          public void onClick(View arg0) {
                                                              alertEnterWordDialog.dismiss();

                                                          }
                                                      };

    private Button.OnClickListener onGameOk           = new ImageButton.OnClickListener() {

                                                          @Override
                                                          public void onClick(View arg0) {
                                                              confirmationDialog.dismiss();
                                                              Intent _playSettingIntent = null;
                                                              if (GameSetting._game_mode == ONE_PLAYER_MODE)
                                                                  _playSettingIntent = new Intent(EnterWordToGuessScreen.this,
                                                                          PlayerSettingsScreen.class);
                                                              else
                                                                  _playSettingIntent = new Intent(EnterWordToGuessScreen.this,
                                                                          TwoPlayerSettingScreen.class);
                                                              _playSettingIntent.putExtra(GAME_MODE, TWO_PLAYER_MODE);
                                                              startActivity(_playSettingIntent);
                                                              finish();
                                                          }
                                                      };
    private Button.OnClickListener onGameCancel       = new ImageButton.OnClickListener() {

                                                          @Override
                                                          public void onClick(View arg0) {
                                                              confirmationDialog.dismiss();
                                                          }
                                                      };

    @Override
    public void onBackPressed() {
        showDialog(DIALOG_CONFIRMATION);
    }

}