package de.jcm.ineedhealingclicker.render;

import android.graphics.Canvas;
import android.util.Log;

import static java.lang.Thread.sleep;

/**
 * Created by JCM on 05.11.2018.
 */
public class GameRenderThread implements Runnable
{
	private static final String TAG = "GameRenderThread";

	private final GameSurfaceView view;

	private boolean running;

	public GameRenderThread(GameSurfaceView view)
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
			synchronized(view)
			{
				Canvas canvas = null;
				try
				{
					canvas = view.getHolder().lockCanvas();
					synchronized(canvas)
					{
						view.renderTick();
						view.draw(canvas);
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				finally
				{
					if(canvas != null)
						view.getHolder().unlockCanvasAndPost(canvas);
				}
			}
			long timeToDraw = System.nanoTime() - nanos;

			if(timeToDraw > 50000000)
			{
				Log.w(TAG, "LAG! " + timeToDraw);
				timeToDraw = 50000000;
			}

			try
			{
				sleep(50 - (timeToDraw / 1000000));    //Try to sync at 20 FPS
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
