package de.jcm.ineedhealingclicker.shop;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

import de.jcm.ineedhealingclicker.GameResources;
import de.jcm.ineedhealingclicker.GameThread;
import de.jcm.ineedhealingclicker.R;

/**
 * Created by JCM on 18.11.2018.
 */
public enum AutoclickerShopItems implements ShopItem
{
	TRAINING_BOT(R.drawable.ic_genji_cat,
			R.string.sitem_autoclicker_training_bot, R.string.sitem_autoclicker_training_bot_desc,
			100, 2.0, 1.0),
	VENOM_MINE(R.drawable.ic_close,
			R.string.sitem_autoclicker_venom_mine, R.string.sitem_autoclicker_venom_mine_desc,
			1000, 2.0, 10.0);

	private final @DrawableRes
	int iconId;
	private final @StringRes
	int nameId;
	private final @StringRes
	int descriptionId;
	private Drawable icon;
	private String name;
	private String description;
	private int startCost;
	private double costScale;
	private double perTick;

	AutoclickerShopItems(@DrawableRes int iconId, @StringRes int nameId, @StringRes int descriptionId, int startCost, double costScale, double perSecond)
	{
		this.iconId = iconId;
		this.nameId = nameId;
		this.descriptionId = descriptionId;
		this.startCost = startCost;
		this.costScale = costScale;
		this.perTick = (perSecond / (1000.0 / (double) GameThread.TIME_PER_TICK)) + 0.0025;
	}

	public void init(GameResources resources, AtomicInteger progress)
	{
		this.icon = resources.getDrawable(iconId);
		progress.incrementAndGet();
		this.name = resources.getString(nameId);
		progress.incrementAndGet();
		this.description = resources.getString(descriptionId);
		progress.incrementAndGet();
	}

	@Override
	public int getProgressCount()
	{
		return 3;
	}

	@Override
	public Drawable getIcon()
	{
		return icon;
	}

	@Override
	public String getDataName()
	{
		return getDeclaringClass().getSimpleName() + "_" + name();
	}

	@Override
	public String getDisplayName()
	{
		return name;
	}

	@Override
	public String getDescription()
	{
		return description;
	}

	@Override
	public BigDecimal getCost(int count)
	{
		//return (int) (startCost*Math.pow(costScale, count));
		return BigDecimal.valueOf(startCost).multiply(
				BigDecimal.valueOf(costScale).pow(count)
		);
	}

	public double getPerTick()
	{
		return perTick;
	}
}
