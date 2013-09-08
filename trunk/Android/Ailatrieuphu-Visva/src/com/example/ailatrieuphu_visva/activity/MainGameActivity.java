package com.example.ailatrieuphu_visva.activity;

import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.ailatrieuphu_visva.R;
import com.example.ailatrieuphu_visva.db.DBConnector;
import com.example.ailatrieuphu_visva.db.Question;
import com.example.ailatrieuphu_visva.utils.Helpers;

@SuppressWarnings("deprecation")
public class MainGameActivity extends Activity {
	private static final int ID_DIALOG_CONFIRM_USE_50_50 = 0;
	private static final int ID_DIALOG_CONFIRM_USE_AUDIENCE = 1;
	private static final int ID_DIALOG_CONFIRM_USE_CALL = 2;
	private static final int ID_DIALOG_CONFIRM_USE_CHANGE_QUESTION = 3;
	private TextView _txt_question_number;
	private TextView _txt_question_content;
	private Button _btn_help_50_50;
	private boolean _is_use_50_50 = false;
	private int _case_false_in_50_50 = -1;
	private Button _btn_help_call_relatives;
	private Button _btn_help_audience;
	private Button _btn_help_change_question;

	private int _level = 1;
	private int[] _question_sound_id = new int[] { R.raw._altp_sound_question_01, R.raw._altp_sound_question_02, R.raw._altp_sound_question_03, R.raw._altp_sound_question_04, R.raw._altp_sound_question_05, R.raw._altp_sound_question_06, R.raw._altp_sound_question_07, R.raw._altp_sound_question_08, R.raw._altp_sound_question_09, R.raw._altp_sound_question_10, R.raw._altp_sound_question_11, R.raw._altp_sound_question_12, R.raw._altp_sound_question_13, R.raw._altp_sound_question_14,
			R.raw._altp_sound_question_15, R.raw._altp_sound_best_player };
	private int[] _answer_wrong_sound_id = new int[] { R.raw._altp_sound_answer_wrong_a, R.raw._altp_sound_answer_wrong_b, R.raw._altp_sound_answer_wrong_c, R.raw._altp_sound_answer_wrong_d };
	private int[] _answer_sound_id = new int[] { R.raw._altp_sound_answer_a, R.raw._altp_sound_answer_b, R.raw._altp_sound_answer_c, R.raw._altp_sound_answer_d };
	private int[] _suggest_experts_id = new int[] { R.string._txt_msg_help_of_experts_0, R.string._txt_msg_help_of_experts_1, R.string._txt_msg_help_of_experts_2, R.string._txt_msg_help_of_experts_3, R.string._txt_msg_help_of_experts_4, R.string._txt_msg_help_of_experts_5 };
	private MediaPlayer _sound_bg;
	private MediaPlayer _sound_effect;
	private DBConnector _db_conector;
	private Random random = new Random();
	private int _my_answer = -1;
	private Question _current_question;
	private AnimationDrawable _anim_answer;
	private Dialog _dialog_save_score;
	private Dialog _dialog_select_experts;
	private Dialog _dialog_help_audience;

