package de.jcm.ineedhealingclicker.sprites;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

import de.jcm.ineedhealingclicker.GameData;
import de.jcm.ineedhealingclicker.GameThread;
import de.jcm.ineedhealingclicker.render.GameSurfaceView;

/**
 * Created by JCM on 04.11.2018.
 */
public class SpeechBubbleSprite extends DrawableGameSprite
{
	private static final int TICKS_TO_LIVE = 1000 / GameThread.TIME_PER_TICK;

	private int ticksToLive;
	private long animationStart;

	public SpeechBubbleSprite(Drawable drawable)
	{
		super(drawable);
		ticksToLive = TICKS_TO_LIVE;
	}

	@Override
	public void onClick(MotionEvent event, GameSurfaceView view, GameData data)
	{

	}

	@Override
	public void tick()
	{
		if(ticksToLive > 0)
		{
			ticksToLive--;

			if(ticksToLive == 0)
				setDead(true);
		}
	}

	@Override
	public void renderTick()
	{
		if(animationStart == 0)
			animationStart = System.currentTimeMillis();

		long time = System.currentTimeMillis() - animationStart;

		//Way faster than Math.sin(double) or float[] sinTable
		if(time < ((TICKS_TO_LIVE * GameThread.TIME_PER_TICK) / 2))
			scale = 0.75f + (time) / (float) (TICKS_TO_LIVE * GameThread.TIME_PER_TICK * 2);
		else
			scale = 1.0f - (time - 500) / (float) (TICKS_TO_LIVE * GameThread.TIME_PER_TICK * 2);
	}

	@Override
	protected boolean shouldUseAdvancedContains()
	{
		return false;
	}

	@Override
	protected boolean shouldRenderDrawable()
	{
		return true;
	}

	@Override
	public Rect getBoundingRectangle()
	{
		return new Rect(0, 0, 0, 0);
	}
}
