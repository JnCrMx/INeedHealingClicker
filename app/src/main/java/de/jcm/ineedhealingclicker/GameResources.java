package de.jcm.ineedhealingclicker;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.res.ResourcesCompat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by JCM on 06.11.2018.
 */
public class GameResources
{
	public static final int PROGRESS_COUNT = 15;
	//preInit
	public List<String> loadingScreenMessages;
	public @ColorInt
	int background;
	public @NonNull
	Drawable largeHealthPack;
	//init
	public LinkedList<String> numberNames;
	public MediaPlayer iNeedHealingSound;
	public Typeface overwatchFont;
	public @NonNull
	Drawable criticalBackground;
	public @NonNull
	Drawable vectorGenji;
	public @NonNull
	Drawable speechbubbleNeedLeft;
	public @NonNull
	Drawable speechbubbleNeedRight;
	public @NonNull
	Drawable speechbubbleRequireLeft;
	public @NonNull
	Drawable speechbubbleRequireRight;
	public @NonNull
	Drawable criticalSpeechbubbleNeedLeft;
	public @NonNull
	Drawable criticalSpeechbubbleNeedRight;
	public @NonNull
	Drawable criticalSpeechbubbleRequireLeft;
	public @NonNull
	Drawable criticalSpeechbubbleRequireRight;
	public @NonNull
	Drawable healingSymbol;
	public @NonNull
	Drawable settingsSymbol;
	public @NonNull
	Drawable shopSymbol;
	public @NonNull
	Drawable closeSymbol;
	public @NonNull
	Drawable upgradeSymbol;
	public @ColorInt
	int buttonColor;
	public String settingsReset;
	private MainActivity activity;
	private Resources resources;
	private HashMap<Integer, Drawable> drawableCache;
	private HashMap<Integer, String> stringCache;

	public GameResources(MainActivity activity)
	{
		this.activity = activity;
		this.resources = activity.getResources();

		this.drawableCache = new HashMap<>();
		this.stringCache = new HashMap<>();
	}

	private String loadString(@StringRes int id)
	{
		String string = Objects.requireNonNull(resources.getString(id));
		stringCache.put(id, string);
		return string;
	}

	public String getString(@StringRes int id)
	{
		if(stringCache.containsKey(id))
		{
			return stringCache.get(id);
		}
		else
		{
			return loadString(id);
		}
	}

	private Drawable loadDrawable(@DrawableRes int id)
	{
		Drawable drawable = Objects.requireNonNull(
				ResourcesCompat.getDrawable(resources, id, null));
		drawableCache.put(id, drawable);
		return drawable;
	}

	public Drawable getDrawable(@DrawableRes int id)
	{
		if(drawableCache.containsKey(id))
		{
			return drawableCache.get(id);
		}
		else
		{
			return loadDrawable(id);
		}
	}

	//Loads the resources required for LoadingScreen
	public void preInit()
	{
		String[] messages = resources.getStringArray(R.array.loading_messages);
		loadingScreenMessages = Arrays.asList(messages);

		background = ResourcesCompat.getColor(resources, R.color.background, null);

		largeHealthPack = loadDrawable(R.drawable.ic_health_pack_large);

		overwatchFont = ResourcesCompat.getFont(activity, R.font.bignoodletoo);
	}

	//Loads the other resources
	public void init(AtomicInteger progress)
	{
		iNeedHealingSound = MediaPlayer.create(activity.getApplicationContext(),
				R.raw.i_need_healing);
		progress.incrementAndGet();

		String[] numbers = resources.getStringArray(R.array.numbers);
		numberNames = new LinkedList<>();
		for(int i = 0; i < numbers.length; i++)
		{
			numberNames.add(numbers[i]);
		}
		progress.incrementAndGet();

		criticalBackground = loadDrawable(R.drawable.critical_background);
		progress.incrementAndGet();

		vectorGenji = loadDrawable(R.drawable.ic_genji_cat);
		progress.incrementAndGet();

		speechbubbleNeedLeft = loadDrawable(R.drawable.ic_speechbubble_need_left);
		progress.incrementAndGet();
		speechbubbleNeedRight = loadDrawable(R.drawable.ic_speechbubble_need_right);
		progress.incrementAndGet();
		speechbubbleRequireLeft = loadDrawable(R.drawable.ic_speechbubble_require_left);
		progress.incrementAndGet();
		speechbubbleRequireRight = loadDrawable(R.drawable.ic_speechbubble_require_right);
		progress.incrementAndGet();

		healingSymbol = loadDrawable(R.drawable.ic_healing);
		progress.incrementAndGet();

		settingsSymbol = loadDrawable(R.drawable.ic_settings);
		progress.incrementAndGet();
		shopSymbol = loadDrawable(R.drawable.ic_shop);
		progress.incrementAndGet();
		closeSymbol = loadDrawable(R.drawable.ic_close);
		progress.incrementAndGet();

		upgradeSymbol = loadDrawable(R.drawable.ic_upgrade);
		progress.incrementAndGet();

		buttonColor = ResourcesCompat.getColor(resources, R.color.button, null);
		progress.incrementAndGet();

		settingsReset = loadString(R.string.settings_reset);
		progress.incrementAndGet();
	}
}
