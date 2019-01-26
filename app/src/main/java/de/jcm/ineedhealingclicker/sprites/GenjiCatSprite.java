package de.jcm.ineedhealingclicker.sprites;

import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

import java.math.BigDecimal;
import java.util.Random;

import de.jcm.ineedhealingclicker.GameData;
import de.jcm.ineedhealingclicker.render.GameSurfaceView;
import de.jcm.ineedhealingclicker.screens.MainScreen;

/**
 * Created by JCM on 03.11.2018.
 */
public class GenjiCatSprite extends DrawableGameSprite
{
	private long expandUntil = 0;
	private Random random;

	public GenjiCatSprite(Drawable drawable, int width, int height)
	{
		super(drawable, width, height);
		this.random = new Random();
	}

	private void expand()
	{
		if(expandUntil == 0)
		{
			this.scale = 1.1f;
			this.posX = posX - (width * 0.75) * 0.1;
			this.posY = posY - (height * 0.75) * 0.1;
			this.expandUntil = System.currentTimeMillis() + 100;
		}
	}

	private void collapse()
	{
		expandUntil = 0;

		this.scale = 1.0f;
		this.posX = posX + (width * 0.75) * 0.1;
		this.posY = posY + (height * 0.75) * 0.1;
	}

	@Override
	public void tick()
	{

	}

	@Override
	public void renderTick()
	{
		if(expandUntil != 0)
		{
			if(expandUntil <= System.currentTimeMillis())
			{
				collapse();
			}
		}
	}

	@Override
	protected boolean shouldUseAdvancedContains()
	{
		return true;
	}

	@Override
	protected boolean shouldRenderDrawable()
	{
		return true;
	}

	@Override
	public void onClick(MotionEvent event, GameSurfaceView view, GameData data)
	{
		if(event.getAction() == MotionEvent.ACTION_DOWN)
		{
			expand();

			if(data.critical)
			{
				data.iNeedHealings = data.iNeedHealings.add(
						data.iNeedHealingsPerClick.multiply(BigDecimal.TEN));
			}
			else
			{
				data.iNeedHealings = data.iNeedHealings.add(data.iNeedHealingsPerClick);

				int bubble = random.nextInt(4);

				Drawable drawable = null;

				switch(bubble)
				{
					case 0:
						drawable = view.resources.speechBubbleNeedLeft;
						break;
					case 1:
						drawable = view.resources.speechBubbleNeedRight;
						break;
					case 2:
						drawable = view.resources.speechBubbleRequireLeft;
						break;
					case 3:
						drawable = view.resources.speechBubbleRequireRight;
						break;
				}

				SpeechBubbleSprite sprite = new SpeechBubbleSprite(drawable);
				sprite.posX = random.nextInt(view.getWidth());
				sprite.posY = random.nextInt(view.getHeight() - 100) + 50;
				((MainScreen) view.screens.getLast()).spawnBubble(sprite);
			}
		}
	}
}
