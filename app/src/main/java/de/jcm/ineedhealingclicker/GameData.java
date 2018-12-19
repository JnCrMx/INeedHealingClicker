package de.jcm.ineedhealingclicker;

import android.content.SharedPreferences;
import android.util.ArraySet;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.jcm.ineedhealingclicker.shop.AutoclickerShopItems;
import de.jcm.ineedhealingclicker.shop.ShopCategories;
import de.jcm.ineedhealingclicker.shop.ShopItem;
import de.jcm.ineedhealingclicker.shop.UpgradeShopItems;

/**
 * Created by JCM on 03.11.2018.
 */
public class GameData
{
	public BigDecimal iNeedHealings;

	public boolean critical;

	public int critialHealthMeter;
	public int critialHealthTime;
	public BigDecimal iNeedHealingsPerSecond = BigDecimal.ZERO;
	public int lastShopCategory;
	public Map<ShopItem, Integer> shopItemCountMap = new HashMap<>();
	public BigDecimal iNeedHealingsPerClick;
	private long lastTime;
	private BigDecimal lastINeedHealings;

	public void save(MainActivity activity)
	{
		SharedPreferences.Editor editor = activity.gameData.edit();

		editor.putString(activity.getString(R.string.game_data_i_need_healings),
				iNeedHealings.toPlainString());

		editor.putInt(activity.getString(R.string.game_data_critical_health_meter),
				critialHealthMeter);
		editor.putInt(activity.getString(R.string.game_data_critical_health_time),
				critialHealthTime);

		ArraySet<String> shopEntries = new ArraySet<>();
		for(Map.Entry<ShopItem, Integer> entry : shopItemCountMap.entrySet())
		{
			if(entry.getValue() > 0)
			{
				editor.putInt(activity.getString(R.string.game_data_shop_prefix) +
						entry.getKey().getDataName(), entry.getValue());
				shopEntries.add(entry.getKey().getDataName());
			}
		}
		editor.putStringSet(activity.getString(R.string.game_data_shop_entry_set), shopEntries);

		editor.putInt(activity.getString(R.string.game_data_shop_last_category), lastShopCategory);

		editor.apply();
	}

	public void load(MainActivity activity)
	{
		iNeedHealings = new BigDecimal(activity.gameData.getString(
				activity.getString(R.string.game_data_i_need_healings), "0"));
		critialHealthMeter = activity.gameData.getInt(
				activity.getString(R.string.game_data_critical_health_meter), 0);
		critialHealthTime = activity.gameData.getInt(
				activity.getString(R.string.game_data_critical_health_time), 0);

		shopItemCountMap.clear();
		Set<String> shopEntries = activity.gameData.getStringSet(
				activity.getString(R.string.game_data_shop_entry_set), new ArraySet<String>());
		for(String entry : shopEntries)
		{
			String key = activity.getString(R.string.game_data_shop_prefix) + entry;
			int value = activity.gameData.getInt(key, 0);

			for(ShopCategories category : ShopCategories.values())
			{
				for(ShopItem item : category.getItems())
				{
					if(item.getDataName().equals(entry))
					{
						shopItemCountMap.put(item, value);
					}
				}
			}
		}

		lastShopCategory = activity.gameData.getInt(
				activity.getString(R.string.game_data_shop_last_category), 0);

		calculatePerClick();
	}

	public void tick()
	{
		if(critialHealthTime == 0)
		{
			critialHealthMeter++;
			if(critialHealthMeter >= (1000 / GameThread.TIME_PER_TICK) * 60)
			{
				critialHealthMeter = 0;
				critialHealthTime = (1000 / GameThread.TIME_PER_TICK) * 10;
				critical = true;
			}
		}
		else
		{
			critialHealthTime--;
			if(critialHealthTime == 0)
			{
				critical = false;
			}
		}

		for(AutoclickerShopItems item : AutoclickerShopItems.values())
		{
			int count = getShopItemCount(item);

			double inhPt = ((double) count) * item.getPerTick();
			iNeedHealings = iNeedHealings.add(BigDecimal.valueOf(inhPt));
		}

		//Calculate iNeedHealings per second
		if(lastTime == 0)
		{
			lastTime = System.currentTimeMillis();
			lastINeedHealings = iNeedHealings;
		}
		if(System.currentTimeMillis() > lastTime + 1000)
		{
			long timeDiff = System.currentTimeMillis() - lastTime;
			BigDecimal valueDiff = iNeedHealings.subtract(lastINeedHealings);

			iNeedHealingsPerSecond = valueDiff.divide(BigDecimal.valueOf(timeDiff), 10, BigDecimal.ROUND_HALF_UP)
					.multiply(BigDecimal.valueOf(1000)).setScale(3, BigDecimal.ROUND_HALF_UP);

			lastTime = System.currentTimeMillis();
			lastINeedHealings = iNeedHealings;
		}
	}

	public void calculatePerClick()
	{
		iNeedHealingsPerClick = BigDecimal.ONE;

		for(UpgradeShopItems item : UpgradeShopItems.values())
		{
			int count = getShopItemCount(item);
			BigDecimal multiplier = BigDecimal.valueOf(item.getMultiplier()).pow(count);

			iNeedHealingsPerClick = iNeedHealingsPerClick.multiply(multiplier);
		}
	}

	public int getShopItemCount(ShopItem item)
	{
		return shopItemCountMap.containsKey(item) ? shopItemCountMap.get(item) : 0;
	}
}