	private int _time = 600;
	private int _money = 0;
	private OnClickListener _listener_onclick_help_50_50 = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			showDialog(ID_DIALOG_CONFIRM_USE_50_50);
		}
	};

	private OnClickListener _listener_onclick_help_call_relatives = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			showDialog(ID_DIALOG_CONFIRM_USE_CALL);
		}
	};
	private OnClickListener _listener_onclick_help_audience = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			showDialog(ID_DIALOG_CONFIRM_USE_AUDIENCE);
		}
	};
	private OnClickListener _listener_onclick_help_change_question = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			showDialog(ID_DIALOG_CONFIRM_USE_CHANGE_QUESTION);
		}
	};

	private Button _btn_answer[] = new Button[4];
	private OnClickListener _listener_onclick_answer_a = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			confirmSelectAnswer(0);
		}
	};

	private OnClickListener _listener_onclick_answer_b = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			confirmSelectAnswer(1);
		}
	};
	private OnClickListener _listener_onclick_answer_c = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			confirmSelectAnswer(2);
		}
	};
	private OnClickListener _listener_onclick_answer_d = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			confirmSelectAnswer(3);
		}
	};
	private OnClickListener _listener_onclick_oki_save_score = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			onClickOkiSaveScore();
		}
	};
	private OnClickListener _listener_onclick_cancel_save_score = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			onClickCancelSaveScore();
		}
	};
	private Runnable task_count_down_time = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			long current_time, begin_time;
			current_time = begin_time = System.currentTimeMillis();
			while (_time > 0) {
				current_time = System.currentTimeMillis();
				if (current_time - begin_time >= 1000) {
					_time--;
					begin_time = current_time;
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							_txt_time.setText(Helpers.parse_seconds_to_time_string(_time));
						}
					});
				}
				try {
					Thread.sleep(10L);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			out_time();
		}
	};
	private TextView _txt_time;
	private TextView _txt_money;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout._altp_layout_main_game);
		_btn_help_50_50 = (Button) findViewById(R.id._layout_main_game_btn_help_50_50);
		_btn_help_50_50.setOnClickListener(_listener_onclick_help_50_50);
		_btn_help_call_relatives = (Button) findViewById(R.id._layout_main_game_btn_help_call_relatives);
		_btn_help_call_relatives.setOnClickListener(_listener_onclick_help_call_relatives);
		_btn_help_audience = (Button) findViewById(R.id._layout_main_game_btn_help_audience);
		_btn_help_audience.setOnClickListener(_listener_onclick_help_audience);
		_btn_help_change_question = (Button) findViewById(R.id._layout_main_game_btn_help_change_question);
		_btn_help_change_question.setOnClickListener(_listener_onclick_help_change_question);
		_btn_answer[0] = (Button) findViewById(R.id._layout_main_game_btn_answer_a);
		_btn_answer[0].setOnClickListener(_listener_onclick_answer_a);
		_btn_answer[1] = (Button) findViewById(R.id._layout_main_game_btn_answer_b);
		_btn_answer[1].setOnClickListener(_listener_onclick_answer_b);
		_btn_answer[2] = (Button) findViewById(R.id._layout_main_game_btn_answer_c);
		_btn_answer[2].setOnClickListener(_listener_onclick_answer_c);
		_btn_answer[3] = (Button) findViewById(R.id._layout_main_game_btn_answer_d);
		_btn_answer[3].setOnClickListener(_listener_onclick_answer_d);
		_txt_time = (TextView) findViewById(R.id._layout_main_game_txt_time);
		_txt_time.setText(Helpers.parse_seconds_to_time_string(_time));
		_txt_money = (TextView) findViewById(R.id._layout_main_game_txt_money);
		_txt_money.setText(_money + " $");
		_txt_question_content = (TextView) findViewById(R.id._layout_main_game_txt_question_content);
		_txt_question_number = (TextView) findViewById(R.id._layout_main_game_txt_question_number);

		_db_conector = new DBConnector(this);
		_db_conector.openDataBase();
		new Thread(task_count_down_time).start();
		toNextQuestion(_level);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Helpers.releaseSound(_sound_bg);
		if (_db_conector != null) {
			_db_conector.close();
			_db_conector = null;
		}
		super.onPause();
	}

	@Override
	protected void onResume() {

		// TODO Auto-generated method stub
		if (_db_conector == null) {
			_db_conector = new DBConnector(this);
			_db_conector.openDataBase();
		}
		super.onResume();

	}

	public static void startActivity(Activity activity) {
		Intent intent = new Intent(activity, MainGameActivity.class);
		activity.startActivity(intent);
	}

	private void toNextQuestion(final int level) {
		if (level < 15) {
			new Thread() {
				public void run() {
					_current_question = _db_conector.getData(level);
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							lockAnswer(false);
							bindDataToView(_current_question);
						}
					});
					_sound_effect = Helpers.playSound(getContext(), _question_sound_id[level - 1], false);
					Helpers.wait_sound(_sound_effect);
					if (level == 5 || level == 10 || level == 15)
						_sound_effect = Helpers.playSound(getContext(), R.raw._altp_sound_important_level, false);
					if (level < 6) {
						_sound_bg = Helpers.playSound(getContext(), R.raw._altp_sound_play_bg_01, true);
					} else if (level < 11) {
						_sound_bg = Helpers.playSound(getContext(), R.raw._altp_sound_play_bg_02, true);
					} else {
						_sound_bg = Helpers.playSound(getContext(), R.raw._altp_sound_play_bg_03, true);
					}
					_my_answer = -1;
				}
			}.start();
		} else {
			creat_dialog_save_score(getContext());
		}
	}

	public void bindDataToView(Question question) {
		String msg = getResources().getString(R.string._txt_question_number);
		_txt_question_number.setText(msg.replace("1", String.valueOf(question.getLevel())));
		_txt_question_content.setText(question.get_question_content());
		for (int i = 0; i < 4; i++) {
			_btn_answer[i].setText(getStringIndex(i) + " : " + question.get_answer(i));
			_btn_answer[i].setBackgroundResource(R.drawable.altp_btn_answer_normal_);
		}

	}

	public void lockAnswer(boolean b) {
		for (int i = 0; i < 4; i++)
			_btn_answer[i].setEnabled(!b);
	}

	public void confirmSelectAnswer(final int answer) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		String msg = getResources().getString(R.string._txt_msg_dialog_select_answer_confirm);
		builder.setMessage(msg.replace("%", getStringIndex(answer)));
		builder.setPositiveButton(getResources().getString(R.string._txt_btn_confirm_oki_select), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				selectAnswer(answer);
			}
		});
		builder.setNegativeButton(getResources().getString(R.string._txt_btn_confirm_cancel_select), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});
		builder.create().show();
	}

	public void selectAnswer(final int answer) {
		new Thread() {
			public void run() {
				int id_sound = _answer_sound_id[answer];
				Helpers.releaseSound(_sound_bg);
				_sound_effect = Helpers.playSound(getContext(), id_sound, false);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						_btn_answer[answer].setBackgroundResource(R.drawable._altp_answer_accept_state);
						_my_answer = answer + 1;
						lockAnswer(true);
					}
				});
				Helpers.wait_sound(_sound_effect);
				_sound_effect = Helpers.playSound(getContext(), R.raw._altp_sound_given_answer_01, false);
				Helpers.wait_sound(_sound_effect);
				final int _correct_answer = _current_question.get_correct_answer();
				if (_correct_answer == _my_answer) {
					if (_correct_answer == 1) {
						_sound_effect = Helpers.playSound(getContext(), R.raw._altp_sound_answer_true_a, false);
					} else if (_correct_answer == 2) {
						_sound_effect = Helpers.playSound(getContext(), R.raw._altp_sound_answer_true_b, false);
					} else {
						int r = random.nextInt(2);
						if (_correct_answer == 3) {
							if (r == 0)
								_sound_effect = Helpers.playSound(getContext(), R.raw._altp_sound_answer_true_c_01, false);
							else if (r == 1)
								_sound_effect = Helpers.playSound(getContext(), R.raw._altp_sound_answer_true_c_02, false);
						} else if (_correct_answer == 4) {
							if (r == 0)
								_sound_effect = Helpers.playSound(getContext(), R.raw._altp_sound_answer_true_d_01, false);
							else if (r == 1)
								_sound_effect = Helpers.playSound(getContext(), R.raw._altp_sound_answer_true_d_02, false);
						}
					}
				} else {
					_sound_effect = Helpers.playSound(getContext(), _answer_wrong_sound_id[_correct_answer - 1], false);
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						_btn_answer[_correct_answer - 1].setBackgroundResource(R.drawable.altp_btn_anim_answer_);
						_anim_answer = (AnimationDrawable) _btn_answer[_correct_answer - 1].getBackground();
						_anim_answer.start();
					}
				});
				Helpers.wait_sound(_sound_effect);
				if (_level == 15 && _correct_answer == _my_answer) {
					_sound_effect = Helpers.playSound(getContext(), R.raw._altp_sound_win_level_14, false);
					Helpers.wait_sound(_sound_effect);
				}
				if (_correct_answer == _my_answer) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							_txt_money.setText(caculate_money(_level) + " $");
						}
					});
					_level++;
					toNextQuestion(_level);
				} else {
					_sound_effect = Helpers.playSound(getContext(), R.raw._altp_sound_lose_game, false);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							creat_dialog_save_score(getContext());
						}
					});
				}
			}
		}.start();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch (id) {
		case ID_DIALOG_CONFIRM_USE_50_50:
			builder.setMessage(getResources().getString(R.string._msg_confirm_use_50_50));
			builder.setPositiveButton(getResources().getString(R.string._txt_btn_confirm_oki_select), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					_btn_help_50_50.setBackgroundResource(R.drawable._altp_help_50_50_press);
					_btn_help_50_50.setEnabled(false);
					help_50_50();
				}
			});
			builder.setNegativeButton(getResources().getString(R.string._txt_btn_confirm_cancel_select), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub

				}
			});
			return builder.create();
		case ID_DIALOG_CONFIRM_USE_AUDIENCE:
			builder.setMessage(getResources().getString(R.string._msg_confirm_use_audience));
			builder.setPositiveButton(getResources().getString(R.string._txt_btn_confirm_oki_select), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					_btn_help_audience.setBackgroundResource(R.drawable._altp_help_audience_press);
					_btn_help_audience.setEnabled(false);
					help_audience();
				}
			});
			builder.setNegativeButton(getResources().getString(R.string._txt_btn_confirm_cancel_select), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub

				}
			});
			return builder.create();
		case ID_DIALOG_CONFIRM_USE_CALL:
			builder.setMessage(getResources().getString(R.string._msg_confirm_use_call));
			builder.setPositiveButton(getResources().getString(R.string._txt_btn_confirm_oki_select), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					_btn_help_call_relatives.setBackgroundResource(R.drawable._altp_help_call_relatives_press);
					_btn_help_call_relatives.setEnabled(false);
					help_call_relatives();
				}
			});
			builder.setNegativeButton(getResources().getString(R.string._txt_btn_confirm_cancel_select), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub

				}
			});
			return builder.create();
		case ID_DIALOG_CONFIRM_USE_CHANGE_QUESTION:
			builder.setMessage(getResources().getString(R.string._msg_confirm_use_change_question));
			builder.setPositiveButton(getResources().getString(R.string._txt_btn_confirm_oki_select), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					_btn_help_change_question.setBackgroundResource(R.drawable._altp_help_change_question_press);
					_btn_help_change_question.setEnabled(false);
					help_call_change_question();
				}
			});
			builder.setNegativeButton(getResources().getString(R.string._txt_btn_confirm_cancel_select), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub

				}
			});
			return builder.create();
		default:
			break;
		}
		return super.onCreateDialog(id);
	}

	private void out_time() {
	}

	public void help_50_50() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Helpers.wait_sound(_sound_effect);
				_sound_effect = Helpers.playSound(getContext(), R.raw._altp_sound_help_50_50, false);
				Helpers.wait_sound(_sound_effect);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						_case_false_in_50_50 = caculate_50_50();
						_is_use_50_50 = true;
					}
				});

			}
		}).start();
	}

	private int caculate_50_50() {
		int remove1, remove2;
		do {
			remove1 = 1 + random.nextInt(4);
		} while (remove1 == _current_question.get_correct_answer());
		_btn_answer[remove1 - 1].setText("");
		_btn_answer[remove1 - 1].setEnabled(false);
		do {
			remove2 = 1 + random.nextInt(4);
		} while (remove2 == _current_question.get_correct_answer() || remove2 == remove1);
		_btn_answer[remove2 - 1].setText("");
		_btn_answer[remove2 - 1].setEnabled(false);
		return 10 - remove1 - remove2 - _current_question.get_correct_answer();
	}

	public void help_audience() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Helpers.wait_sound(_sound_effect);
				_sound_effect = Helpers.playSound(getContext(), R.raw._altp_sound_help_audience, false);
				Helpers.wait_sound(_sound_effect);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						creat_dialog_help_audience(caculate_help_audience(_is_use_50_50), _is_use_50_50, getContext());
					}
				});
			}
		}).start();
	}

	private int[] caculate_help_audience(boolean _is_use_50_50) {
		int rate[] = new int[4];
		if (_is_use_50_50) {
			rate[0] = rate[1] = rate[2] = rate[3] = 0;
			rate[_current_question.get_correct_answer() - 1] = random.nextInt(50 + _level * 3) - _level * 3 + 50;
			rate[_case_false_in_50_50 - 1] = 100 - rate[_current_question.get_correct_answer() - 1];
		} else {
			rate[0] = random.nextInt(50 + _level * 3) - _level * 3 + 50;
			rate[1] = random.nextInt(100 - rate[0]);
			rate[2] = random.nextInt(100 - rate[0] - rate[1]);
			rate[3] = 100 - rate[0] - rate[1] - rate[2];
			int temp = rate[0];
			rate[0] = rate[_current_question.get_correct_answer() - 1];
			rate[_current_question.get_correct_answer() - 1] = temp;
		}
		return rate;
	}

	public void help_call_relatives() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Helpers.wait_sound(_sound_effect);
				_sound_effect = Helpers.playSound(getContext(), R.raw._altp_sound_help_call_family, false);
				Helpers.wait_sound(_sound_effect);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						creat_dialog_select_experts(getContext());
					}
				});
			}
		}).start();
	}

	public String suggest_of_experts(boolean is_random, int experts) {
		String txt_suggest = getResources().getString(_suggest_experts_id[experts]);
		if (is_random) {

		}
		return txt_suggest.replace("%x%", getStringIndex(_current_question.get_correct_answer() - 1));
	}

	public String onClickSelectExperts(int experts) {
		int r = random.nextInt(100);
		switch (experts) {
		case 0:
			if (((_level < 6 || _level > 10) && r < 60) || (_level > 5 && _level < 11 && r < 80)) {
				return suggest_of_experts(false, experts);
			}
			break;
		case 1:
			if ((_level < 6 && r < 90) || (_level > 5 && r < 60)) {
				return suggest_of_experts(false, experts);
			}
			break;
		case 2:
			if ((_level < 6 && r < 90) || (_level > 5 && r < 80)) {
				return suggest_of_experts(false, experts);
			}
			break;
		case 3:
			if ((_level > 10 && r < 90) || (_level < 11 && r < 65)) {
				return suggest_of_experts(false, experts);
			}
			break;
		case 4:
			if (((_level < 6 || _level > 10) && r < 70) || (_level > 5 && _level < 11 && r < 90)) {
				return suggest_of_experts(false, experts);
			}
			break;
		case 5:
			if ((_level > 10 && r < 85) || (_level < 11 && r < 70)) {
				return suggest_of_experts(false, experts);
			}
			break;
		default:
			break;
		}
		return suggest_of_experts(true, experts);
	}

	public void help_call_change_question() {
		Helpers.releaseSound(_sound_bg);
		toNextQuestion(_level);
	}

	private String getStringIndex(int answer) {
		switch (answer) {
		case 0:
			return "A";
		case 1:
			return "B";
		case 2:
			return "C";
		case 3:
			return "D";
		default:
			break;
		}
		return null;
	}

	public Context getContext() {
		return this;
	}

	private void onClickCancelSaveScore() {
		finish();
	}

	private void onClickOkiSaveScore() {
		finish();
	}

	public void creat_dialog_save_score(Context mContext) {
		_dialog_save_score = new Dialog(mContext);
		_dialog_save_score.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		_dialog_save_score.requestWindowFeature(Window.FEATURE_NO_TITLE);
		LayoutInflater inflater = (LayoutInflater) ((Activity) mContext).getLayoutInflater();
		View dialog_view = inflater.inflate(R.layout._altp_layout_dialog_save_score, null);
		Button btn_oki = (Button) dialog_view.findViewById(R.id._layout_dialog_save_score_oki);
		btn_oki.setOnClickListener(_listener_onclick_oki_save_score);
		Button btn_dont_known = (Button) dialog_view.findViewById(R.id._layout_dialog_save_score_cancel);
		btn_dont_known.setOnClickListener(_listener_onclick_cancel_save_score);
		_dialog_save_score.setContentView(dialog_view);
		_dialog_save_score.setCancelable(false);
		_dialog_save_score.show();
	}

	public void creat_dialog_select_experts(Context mContext) {
		_dialog_select_experts = new Dialog(mContext);
		_dialog_select_experts.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		_dialog_select_experts.requestWindowFeature(Window.FEATURE_NO_TITLE);
		LayoutInflater inflater = (LayoutInflater) ((Activity) mContext).getLayoutInflater();
		View dialog_view = inflater.inflate(R.layout._altp_layout_dialog_select_experts, null);
		ImageButton _btn_experts[] = new ImageButton[6];
		_btn_experts[0] = (ImageButton) dialog_view.findViewById(R.id.dialog_select_experts_baochaungo);
		_btn_experts[1] = (ImageButton) dialog_view.findViewById(R.id.dialog_select_experts_billgate);
		_btn_experts[2] = (ImageButton) dialog_view.findViewById(R.id.dialog_select_experts_einstein);
		_btn_experts[3] = (ImageButton) dialog_view.findViewById(R.id.dialog_select_experts_khongminh);
		_btn_experts[4] = (ImageButton) dialog_view.findViewById(R.id.dialog_select_experts_lanlevan);
		_btn_experts[5] = (ImageButton) dialog_view.findViewById(R.id.dialog_select_experts_paul);
		final TextView _txt_title = (TextView) dialog_view.findViewById(R.id.dialog_help_experts_title);
		final TextView _txt_suggest = (TextView) dialog_view.findViewById(R.id.dialog_help_experts_suggest);
		final Button _btn_back = (Button) dialog_view.findViewById(R.id._layout_dialog_help_experts_oki);
		final TableLayout _table_select_experts = (TableLayout) dialog_view.findViewById(R.id.dialog_select_experts_container);
		for (int i = 0; i < 6; i++) {
			final int experts = i;
			_btn_experts[i].setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String suggest = onClickSelectExperts(experts);
					_txt_title.setText(getContext().getResources().getString(R.string._txt_title_help_experts));
					_txt_suggest.setVisibility(View.VISIBLE);
					_btn_back.setVisibility(View.VISIBLE);
					_table_select_experts.setVisibility(View.GONE);
					_txt_suggest.setText(suggest);
				}
			});
		}
		_btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				_dialog_select_experts.dismiss();
			}
		});
		_dialog_select_experts.setContentView(dialog_view);
		_dialog_select_experts.show();
	}

	public void creat_dialog_help_audience(int[] rate, boolean is_use_5050, Context mContext) {
		TextView _txt_percent_audience[] = new TextView[4];
		TextView _txt_rate_audience[] = new TextView[4];
		_dialog_help_audience = new Dialog(mContext);
		_dialog_help_audience.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		_dialog_help_audience.requestWindowFeature(Window.FEATURE_NO_TITLE);
		LayoutInflater inflater = (LayoutInflater) ((Activity) mContext).getLayoutInflater();
		View dialog_view = inflater.inflate(R.layout._altp_layout_dialog_help_audience, null);
		_dialog_help_audience.setContentView(dialog_view);
		_txt_percent_audience[0] = (TextView) dialog_view.findViewById(R.id.dialog_help_audience_rate_percent_a);
		_txt_percent_audience[1] = (TextView) dialog_view.findViewById(R.id.dialog_help_audience_rate_percent_b);
		_txt_percent_audience[2] = (TextView) dialog_view.findViewById(R.id.dialog_help_audience_rate_percent_c);
		_txt_percent_audience[3] = (TextView) dialog_view.findViewById(R.id.dialog_help_audience_rate_percent_d);

		_txt_rate_audience[0] = (TextView) dialog_view.findViewById(R.id.dialog_help_audience_rate_a);
		_txt_rate_audience[1] = (TextView) dialog_view.findViewById(R.id.dialog_help_audience_rate_b);
		_txt_rate_audience[2] = (TextView) dialog_view.findViewById(R.id.dialog_help_audience_rate_c);
		_txt_rate_audience[3] = (TextView) dialog_view.findViewById(R.id.dialog_help_audience_rate_d);
		Button _btn_back = (Button) dialog_view.findViewById(R.id._layout_dialog_help_audience_oki);
		_btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				_dialog_help_audience.dismiss();
			}
		});
		if (_is_use_50_50) {
			((LinearLayout.LayoutParams) _txt_rate_audience[_current_question.get_correct_answer() - 1].getLayoutParams()).weight = (float) rate[_current_question.get_correct_answer() - 1] / 100;
			_txt_percent_audience[_current_question.get_correct_answer() - 1].setText(rate[_current_question.get_correct_answer() - 1] + "%");
			((LinearLayout.LayoutParams) _txt_rate_audience[_case_false_in_50_50 - 1].getLayoutParams()).weight = (float) rate[_case_false_in_50_50 - 1] / 100;
			_txt_percent_audience[_case_false_in_50_50 - 1].setText(rate[_case_false_in_50_50 - 1] + "%");
		} else {
			for (int i = 0; i < 4; i++) {
				((LinearLayout.LayoutParams) _txt_rate_audience[i].getLayoutParams()).weight = (float) rate[i] / 100;
				_txt_percent_audience[i].setText(rate[i] + "%");
			}
		}
		_dialog_help_audience.show();
	}

	public String caculate_money(int level) {
		switch (level) {
		case 1:
			return "100";
		case 2:
			return "200";
		case 3:
			return "300";
		case 4:
			return "500";
		case 5:
			return "1 000";
		case 6:
			return "2 000";
		case 7:
			return "3 600";
		case 8:
			return "6 000";
		case 9:
			return "9 000";
		case 10:
			return "15 000";
		case 11:
			return "25 000";
		case 12:
			return "35 000";
		case 13:
			return "50 000";
		case 14:
			return "80 000";
		case 15:
			return "120 000";
		default:
			break;
		}
		return "0";
	}
}
