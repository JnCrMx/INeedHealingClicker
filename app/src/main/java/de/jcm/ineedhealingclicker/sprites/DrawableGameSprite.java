package de.jcm.ineedhealingclicker.sprites;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;

import java.util.Objects;

/**
 * Created by JCM on 09.11.2018.
 */
public abstract class DrawableGameSprite extends GameSprite
{
	protected Bitmap bitmap;
	protected Drawable drawable;

	public DrawableGameSprite(@NonNull Bitmap bitmap)
	{
		this.bitmap = bitmap;
		this.width = bitmap.getWidth();
		this.height = bitmap.getHeight();
	}

	public DrawableGameSprite(Resources resources, @DrawableRes int drawableRes)
	{
		this(Objects.requireNonNull(ResourcesCompat.getDrawable(resources, drawableRes, null)));
	}

	public DrawableGameSprite(Resources resources, @DrawableRes int drawableRes, int width, int height)
	{
		this(Objects.requireNonNull(ResourcesCompat.getDrawable(resources, drawableRes, null)), width, height);
	}

	public DrawableGameSprite(@NonNull Drawable drawable)
	{
		this(drawable, drawable.getMinimumWidth(), drawable.getMinimumHeight());
	}

	public DrawableGameSprite(@NonNull Drawable drawable, int width, int height)
	{
		if(this.shouldRenderDrawable())
		{
			this.drawable = drawable;
		}

		if(!this.shouldRenderDrawable() || this.shouldUseAdvancedContains())
		{
			bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, width, height);
			drawable.draw(canvas);
		}

		this.width = width;
		this.height = height;
	}

	@Override
	public void draw(@NonNull Canvas canvas)
	{
		if(isVisible())
		{
			if(this.shouldRenderDrawable() && drawable != null)
			{
				drawable.setBounds((int) posX, (int) posY, (int) (posX + width * scale), (int) (posY + height * scale));
				drawable.draw(canvas);
			}
			else
			{
				if(bitmap == null)
					return;

				Matrix matrix = new Matrix();
				matrix.postTranslate((float) posX, (float) posY);
				matrix.postScale(scale, scale);

				canvas.drawBitmap(bitmap, matrix, new Paint());
			}
		}
	}

	public boolean contains(int x, int y)
	{
		if(shouldUseAdvancedContains() && bitmap != null)
		{
			if(!getBoundingRectangle().contains(x, y))
				return false;

			int ix = (int) (x - posX);
			int iy = (int) (y - posY);

			int color = bitmap.getPixel(ix, iy);

			return Color.alpha(color) == 0xFF;
		}
		else
			return getBoundingRectangle().contains(x, y);
	}

}
