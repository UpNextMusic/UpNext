package com.schneenet.android.upnext.media;

public abstract class ContentObject
{
	
	protected long mId;
	
	protected ContentObject(long id)
	{
		mId = id;
	}
	
	public long getId()
	{
		return mId;
	}
	
}
