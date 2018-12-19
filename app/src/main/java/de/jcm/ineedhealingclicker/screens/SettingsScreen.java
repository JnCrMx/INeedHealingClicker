package de.jcm.ineedhealingclicker.screens;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

import java.math.BigDecimal;

import de.jcm.ineedhealingclicker.render.GameSurfaceView;
import de.jcm.ineedhealingclicker.sprites.ConfirmButtonSprite;
import de.jcm.ineedhealingclicker.sprites.ImageButtonSprite;

/**
 * Created by JCM on 09.11.2018.
 */
public class SettingsScreen extends GameScreen
{
	private RectF box;
	private ImageButtonSprite close;

	private ConfirmButtonSprite reset;

	public SettingsScreen(GameSurfaceView gameSurfaceView)
	{
		super(gameSurfaceView);
	}

	@Override
	public void init()
	{
		box = new RectF(50, 50, getWidth() - 50, getHeight() - 50);

		int closeSize = (int) (getWidth() / 7.5);
		Runnable closeRunnable = new Runnable()
		{
			@Override
			public void run()
			{
				gameSurfaceView.hideScreen(SettingsScreen.this);
			}
		};
		close = new ImageButtonSprite(resources.closeSymbol, closeSize, closeSize, closeRunnable);
		close.posX = box.right - 25 - close.width;
		close.posY = box.top + 25;

		int y = (int) (box.top + 25 + close.height + 25);

		Paint criticalButtonPaint = new Paint();
		criticalButtonPaint.setStyle(Paint.Style.FILL);
		criticalButtonPaint.setColor(resources.buttonColor);

		Paint confirmButtonPaint = new Paint();
		confirmButtonPaint.setStyle(Paint.Style.FILL);
		confirmButtonPaint.setColor(Color.RED);

		Paint textPaint = new Paint();
		textPaint.setTextSize(50);
		textPaint.setTypeface(resources.overwatchFont);

		Runnable resetRunnable = new Runnable()
		{
			@Override
			public void run()
			{
				synchronized(data)
				{
					data.iNeedHealings = BigDecimal.ZERO;

					data.critialHealthTime = 0;
					data.critialHealthMeter = 0;
					data.lastShopCategory = 0;

					data.critical = false;

					data.shopItemCountMap.clear();

					data.calculatePerClick();
				}
			}
		};
		reset = new ConfirmButtonSprite(resources.settingsReset, (int) box.left + 25, y,
				25, 25, criticalButtonPaint, confirmButtonPaint, textPaint,
				resetRunnable);

		running = true;
	}

	@Override
	public void tick()
	{
		reset.tick();
	}

	@Override
	public void renderTick()
	{

	}

	@Override
	public void onTouchEvent(MotionEvent event)
	{
		close.onTouchEvent(event, gameSurfaceView);
		reset.onTouchEvent(event, gameSurfaceView);
	}

	@Override
	public void draw(Canvas canvas)
	{
		Paint boxPaint = new Paint();
		boxPaint.setStyle(Paint.Style.FILL);
		boxPaint.setColor(Color.LTGRAY);
		boxPaint.setAlpha(0x99);

		canvas.drawRoundRect(box, 50, 50, boxPaint);

		close.draw(canvas);

		reset.draw(canvas);
	}

	@Override
	public void destroy()
	{

	}
}
