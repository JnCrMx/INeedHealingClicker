package de.jcm.ineedhealingclicker.render;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.jcm.ineedhealingclicker.GameData;
import de.jcm.ineedhealingclicker.GameResources;
import de.jcm.ineedhealingclicker.GameThread;
import de.jcm.ineedhealingclicker.MainActivity;
import de.jcm.ineedhealingclicker.screens.GameScreen;
import de.jcm.ineedhealingclicker.screens.LoadingScreen;

/**
 * Created by JCM on 03.11.2018.
 */
public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback
{
	private static final String TAG = "GameSurfaceView";

	public MainActivity activity;

	public ExecutorService gameExecutor;
	public ExecutorService renderExecutor;

	public GameThread gameThread;
	public GameRenderThread renderThread;

	public GameData data;
	public GameResources resources;
	public LinkedList<GameScreen> screens;

	public GameSurfaceView(MainActivity context)
	{
		super(context);
		Log.i(TAG, "GameSurfaceView(Context context)");

		this.gameExecutor = Executors.newSingleThreadExecutor();
		this.renderExecutor = Executors.newSingleThreadExecutor();

		this.activity = context;

		this.setFocusable(true);
		this.getHolder().addCallback(this);

		resources = new GameResources(activity);
		resources.preInit();
		//Do not load all resources yet, leave this to LoadingScreen
		//resources.init();

		Log.i(TAG, "Checkpoint #1");

		data = new GameData();
		data.load(activity);

		Log.i(TAG, "Checkpoint #2");

		screens = new LinkedList<>();

		Log.i(TAG, "Checkpoint #3");

		gameThread = new GameThread(this);
		gameThread.setRunning(true);
		//gameThread.start();

		Log.i(TAG, "Checkpoint #4");

		renderThread = new GameRenderThread(this);

		Log.i(TAG, "Checkpoint #5");

		LoadingScreen loadingScreen = new LoadingScreen(this);
		screens.addLast(loadingScreen);
		loadingScreen.setInForeground(true);
	}

	@Override
	public void draw(Canvas canvas)
	{
		super.draw(canvas);

		for(int i = 0; i < screens.size(); i++)
			if(screens.get(i).isRunning())
				screens.get(i).draw(canvas);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		try
		{
			Log.i(TAG, "surfaceCreated(SurfaceHolder holder)");

			if(!renderThread.isRunning())
			{
				renderThread.setRunning(true);
				renderExecutor.execute(renderThread);
			}

			if(!(screens.getLast() instanceof LoadingScreen))
			{
				if(!gameThread.isRunning())
				{
					gameThread.setRunning(true);
					gameExecutor.execute(gameThread);
				}
			}

			for(GameScreen screen : screens)
			{
				if(!screen.isRunning())
				{
					screen.init();
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{
		Log.i(TAG, "surfaceChanged(SurfaceHolder holder, int format, int width, int height)");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		Log.i(TAG, "surfaceDestroyed(SurfaceHolder holder)");

		data.save(activity);

		gameThread.setRunning(false);
		renderThread.setRunning(false);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if(event.getAction() == MotionEvent.ACTION_DOWN ||
				event.getAction() == MotionEvent.ACTION_UP ||
				event.getAction() == MotionEvent.ACTION_MOVE)
		{
			if(!screens.isEmpty())
				if(screens.getLast().isRunning())
					screens.getLast().onTouchEvent(event);

			return true;
		}
		return false;
	}

	public void renderTick()
	{
		for(int i = 0; i < screens.size(); i++)
			if(screens.get(i).isRunning())
				screens.get(i).renderTick();
	}

	public void tick()
	{
		data.tick();

		for(int i = 0; i < screens.size(); i++)
			if(screens.get(i).isRunning())
				screens.get(i).tick();
	}

	public void showScreen(GameScreen screen)
	{
		if(!screens.isEmpty())
			if(screens.getLast().isRunning())
				screens.getLast().setInForeground(false);

		screens.addLast(screen);
		screen.init();
		screen.setInForeground(true);
	}

	public void hideScreen()
	{
		GameScreen screen = screens.removeLast();
		screen.destroy();

		if(!screens.isEmpty())
			if(screens.getLast().isRunning())
				screens.getLast().setInForeground(true);
	}

	public void hideScreen(GameScreen screen)
	{
		screens.remove(screen);
		screen.destroy();

		if(!screens.isEmpty())
			if(screens.getLast().isRunning())
				screens.getLast().setInForeground(true);
	}
}
