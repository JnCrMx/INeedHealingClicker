package de.jcm.ineedhealingclicker;

import java.util.concurrent.atomic.AtomicInteger;

import de.jcm.ineedhealingclicker.shop.ShopCategories;

/**
 * Created by JCM on 09.11.2018.
 */
public class LoadingThread extends Thread
{
	public static final int PROGRESS_COUNT = 10;
	public static final int PROGRESS_END =
			GameResources.PROGRESS_COUNT +
					LoadingThread.PROGRESS_COUNT +
					ShopCategories.getAllProgressCount();

	private final AtomicInteger progress = new AtomicInteger();
	private final GameResources resources;

	private final Runnable onComplete;

	public LoadingThread(GameResources resources, Runnable onComplete)
	{
		this.resources = resources;
		this.onComplete = onComplete;
	}

	@Override
	public void run()
	{
		resources.init(progress);

		for(int i = 0; i < 10; i++)
		{
			//Simulate loading some stuff
			try
			{
				Thread.sleep(100);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
			progress.incrementAndGet();
		}

		ShopCategories.initAll(resources, progress);
		NumberFormatHelper.init(resources);

		onComplete.run();
	}

	public AtomicInteger getProgress()
	{
		return progress;
	}
}
