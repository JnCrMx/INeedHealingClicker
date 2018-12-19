package de.jcm.ineedhealingclicker.sprites;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import de.jcm.ineedhealingclicker.GameData;
import de.jcm.ineedhealingclicker.render.GameSurfaceView;

/**
 * Created by JCM on 09.11.2018.
 */
public class SelectableImageButtonSprite extends DrawableGameSprite implements Selectable
{
	private final @ColorInt
	int selectionTint;
	private final Runnable onClick;
	private SelectionGroup group;
	private boolean selected;

	public SelectableImageButtonSprite(@NonNull Bitmap bitmap, @ColorInt int selectionTint, Runnable onClick)
	{
		super(bitmap);
		this.selectionTint = selectionTint;
		this.onClick = onClick;
	}

	public SelectableImageButtonSprite(Resources resources, int drawableRes, @ColorInt int selectionTint, Runnable onClick)
	{
		super(resources, drawableRes);
		this.selectionTint = selectionTint;
		this.onClick = onClick;
	}

	public SelectableImageButtonSprite(Resources resources, int drawableRes, int width, int height, @ColorInt int selectionTint, Runnable onClick)
	{
		super(resources, drawableRes, width, height);
		this.selectionTint = selectionTint;
		this.onClick = onClick;
	}

	public SelectableImageButtonSprite(@NonNull Drawable drawable, @ColorInt int selectionTint, Runnable onClick)
	{
		super(drawable);
		this.selectionTint = selectionTint;
		this.onClick = onClick;
	}

	public SelectableImageButtonSprite(@NonNull Drawable drawable, int width, int height, @ColorInt int selectionTint, Runnable onClick)
	{
		super(drawable, width, height);
		this.selectionTint = selectionTint;
		this.onClick = onClick;
	}

	public SelectionGroup getGroup()
	{
		return group;
	}

	public void setGroup(SelectionGroup group)
	{
		this.group = group;
	}

	@Override
	public void onClick(MotionEvent event, GameSurfaceView view, GameData data)
	{
		if(event.getAction() == MotionEvent.ACTION_DOWN)
		{
			selected = true;

			if(group != null)
				group.onSelected(this);

			onClick.run();
		}
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

	public boolean isSelected()
	{
		return selected;
	}

	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}

	@Override
	public void draw(@NonNull Canvas canvas)
	{
		if(!isInvisible())
		{
			drawable.setBounds((int) posX, (int) posY, (int) (posX + width * scale), (int) (posY + height * scale));

			if(selected)
				drawable.setTint(selectionTint);
			drawable.draw(canvas);
			if(selected)
				drawable.setTint(0xFFFFFFFF);
		}
	}
}
