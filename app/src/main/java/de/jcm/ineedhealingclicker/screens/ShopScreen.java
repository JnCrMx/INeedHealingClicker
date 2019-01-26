package de.jcm.ineedhealingclicker.screens;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;

import java.math.BigDecimal;

import de.jcm.ineedhealingclicker.GameData;
import de.jcm.ineedhealingclicker.NumberFormatHelper;
import de.jcm.ineedhealingclicker.render.GameSurfaceView;
import de.jcm.ineedhealingclicker.shop.ShopCategories;
import de.jcm.ineedhealingclicker.shop.ShopItem;
import de.jcm.ineedhealingclicker.sprites.ImageButtonSprite;
import de.jcm.ineedhealingclicker.sprites.SelectableImageButtonSprite;
import de.jcm.ineedhealingclicker.sprites.SelectionGroup;
import de.jcm.ineedhealingclicker.sprites.TextImageButtonSprite;

/**
 * Created by JCM on 19.11.2018.
 */
public class ShopScreen extends GameScreen
{
	private RectF box;
	private ImageButtonSprite close;

	private SelectionGroup categoryGroup;
	private SelectableImageButtonSprite[] categoryButtonSprites;

	private ShopItem[] shopItems;
	private TextImageButtonSprite[] shopItemCosts;

	private int contentX, contentY;
	private int contentWidth, contentHeight;

	public ShopScreen(GameSurfaceView gameSurfaceView)
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
				gameSurfaceView.hideScreen(ShopScreen.this);
			}
		};
		close = new ImageButtonSprite(resources.closeSymbol, closeSize, closeSize, closeRunnable);
		close.posX = box.right - 25 - close.width;
		close.posY = box.top + 25;

		int x = (int) (box.left + 25);
		int y = (int) (box.top + 25 + close.height + 25);

		int width = (int) (box.right - box.left) - 50;
		int height = (int) (box.bottom - box.top) - 50;
		int catSize = width / (ShopCategories.values().length);

		categoryButtonSprites = new SelectableImageButtonSprite[ShopCategories.values().length];
		categoryGroup = new SelectionGroup();
		for(int i = 0; i < ShopCategories.values().length; i++)
		{
			ShopCategories category = ShopCategories.values()[i];

			final int num = i;
			Runnable categoryRunnable = new Runnable()
			{
				@Override
				public void run()
				{
					data.lastShopCategory = num;
					shopItems = ShopCategories.values()[categoryGroup.getSelectedIndex()].getItems();
					updateShopItems();
				}
			};

			categoryButtonSprites[i] = new SelectableImageButtonSprite(category.getIcon(),
					catSize - 25, catSize - 25, resources.buttonColor, categoryRunnable);
			categoryButtonSprites[i].posY = y;
			categoryButtonSprites[i].posX = x + 12.5 + i * catSize;

			categoryGroup.addSelectable(categoryButtonSprites[i]);
		}
		categoryGroup.setSelectedItem(categoryButtonSprites[data.lastShopCategory]);

		contentX = x;
		contentY = y + catSize + 25;
		contentWidth = width;
		contentHeight = height - catSize - 25;

		updateShopItems();

		running = true;
	}

	private void updateShopItems()
	{
		shopItems = ShopCategories.values()[categoryGroup.getSelectedIndex()].getItems();
		shopItemCosts = new TextImageButtonSprite[shopItems.length];
		int ty = contentY;

		Paint buttonBackground = new Paint();
		buttonBackground.setStyle(Paint.Style.FILL);
		buttonBackground.setColor(Color.LTGRAY);

		Paint textPaint = new Paint();
		textPaint.setTextSize(40);
		textPaint.setColor(Color.WHITE);
		textPaint.setTypeface(resources.overwatchFont);

		for(int i = 0; i < shopItems.length; i++)
		{
			final ShopItem item = shopItems[i];
			final int itemCount = data.getShopItemCount(item);
			final BigDecimal itemCost = item.getCost(itemCount);

			Runnable buy = new Runnable()
			{
				@Override
				public void run()
				{
					GameData data = gameSurfaceView.data;
					if(data.iNeedHealings.compareTo(itemCost) >= 0)
					{
						data.shopItemCountMap.remove(item);
						data.shopItemCountMap.put(item, itemCount + 1);
						data.iNeedHealings = data.iNeedHealings.subtract(itemCost);

						data.calculatePerClick();

						updateShopItems();
					}
				}
			};
			String cost = NumberFormatHelper.bigDecimalToString(itemCost);
			shopItemCosts[i] = new TextImageButtonSprite(cost,
					contentX, ty, 25, 25,
					buttonBackground, textPaint,
					resources.healingSymbol, buy);
			shopItemCosts[i].translateTo(contentX + contentWidth
					- shopItemCosts[i].getBoundingRectangle().width(), (int) shopItemCosts[i].posY);
			ty += shopItemCosts[i].getBoundingRectangle().height() + 25;
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
	public void onTouchEvent(MotionEvent event)
	{
		Log.d("ShopScreen", event.toString());

		close.onTouchEvent(event, gameSurfaceView);

		for(SelectableImageButtonSprite categoryButton : categoryButtonSprites)
		{
			categoryButton.onTouchEvent(event, gameSurfaceView);
		}

		for(TextImageButtonSprite shopItemCost : shopItemCosts)
		{
			shopItemCost.onTouchEvent(event, gameSurfaceView);
		}
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

		for(SelectableImageButtonSprite categoryButton : categoryButtonSprites)
		{
			categoryButton.draw(canvas);
		}

		for(int i = 0; i < shopItems.length; i++)
		{
			ShopItem item = shopItems[i];
			TextImageButtonSprite itemCost = shopItemCosts[i];

			itemCost.draw(canvas);

			Drawable drawable = item.getIcon();
			int iconSize = itemCost.getBoundingRectangle().height();

			double width;
			double height;

			if(drawable.getIntrinsicWidth() > drawable.getIntrinsicHeight())
			{
				width = iconSize;
				height = iconSize *
						((double) drawable.getIntrinsicHeight()) / ((double) drawable.getIntrinsicWidth());
			}
			else
			{
				height = iconSize;
				width = iconSize *
						((double) drawable.getIntrinsicWidth()) / ((double) drawable.getIntrinsicHeight());
			}

			drawable.setBounds(contentX, (int) itemCost.posY,
					contentX + (int) width,
					(int) itemCost.posY + (int) height);
			drawable.draw(canvas);

			Paint namePaint = new Paint();
			namePaint.setTypeface(resources.overwatchFont);
			namePaint.setTextSize(32.5f);

			String name = item.getDisplayName();

			Rect nameBounds = new Rect();
			namePaint.getTextBounds(name, 0, name.length(), nameBounds);

			canvas.drawText(name,
					contentX + iconSize + 25, (float) itemCost.posY + nameBounds.height() + 12.5f,
					namePaint);

			Paint descPaint = new Paint();
			descPaint.setTextSize(15f);

			String description = item.getDescription();

			Rect descBounds = new Rect();
			descPaint.getTextBounds(description, 0, description.length(), descBounds);

			canvas.drawText(description, contentX + iconSize + 25,
					(float) itemCost.posY + nameBounds.height() + 12.5f + descBounds.height() + 12.5f,
					descPaint);
		}
	}

	@Override
	public void destroy()
	{

	}
}
