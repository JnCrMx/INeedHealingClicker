package de.jcm.ineedhealingclicker.shop;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

import de.jcm.ineedhealingclicker.GameResources;
import de.jcm.ineedhealingclicker.R;

/**
 * Created by JCM on 18.11.2018.
 */
public enum UpgradeShopItems implements ShopItem
{
	SMALL_HEALTH_PACk(R.drawable.ic_genji_cat,
			R.string.sitem_upgrade_small_health_pack, R.string.sitem_upgrade_small_health_pack_desc,
			1000, 10.0, 2.0),
	LARGE_HEALTH_PACk(R.drawable.ic_health_pack_large,
			R.string.sitem_upgrade_large_health_pack, R.string.sitem_upgrade_large_health_pack_desc,
			5000, 10.0, 4.0);

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
	private double multiplier;

	UpgradeShopItems(@DrawableRes int iconId, @StringRes int nameId, @StringRes int descriptionId, int startCost, double costScale, double multiplier)
	{
		this.iconId = iconId;
		this.nameId = nameId;
		this.descriptionId = descriptionId;
		this.startCost = startCost;
		this.costScale = costScale;
		this.multiplier = multiplier;
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

	public double getMultiplier()
	{
		return multiplier;
	}
}
