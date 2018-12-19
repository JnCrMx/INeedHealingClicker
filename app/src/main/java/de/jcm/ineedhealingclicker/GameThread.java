package de.jcm.ineedhealingclicker;

import android.util.Log;

import de.jcm.ineedhealingclicker.render.GameSurfaceView;

import static java.lang.Thread.sleep;

/**
 * Created by JCM on 03.11.2018.
 */
public class GameThread implements Runnable
{
	private static final String TAG = "GameThread";
	public static int TIME_PER_TICK = 50;
	private final GameSurfaceView view;

	private boolean running;

	public GameThread(GameSurfaceView view)
	{
		this.view = view;
	}

	@Override
	public void run()
	{
		Log.i(TAG, "Started!");
		while(running)
		{
			long nanos = System.nanoTime();
			//synchronized(view)
			{
				try
				{
					view.tick();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			long timeToTick = System.nanoTime() - nanos;

			if(timeToTick > 1000000 * TIME_PER_TICK)
			{
				Log.e(TAG, "LAG! " + timeToTick);
				timeToTick = 1000000 * TIME_PER_TICK;
			}

			try
			{
				sleep(TIME_PER_TICK - (timeToTick / 1000000));    //Try to sync at 20 TPS
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		Log.i(TAG, "Terminated!");
	}

	public boolean isRunning()
	{
		return running;
	}

	public void setRunning(boolean running)
	{
		this.running = running;

	}
}
