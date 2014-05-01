package com.vsv.android.funnybird;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.flurry.android.FlurryAgent;


public class MenuActivity extends Activity {
    private int mStoryMode;
	public ActivityManager actManager = null;
	private List<RunningAppProcessInfo> runningProcInfo;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ExceptionHandler.register(getApplicationContext(), "http://crashes.vv-master.com/android-remote-stacktrace/server.php", "crashes", "!androidteam!");
        // Run as full-screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.main);

        final ImageButton btn_readToMe = (ImageButton)findViewById(R.id.btn_readToMe);
        btn_readToMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mStoryMode = StoryActionActivity.MODE_READ_TO_ME_ORIGINAL;
                if (ownRecordingExists()) {
                    askOwnRecordingReadPageAndStartStory();
                } else {
                    askLastReadPageAndStartStory();
                }
            }
        });
        final ImageButton btn_readMyself = (ImageButton)findViewById(R.id.btn_readMyself);
        btn_readMyself.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStoryMode = StoryActionActivity.MODE_READ_MYSELF;
                askLastReadPageAndStartStory();
            }
        });
        final ImageButton btn_record = (ImageButton)findViewById(R.id.btn_record);
        btn_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StoryActionActivity.startIt(MenuActivity.this, StoryActionActivity.MODE_RECORD, 0);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FlurryAgent.onStartSession(this, PagesManager.FLURRY_KEY);
    }

    @Override
    public void onPause() {
        super.onPause();
        AudioPlayer.getInstance(this).stop();
    }

    @Override
    public void onResume() {
        super.onResume();
        AudioPlayer.getInstance(this).playMainMenu();
    }

    @Override
    public void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AudioPlayer.getInstance(this).onDestroy();
    }

    private boolean ownRecordingExists() {
        final AudioPlayer player = AudioPlayer.getInstance(this);
        boolean exists = false;
        for (int i = 1; i <= PagesManager.PAGE_COUNT; ++i) {
            exists = exists || player.hasOwnRecording(PageData.getPageIdForIndex(i));
        }

        return exists;
    }

    private void askOwnRecordingReadPageAndStartStory() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
        builder.setMessage("Would you like to listen to your own record or the original one?")
                .setPositiveButton("My Own", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mStoryMode = StoryActionActivity.MODE_READ_TO_ME_MYOWN;
                        askLastReadPageAndStartStory();
                    }
                })
                .setNeutralButton("Original", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mStoryMode = StoryActionActivity.MODE_READ_TO_ME_ORIGINAL;
                        askLastReadPageAndStartStory();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void askLastReadPageAndStartStory() {
        final int lastReadPage = StoryActionActivity.getLastReadPage(MenuActivity.this);
        if (lastReadPage != 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
            builder.setMessage("Would you like to continue where you left off?")
                    .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            StoryActionActivity.startIt(MenuActivity.this, mStoryMode, lastReadPage);
                        }
                    })
                    .setNeutralButton("Start Over", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            StoryActionActivity.startIt(MenuActivity.this, mStoryMode, 0);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert = builder.create();
            alert.show();
        } else {
            StoryActionActivity.startIt(MenuActivity.this, mStoryMode, lastReadPage);
        }
    }
    public static void startIt(Activity caller) {
        Intent intent = new Intent(caller, MenuActivity.class);
        caller.startActivity(intent);
    }
    /*
	 * free memory after sign out app
	 */
	private void freeMemory() {
		actManager = (ActivityManager) this.getSystemService(Activity.ACTIVITY_SERVICE);
		runningProcInfo = ((ActivityManager) actManager).getRunningAppProcesses();

		for (int i = 0; i < runningProcInfo.size(); i++) {
			String str = runningProcInfo.get(i).processName.toString();
			PackageInfo pi;
			try {
				pi = getPackageManager().getPackageInfo(str, 0);
				String packageName = pi.applicationInfo.packageName;
				if (packageName.equals(this.getPackageName()))
					actManager.restartPackage(packageName);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			freeMemory();
			return true;

		}
		if (keyCode == KeyEvent.KEYCODE_CALL) {
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
