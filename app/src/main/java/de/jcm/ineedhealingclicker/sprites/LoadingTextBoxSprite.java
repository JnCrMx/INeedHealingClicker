package de.jcm.ineedhealingclicker.sprites;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MotionEvent;

import java.util.LinkedList;
import java.util.Random;

import de.jcm.ineedhealingclicker.GameData;
import de.jcm.ineedhealingclicker.GameResources;
import de.jcm.ineedhealingclicker.render.GameSurfaceView;

/**
 * Created by JCM on 09.11.2018.
 */
public class LoadingTextBoxSprite extends GameSprite
{
	private final GameResources resources;
	private final Random random;

	private final RectF textBox;
	private final Paint textPaint;

	private String text;
	private String[] lines;

	public LoadingTextBoxSprite(GameResources resources, Random random, int width, int height)
	{
		this.resources = resources;
		this.random = random;

		this.text = resources.loadingScreenMessages.get(
				random.nextInt(resources.loadingScreenMessages.size()));

		textBox = new RectF(
				50,
				height - 250,
				width - 50,
				height - 50);
		textPaint = new Paint();
		textPaint.setTextSize(42);
		textPaint.setTypeface(resources.overwatchFont);

		posX = textBox.left;
		posY = textBox.top;
		this.width = textBox.right - posX;
		this.height = textBox.bottom - posY;

		initText();
	}

	private static void drawTextLines(String[] lines, Canvas canvas, int x, int y, Paint paint)
	{
		int ty = y;
		for(String line : lines)
		{
			Rect textBounds = new Rect();
			paint.getTextBounds(line, 0, line.length(), textBounds);
			textBounds.offsetTo(x, ty);

			canvas.drawText(line, textBounds.left, textBounds.bottom, paint);
			ty -= ((textBounds.top - textBounds.bottom) - 5);
		}
	}

	private static String[] calculateTextForBounds(RectF bounds, String text, Paint paint)
	{
		Rect rect = new Rect(
				(int) bounds.left + 25,
				(int) bounds.top + 25,
				(int) bounds.right - 25,
				(int) bounds.bottom - 25);
		return calculateTextForBounds(rect, text, paint);
	}

	private static String[] calculateTextForBounds(Rect bounds, String text, Paint paint)
	{
		LinkedList<String> lines = new LinkedList<>();

		Rect textBounds = new Rect();

		String[] parts = text.split(" ");

		Log.d("calculateTextForBounds", "Calculating lines for string " + text);
		int current = 0;
		while(current < parts.length)
		{
			int count = parts.length + 1 - current;
			do
			{
				count--;
				StringBuilder assembly = new StringBuilder();
				for(int i = 0; i < count; i++)
					assembly.append(parts[current + i]).append(" ");
				assembly.deleteCharAt(assembly.length() - 1);

				paint.getTextBounds(assembly.toString(), 0, assembly.length(), textBounds);
				textBounds.offsetTo(bounds.left, bounds.top);
			}
			while((textBounds.right > bounds.right));

			StringBuilder assembly = new StringBuilder();
			for(int i = 0; i < count; i++)
				assembly.append(parts[current + i]).append(" ");
			assembly.deleteCharAt(assembly.length() - 1);

			lines.addLast(assembly.toString());

			Log.d("calculateTextForBounds", "Line " + lines.size() + ": " + assembly);

			current += count;
			bounds.top -= ((textBounds.top - textBounds.bottom) - 5);
		}
		return lines.toArray(new String[0]);
	}

	private void initText()
	{
		lines = calculateTextForBounds(textBox, text, textPaint);
	}

	@Override
	public void onClick(MotionEvent event, GameSurfaceView view, GameData data)
	{
		if(event.getAction() == MotionEvent.ACTION_DOWN)
		{
			setText(resources.loadingScreenMessages.get(
					random.nextInt(resources.loadingScreenMessages.size())));
		}
	}

	@Override
	public void draw(@NonNull Canvas canvas)
	{
		Paint textBoxPaint = new Paint();
		textBoxPaint.setStyle(Paint.Style.FILL);
		textBoxPaint.setColor(Color.LTGRAY);

		canvas.drawRoundRect(textBox, 50, 50, textBoxPaint);

		drawTextLines(lines, canvas, (int) textBox.left + 25, (int) textBox.top + 25, textPaint);
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

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
		initText();
	}
}
