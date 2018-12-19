package de.jcm.ineedhealingclicker.screens;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

import java.util.Random;

import de.jcm.ineedhealingclicker.LoadingThread;
import de.jcm.ineedhealingclicker.render.GameSurfaceView;
import de.jcm.ineedhealingclicker.sprites.ImageButtonSprite;
import de.jcm.ineedhealingclicker.sprites.LoadingTextBoxSprite;

/**
 * Created by JCM on 08.11.2018.
 */
public class LoadingScreen extends GameScreen
{
	private static final String TAG = "LoadingScreen";

	private Random random;
	private LoadingTextBoxSprite textBoxSprite;

	private ImageButtonSprite healthPack;

	private LoadingThread loadingThread;

	public LoadingScreen(GameSurfaceView gameSurfaceView)
	{
		super(gameSurfaceView);
		this.random = new Random();
	}

	@Override
	public void init()
	{
		Log.i(TAG, "init()");

		Runnable loadedRunnable = new Runnable()
		{
			@Override
			public void run()
			{
				resources.iNeedHealingSound.start();
			}
		};
		loadingThread = new LoadingThread(resources, loadedRunnable);
		loadingThread.start();

		Log.i(TAG, "Checkpoint #1");

		textBoxSprite = new LoadingTextBoxSprite(resources, random,
				getWidth(), getHeight());

		Log.i(TAG, "Checkpoint #2");

		int width = getWidth();
		int height = (int) textBoxSprite.posY;

		int drawableWidth = (int) (((double) width) / 1.5);
		int drawableHeight = (int)    //We need doubles or 0.1 be casted to int 0
				(((double) resources.largeHealthPack.getMinimumHeight() /
						(double) resources.largeHealthPack.getMinimumWidth())
						* (double) drawableWidth);
		int x = (width - drawableWidth) / 2;

		Log.i(TAG, "Checkpoint #3");

		int y = (height - drawableHeight) / 2;

		Runnable healthPackRunnable = new Runnable()
		{
			@Override
			public void run()
			{
				gameSurfaceView.gameExecutor.execute(gameSurfaceView.gameThread);

				gameSurfaceView.showScreen(new MainScreen(gameSurfaceView));
				gameSurfaceView.hideScreen(LoadingScreen.this);
			}
		};
		healthPack = new ImageButtonSprite(resources.largeHealthPack, drawableWidth, drawableHeight,
				healthPackRunnable);
		healthPack.posX = x;
		healthPack.posY = y;
		healthPack.setInvisible(true);

		Log.i(TAG, "Checkpoint #4");

		running = true;
		Log.i(TAG, "Checkpoint #5");
	}

	//Won't be called before GameThread was started
	@Override
	public void tick()
	{

	}

	@Override
	public void renderTick()
	{
		if(loadingThread.getProgress().get() >= LoadingThread.PROGRESS_END)
		{
			healthPack.setInvisible(false);
		}
	}

	@Override
	public void onTouchEvent(MotionEvent event)
	{
		textBoxSprite.onTouchEvent(event, gameSurfaceView);
		healthPack.onTouchEvent(event, gameSurfaceView);
	}

	@Override
	public void draw(Canvas canvas)
	{
		canvas.drawColor(resources.background);

		textBoxSprite.draw(canvas);

		int progress = loadingThread.getProgress().get();

		float percent = ((float) progress) / ((float) LoadingThread.PROGRESS_END) * 100.0f;
		float degrees = ((float) progress) / ((float) LoadingThread.PROGRESS_END) * 360.0f;

		Paint textPaint = new Paint();
		textPaint.setTextSize(50);

		canvas.drawText(progress + " -> " + percent + "% -> " + degrees + "°",
				25, getHeight() / 2, textPaint);
		healthPack.draw(canvas);
	}

	@Override
	public void destroy()
	{

	}
}
