package de.jcm.ineedhealingclicker;

import org.junit.Test;

import java.math.BigDecimal;

import de.jcm.ineedhealingclicker.shop.AutoclickerShopItems;
import de.jcm.ineedhealingclicker.shop.ShopCategories;
import de.jcm.ineedhealingclicker.shop.UpgradeShopItems;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest
{
	@Test
	public void testLoadingTime()
	{
		int resourceCount = GameResources.PROGRESS_COUNT;
		int simulationCount = LoadingThread.PROGRESS_COUNT;
		int shopItemCount = ShopCategories.getAllProgressCount();

		System.out.println("Steps for GameResources.init(...) : " + resourceCount);
		System.out.println("Steps for simulating work in LoadingThread : " + simulationCount);
		System.out.println("Steps for ShopCategories.initAll(GameResources, ...) : " + shopItemCount);

		int sum = resourceCount + simulationCount + shopItemCount;
		assertEquals(LoadingThread.PROGRESS_END, sum);
	}

	@Test
	public void testNumberFormat()
	{
		NumberFormatHelper.suffixes.add("3");
		NumberFormatHelper.suffixes.add("6");
		NumberFormatHelper.suffixes.add("9");
		NumberFormatHelper.suffixes.add("12");
		NumberFormatHelper.suffixes.add("15");
		NumberFormatHelper.suffixes.add("18");

		BigDecimal big = new BigDecimal("1000000");

		System.out.println(big);
		System.out.println(NumberFormatHelper.bigDecimalToString(big));

		big = new BigDecimal("100");

		System.out.println(big);
		System.out.println(NumberFormatHelper.bigDecimalToString(big));
	}

	@Test
	public void testDataNames()
	{
		for(AutoclickerShopItems item : AutoclickerShopItems.values())
		{
			System.out.println(item.getDataName());
			assertEquals(item.getDataName(), "AutoclickerShopItems_" + item.name());
		}
		for(UpgradeShopItems item : UpgradeShopItems.values())
		{
			System.out.println(item.getDataName());
			assertEquals(item.getDataName(), "UpgradeShopItems_" + item.name());
		}
	}
}