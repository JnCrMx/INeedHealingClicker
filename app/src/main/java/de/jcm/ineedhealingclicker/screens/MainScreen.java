package de.jcm.ineedhealingclicker.screens;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.ArrayList;

import de.jcm.ineedhealingclicker.GameThread;
import de.jcm.ineedhealingclicker.NumberFormatHelper;
import de.jcm.ineedhealingclicker.render.GameSurfaceView;
import de.jcm.ineedhealingclicker.sprites.GenjiCatSprite;
import de.jcm.ineedhealingclicker.sprites.ImageButtonSprite;
import de.jcm.ineedhealingclicker.sprites.SpeechBubbleSprite;

/**
 * Created by JCM on 06.11.2018.
 */
public class MainScreen extends GameScreen
{

	private final ArrayList<SpeechBubbleSprite> speechBubbleSprites = new ArrayList<>();
	private GenjiCatSprite genjiCat;
	private ImageButtonSprite settings;
	private ImageButtonSprite shop;

	public MainScreen(GameSurfaceView gameSurfaceView)
	{
		super(gameSurfaceView);
	}

	@Override
	public void init()
	{
		int width = getWidth();
		int height = getHeight();

		int drawableWidth = (int) (((double) width) / 1.5);
		int drawableHeight = (int)    //We need doubles or 0.1 be casted to int 0
				(((double) resources.vectorGenji.getMinimumHeight() /
						(double) resources.vectorGenji.getMinimumWidth())
						* (double) drawableWidth);
		genjiCat = new GenjiCatSprite(resources.vectorGenji, drawableWidth, drawableHeight);

		int genjiX = (width - drawableWidth) / 2;
		int genjiY = (height - drawableHeight) / 2;
		genjiCat.posX = genjiX;
		genjiCat.posY = genjiY;

		Paint buttonPaint = new Paint();
		buttonPaint.setStyle(Paint.Style.FILL);
		buttonPaint.setColor(resources.buttonColor);

		Paint textPaint = new Paint();
		textPaint.setTextSize(50);

		int menuIconSize = (int) (getWidth() / 6.25);
		Runnable settingsRunnable = new Runnable()
		{
			@Override
			public void run()
			{
				gameSurfaceView.showScreen(new SettingsScreen(gameSurfaceView));
			}
		};
		settings = new ImageButtonSprite(resources.settingsSymbol, menuIconSize, menuIconSize, settingsRunnable);
		settings.posX = getWidth() - 25 - settings.width;
		settings.posY = getHeight() - 25 - settings.height;

		Runnable shopRunnable = new Runnable()
		{
			@Override
			public void run()
			{
				gameSurfaceView.showScreen(new ShopScreen(gameSurfaceView));
			}
		};
		shop = new ImageButtonSprite(resources.shopSymbol, menuIconSize, menuIconSize, shopRunnable);
		shop.posX = getWidth() - 25 - settings.width - 50 - shop.width;
		shop.posY = getHeight() - 25 - shop.height;

		running = true;
	}

	@Override
	public void tick()
	{
		genjiCat.tick();

		synchronized(speechBubbleSprites)
		{
			ArrayList<SpeechBubbleSprite> toRemove = new ArrayList<>();
			for(SpeechBubbleSprite sprite : speechBubbleSprites)
			{
				sprite.tick();
				if(sprite.isDead())
					toRemove.add(sprite);
			}

			for(SpeechBubbleSprite sprite : toRemove)
			{
				speechBubbleSprites.remove(sprite);
			}
		}
	}

	@Override
	public void renderTick()
	{
		genjiCat.renderTick();

		synchronized(speechBubbleSprites)
		{
			for(SpeechBubbleSprite sprite : speechBubbleSprites)
			{
				sprite.renderTick();
			}
		}
	}

	@Override
	public void onTouchEvent(MotionEvent event)
	{
		genjiCat.onTouchEvent(event, gameSurfaceView);
		settings.onTouchEvent(event, gameSurfaceView);
		shop.onTouchEvent(event, gameSurfaceView);
	}

	@Override
	public void draw(Canvas canvas)
	{
		//Background
		if(data.critical)
		{
			resources.criticalBackground.setBounds(0, 0, getWidth(), getHeight());
			resources.criticalBackground.draw(canvas);
		}
		else
		{
			canvas.drawColor(resources.background);
		}

		//Background speechbubbles
		synchronized(speechBubbleSprites)
		{
			for(SpeechBubbleSprite sprite : speechBubbleSprites)
			{
				sprite.draw(canvas);
			}
		}

		//GenjiCat
		genjiCat.draw(canvas);

		//Score
		Paint textPaint = new Paint();
		textPaint.setTextSize(100);
		textPaint.setColor(Color.WHITE);
		textPaint.setTypeface(resources.overwatchFont);

		String iNeedHealings = NumberFormatHelper.bigDecimalToString(data.iNeedHealings);
		Rect bounds = new Rect();
		textPaint.getTextBounds(iNeedHealings, 0, iNeedHealings.length(), bounds);

		int textHeight = -bounds.top;
		int textWidth = bounds.right;

		int healingWidth = textWidth + 25 + textHeight;

		int drawX = (getWidth() - healingWidth) / 2;
		int drawY = 25;
		int textX = drawX + textHeight + 25;
		int textY = textHeight + 25;

		resources.healingSymbol.setBounds(drawX, drawY, drawX + textHeight, drawY + textHeight);
		resources.healingSymbol.draw(canvas);
		canvas.drawText(iNeedHealings, textX, textY, textPaint);

		String perSecond = NumberFormatHelper.bigDecimalToString(data.iNeedHealingsPerSecond) + " / s";
		textPaint.setTextSize(25);
		textPaint.getTextBounds(perSecond, 0, perSecond.length(), bounds);
		textHeight = -bounds.top;
		textWidth = bounds.right;
		textX = (getWidth() - textWidth) / 2;
		textY = textY + 25 + textHeight;
		canvas.drawText(perSecond, textX, textY, textPaint);

		//Critical health meter
		if(data.critialHealthTime == 0)
		{
			int criticalWidth = (int) ((double) data.critialHealthMeter / (1000 / GameThread.TIME_PER_TICK * 60.0) * (double) getWidth());
			int criticalHeight = 10;
			int criticalY = textY + 25;

			Paint criticalPaint = new Paint();
			criticalPaint.setColor(Color.RED);
			criticalPaint.setStyle(Paint.Style.FILL);
			canvas.drawRect(0, criticalY, criticalWidth, criticalY + criticalHeight, criticalPaint);
		}
		else
		{
			int criticalWidth = (int) ((double) data.critialHealthTime / (1000 / GameThread.TIME_PER_TICK * 10.0) * (double) getWidth());
			int criticalHeight = 10;
			int criticalY = textY + 25;

			Paint criticalPaint = new Paint();
			criticalPaint.setColor(Color.RED);
			criticalPaint.setStyle(Paint.Style.FILL);
			canvas.drawRect(0, criticalY, criticalWidth, criticalY + criticalHeight, criticalPaint);
		}

		settings.draw(canvas);
		shop.draw(canvas);
	}

	public void spawnBubble(SpeechBubbleSprite sprite)
	{
		synchronized(speechBubbleSprites)
		{
			speechBubbleSprites.add(sprite);
		}
	}

	@Override
	public void destroy()
	{

	}
}
