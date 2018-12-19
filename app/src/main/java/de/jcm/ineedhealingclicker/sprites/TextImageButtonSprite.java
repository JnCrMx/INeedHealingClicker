package de.jcm.ineedhealingclicker.sprites;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import de.jcm.ineedhealingclicker.GameData;
import de.jcm.ineedhealingclicker.render.GameSurfaceView;

/**
 * Created by JCM on 09.11.2018.
 */
public class TextImageButtonSprite extends GameSprite
{
	private final Drawable drawable;
	private final Runnable onClick;

	private final String text;
	private final Paint buttonPaint;
	private final Paint textPaint;
	private final int radius;
	private final int padding;
	private Rect textBox;
	private Rect box;

	public TextImageButtonSprite(String text, int x, int y, int radius, int padding,
								 Paint buttonPaint, Paint textPaint,
								 Resources resources, int drawableRes, Runnable onClick)
	{
		this(text, x, y, radius, padding, buttonPaint, textPaint,
				resources.getDrawable(drawableRes, null),
				onClick);
	}

	public TextImageButtonSprite(String text, int x, int y, int radius, int padding,
								 Paint buttonPaint, Paint textPaint,
								 Resources resources, int drawableRes, int width, int height, Runnable onClick)
	{
		this(text, x, y, radius, padding, buttonPaint, textPaint,
				resources.getDrawable(drawableRes, null), onClick);
	}

	public TextImageButtonSprite(String text, int x, int y, int radius, int padding,
								 Paint buttonPaint, Paint textPaint,
								 @NonNull Drawable drawable, Runnable onClick)
	{
		this.drawable = drawable;
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

		this.box = new Rect(textBox);
		this.box.right += this.box.height() - padding / 2;

		this.onClick = onClick;
	}

	public void translateTo(int x, int y)
	{
		this.posX = x;
		this.posY = y;

		this.textBox = new Rect();
		textPaint.getTextBounds(text, 0, text.length(), textBox);

		this.textBox.offsetTo(x, y);
		this.textBox.right += padding * 2;
		this.textBox.bottom += padding * 2;

		this.box = new Rect(textBox);
		this.box.right += this.box.height() - padding / 2;
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
		if(!isInvisible())
		{
			canvas.drawRoundRect(box.left, box.top, box.right, box.bottom,
					radius, radius, buttonPaint);
			canvas.drawText(text, textBox.left + padding, textBox.bottom - padding, textPaint);

			drawable.setBounds(textBox.right, textBox.top + padding / 2,
					box.right - padding / 2, box.bottom - padding / 2);

			drawable.draw(canvas);
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

	@Override
	public Rect getBoundingRectangle()
	{
		return this.box;
	}
}
