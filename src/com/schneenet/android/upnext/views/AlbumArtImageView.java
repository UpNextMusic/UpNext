package com.schneenet.android.upnext.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class AlbumArtImageView extends ImageView
{

	public AlbumArtImageView(Context context)
	{
		super(context);
	}

	public AlbumArtImageView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public AlbumArtImageView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int square = getMeasuredWidth();
		setMeasuredDimension(square, square);
	}
}
