package de.jcm.ineedhealingclicker.shop;

import android.graphics.drawable.Drawable;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

import de.jcm.ineedhealingclicker.GameResources;

/**
 * Created by JCM on 18.11.2018.
 */
public interface ShopItem
{
	Drawable getIcon();

	String getDisplayName();

	String getDescription();

	String getDataName();

	BigDecimal getCost(int count);

	void init(GameResources resources, AtomicInteger progress);

	int getProgressCount();
}
