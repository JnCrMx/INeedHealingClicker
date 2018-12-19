package de.jcm.ineedhealingclicker;

import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import de.jcm.ineedhealingclicker.render.GameSurfaceView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends AppCompatActivity
{
	public SharedPreferences gameData;
	private GameSurfaceView gameSurfaceView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		try
		{
			super.onCreate(savedInstanceState);

			this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

			fullscreen();

			this.gameData = getSharedPreferences(getString(R.string.game_data), MODE_PRIVATE);

			this.gameSurfaceView = new GameSurfaceView(this);
			setContentView(gameSurfaceView);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus)
	{
		super.onWindowFocusChanged(hasFocus);
		if(hasFocus)
		{
			fullscreen();
		}
	}

	private void fullscreen()
	{
		// Hide UI first
		ActionBar actionBar = getSupportActionBar();
		if(actionBar != null)
		{
			actionBar.hide();
		}

		View decorView = getWindow().getDecorView();
		decorView.setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
						// Set the content to appear under the system bars so that the
						// content doesn't resize when the system bars hide and show.
						| View.SYSTEM_UI_FLAG_LAYOUT_STABLE
						| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
						// Hide the nav bar and status bar
						| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_FULLSCREEN);
	}

	@Override
	public void onBackPressed()
	{
		if(gameSurfaceView.screens.size() < 2)
		{
			super.onBackPressed();
		}
		else
		{
			gameSurfaceView.hideScreen();
		}
	}
}
