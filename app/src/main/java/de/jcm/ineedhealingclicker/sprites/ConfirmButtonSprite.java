package de.jcm.ineedhealingclicker.sprites;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import de.jcm.ineedhealingclicker.GameData;
import de.jcm.ineedhealingclicker.render.GameSurfaceView;

/**
 * Created by JCM on 09.11.2018.
 */
public class ConfirmButtonSprite extends GameSprite
{
	private final String text;
	private final Rect textBox;

	private final Paint buttonPaint;
	private final Paint confirmPaint;
	private final Paint textPaint;

	private final int radius;
	private final int padding;

	private final Runnable onClick;

	private boolean confirming = false;
	private int confirmProgress = 0;

	public ConfirmButtonSprite(String text, int x, int y, int radius, int padding,
							   Paint buttonPaint, Paint confirmPaint, Paint textPaint,
							   Runnable onClick)
	{
		this.text = text;

		this.posX = x;
		this.posY = y;

		this.radius = radius;
		this.padding = padding;

		this.buttonPaint = buttonPaint;
		this.confirmPaint = confirmPaint;
		this.textPaint = textPaint;

		this.textBox = new Rect();
		textPaint.getTextBounds(text, 0, text.length(), textBox);

		this.textBox.offsetTo(x, y);
		this.textBox.right += padding * 2;
		this.textBox.bottom += padding * 2;

		this.onClick = onClick;
	}

	@Override
	public Rect getBoundingRectangle()
	{
		return textBox;
	}

	@Override
	public void onClick(MotionEvent event, GameSurfaceView view, GameData data)
	{
		if(event.getAction() == MotionEvent.ACTION_DOWN)
		{
			confirming = true;
		}
		if(event.getAction() == MotionEvent.ACTION_UP)
		{
			confirming = false;
			confirmProgress = 0;
		}
	}

	@Override
	public void onTouchEvent(MotionEvent event, GameSurfaceView view)
	{
		if(event.getAction() == MotionEvent.ACTION_UP)
		{
			confirming = false;
			confirmProgress = 0;
		}
		if(!contains((int) event.getX(), (int) event.getY()))
		{
			confirming = false;
			confirmProgress = 0;
		}
		super.onTouchEvent(event, view);
	}

	@Override
	public void draw(@NonNull Canvas canvas)
	{
		canvas.drawRoundRect(textBox.left, textBox.top, textBox.right, textBox.bottom,
				radius, radius, buttonPaint);

		int width = textBox.right - textBox.left;
		int newWidth = (int) (width * (confirmProgress / 100.0D));

		if(newWidth > width)
			newWidth = width;

		canvas.drawRoundRect(textBox.left, textBox.top, textBox.left + newWidth, textBox.bottom,
				radius, radius, confirmPaint);

		canvas.drawText(text, textBox.left + padding, textBox.bottom - padding, textPaint);
	}

	@Override
	public void tick()
	{
		if(confirming)
		{
			confirmProgress++;
			if(confirmProgress == 100)
			{
				onClick.run();
			}
		}
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
}
