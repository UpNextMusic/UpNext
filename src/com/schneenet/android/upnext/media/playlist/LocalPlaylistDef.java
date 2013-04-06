package com.schneenet.android.upnext.media.playlist;

import java.util.ArrayList;

public class LocalPlaylistDef
{
	
	private ArrayList<Long> mSongIds;
	
	public LocalPlaylistDef()
	{
		mSongIds = new ArrayList<Long>();
	}
	
	public void addTrackId(long trackId)
	{
		mSongIds.add(trackId);
	}
	
	public void clear()
	{
		mSongIds.clear();
	}
	
	public ArrayList<Long> getTrackIds()
	{
		return mSongIds;
	}
	
}
