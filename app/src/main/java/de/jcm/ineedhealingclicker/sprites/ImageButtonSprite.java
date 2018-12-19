package de.jcm.ineedhealingclicker.sprites;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import de.jcm.ineedhealingclicker.GameData;
import de.jcm.ineedhealingclicker.render.GameSurfaceView;

/**
 * Created by JCM on 09.11.2018.
 */
public class ImageButtonSprite extends DrawableGameSprite
{
	private final Runnable onClick;

	public ImageButtonSprite(@NonNull Bitmap bitmap, Runnable onClick)
	{
		super(bitmap);
		this.onClick = onClick;
	}

	public ImageButtonSprite(Resources resources, int drawableRes, Runnable onClick)
	{
		super(resources, drawableRes);
		this.onClick = onClick;
	}

	public ImageButtonSprite(Resources resources, int drawableRes, int width, int height, Runnable onClick)
	{
		super(resources, drawableRes, width, height);
		this.onClick = onClick;
	}

	public ImageButtonSprite(@NonNull Drawable drawable, Runnable onClick)
	{
		super(drawable);
		this.onClick = onClick;
	}

	public ImageButtonSprite(@NonNull Drawable drawable, int width, int height, Runnable onClick)
	{
		super(drawable, width, height);
		this.onClick = onClick;
	}

	@Override
	public void onClick(MotionEvent event, GameSurfaceView view, GameData data)
	{
		if(event.getAction() == MotionEvent.ACTION_DOWN)
			onClick.run();
	}

	@Override
	public void tick()
	{

	}

	@Override
	public void renderTick()
	{

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
}
