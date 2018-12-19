package de.jcm.ineedhealingclicker.screens;

import android.graphics.Canvas;
import android.view.MotionEvent;

import de.jcm.ineedhealingclicker.GameData;
import de.jcm.ineedhealingclicker.GameResources;
import de.jcm.ineedhealingclicker.render.GameSurfaceView;

/**
 * Created by JCM on 06.11.2018.
 */
public abstract class GameScreen
{
	protected GameSurfaceView gameSurfaceView;
	protected GameResources resources;
	protected GameData data;
	protected boolean running;
	private boolean inForeground;

	public GameScreen(GameSurfaceView gameSurfaceView)
	{
		this.gameSurfaceView = gameSurfaceView;
		this.resources = gameSurfaceView.resources;
		this.data = gameSurfaceView.data;
	}

	public boolean isRunning()
	{
		return running;
	}

	public int getWidth()
	{
		return gameSurfaceView.getWidth();
	}

	public int getHeight()
	{
		return gameSurfaceView.getHeight();
	}

	/**
	 * Must set running at the end.
	 */
	public abstract void init();

	public abstract void tick();

	public abstract void renderTick();

	public abstract void onTouchEvent(MotionEvent event);

	public abstract void draw(Canvas canvas);

	public abstract void destroy();

	public boolean isInForeground()
	{
		return inForeground;
	}

	public void setInForeground(boolean inForeground)
	{
		this.inForeground = inForeground;
	}
}
