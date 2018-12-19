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
public class ButtonSprite extends GameSprite
{
	private final String text;
	private final Rect textBox;

	private final Paint buttonPaint;
	private final Paint textPaint;

	private final int radius;
	private final int padding;

	private final Runnable onClick;

	public ButtonSprite(String text, int x, int y, int radius, int padding,
						Paint buttonPaint, Paint textPaint, Runnable onClick)
	{
		this.text = text;

		this.posX = x;
		this.posY = y;

		this.radius = radius;
		this.padding = padding;

		this.buttonPaint = buttonPaint;
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
			onClick.run();
	}

	@Override
	public void draw(@NonNull Canvas canvas)
	{
		canvas.drawRoundRect(textBox.left, textBox.top, textBox.right, textBox.bottom,
				radius, radius, buttonPaint);
		canvas.drawText(text, textBox.left + padding, textBox.bottom - padding, textPaint);
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
}
