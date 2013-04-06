package com.schneenet.android.upnext.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ListIconImageView extends ImageView
{

	public ListIconImageView(Context context)
	{
		super(context);
	}
	
	public ListIconImageView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}
	
	public ListIconImageView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int square = getMeasuredHeight();
		setMeasuredDimension(square, square);
	}
}
