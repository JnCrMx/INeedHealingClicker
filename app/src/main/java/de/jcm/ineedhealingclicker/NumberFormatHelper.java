package de.jcm.ineedhealingclicker;

import android.support.annotation.VisibleForTesting;

import java.math.BigDecimal;
import java.util.LinkedList;

/**
 * Created by JCM on 16.12.2018.
 */
public class NumberFormatHelper
{
	@VisibleForTesting
	public static LinkedList<String> suffixes = new LinkedList<>();

	public static void init(GameResources resources)
	{
		suffixes = resources.numberNames;
	}

	public static String bigDecimalToString(BigDecimal decimal)
	{
		int length = decimal.toBigInteger().toString().length() - 1;
		int triples = (int) Math.floor(length / 3.0);

		BigDecimal decimal1 = decimal.movePointLeft(triples * 3);
		BigDecimal decimal2 = decimal1.setScale(1, BigDecimal.ROUND_UP);

		String string = decimal2.toString();
		if(string.endsWith(".0"))
		{
			string = string.substring(0, string.lastIndexOf('.'));
		}
		string = string + " " + suffixes.get(triples);

		return string;
	}
}
