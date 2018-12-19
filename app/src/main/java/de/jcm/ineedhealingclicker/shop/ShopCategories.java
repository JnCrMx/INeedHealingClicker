package de.jcm.ineedhealingclicker.shop;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import java.util.concurrent.atomic.AtomicInteger;

import de.jcm.ineedhealingclicker.GameResources;
import de.jcm.ineedhealingclicker.R;

/**
 * Created by JCM on 18.11.2018.
 */
public enum ShopCategories
{
	AUTOCLICKERS(R.drawable.ic_healing, R.string.shop_category_autoclickers, AutoclickerShopItems.values()),
	UPGRADES(R.drawable.ic_upgrade, R.string.shop_category_upgrades, UpgradeShopItems.values()),
	TEST_B(R.drawable.ic_healing, R.string.shop_category_autoclickers, AutoclickerShopItems.values()),
	TEST_C(R.drawable.ic_healing, R.string.shop_category_autoclickers, AutoclickerShopItems.values());

	private @DrawableRes
	int iconId;
	private @StringRes
	int nameId;

	private Drawable icon;
	private String name;
	private ShopItem[] items;

	ShopCategories(@DrawableRes int icon, @StringRes int name, ShopItem[] items)
	{
		this.iconId = icon;
		this.nameId = name;
		this.items = items;
	}

	public static void initAll(GameResources resources, AtomicInteger progress)
	{
		ShopCategories[] categories = ShopCategories.values();
		for(ShopCategories category : categories)
		{
			category.init(resources, progress);
		}
	}

	public static int getAllProgressCount()
	{
		int progress = 0;
		ShopCategories[] categories = ShopCategories.values();
		for(ShopCategories category : categories)
		{
			progress += category.getProgressCount();
		}
		return progress;
	}

	public Drawable getIcon()
	{
		return icon;
	}

	public String getName()
	{
		return name;
	}

	public ShopItem[] getItems()
	{
		return items;
	}

	public void init(GameResources resources, AtomicInteger progress)
	{
		this.icon = resources.getDrawable(iconId);
		progress.incrementAndGet();

		this.name = resources.getString(nameId);
		progress.incrementAndGet();
		for(ShopItem item : items)
		{
			item.init(resources, progress);
		}
	}

	public int getProgressCount()
	{
		int progress = 2;
		for(ShopItem item : items)
		{
			progress += item.getProgressCount();
		}
		return progress;
	}
}
