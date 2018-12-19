package de.jcm.ineedhealingclicker.sprites;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import de.jcm.ineedhealingclicker.GameData;
import de.jcm.ineedhealingclicker.render.GameSurfaceView;

/**
 * Created by JCM on 03.11.2018.
 */
public abstract class GameSprite
{
	public double posX = 0;
	public double posY = 0;

	/**
	 * <b>Always</b> prefer {@link #getBoundingRectangle()} over this.
	 * The width value provided by this field might be wrong.
	 *
	 * @see #getBoundingRectangle()
	 */
	public double width;

	/**
	 * <b>Always</b> prefer {@link #getBoundingRectangle()} over this.
	 * The height value provided by this field might be wrong.
	 *
	 * @see #getBoundingRectangle()
	 */
	public double height;

	public float scale = 1.0f;

	private boolean dead = false;
	private boolean invisible = false;


	public abstract void onClick(MotionEvent event, GameSurfaceView view, GameData data);

	public abstract void draw(@NonNull Canvas canvas);

	public Rect getBoundingRectangle()
	{
		return new Rect((int) posX, (int) posY, (int) (posX + width), (int) (posY + height));
	}

	public boolean contains(int x, int y)
	{
		return getBoundingRectangle().contains(x, y);
	}

	/**
	 * Called 50 times a second
	 * Anything that manages timing, but not directly rendering should be placed here,
	 */
	public abstract void tick();

	/**
	 * Called as quick as possible
	 * Anything that invokes time consuming render method should be placed here.
	 */
	public abstract void renderTick();

	protected boolean shouldRenderDrawable()
	{
		return false;
	}

	protected abstract boolean shouldUseAdvancedContains();

	public boolean isDead()
	{
		return dead;
	}

	public void setDead(boolean dead)
	{
		this.dead = dead;
	}

	public void onTouchEvent(MotionEvent event, GameSurfaceView view)
	{
		int x = (int) event.getX();
		int y = (int) event.getY();

		if(!isInvisible())
		{
			if(contains(x, y))
			{
				onClick(event, view, view.data);
			}
		}
	}

	public boolean isInvisible()
	{
		return invisible;
	}

	public void setInvisible(boolean invisible)
	{
		this.invisible = invisible;
	}
}
