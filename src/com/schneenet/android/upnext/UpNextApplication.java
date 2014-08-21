package com.schneenet.android.upnext;

import android.content.Intent;

import com.schneenet.android.lib.musicclubplayer.MusicClubPlayerApplication;
import com.schneenet.android.upnext.media.playlist.LocalPlaylistManager;

public class UpNextApplication extends MusicClubPlayerApplication {

	@Override
	public void onCreate()
	{
		setPlaylistManager(new LocalPlaylistManager(this));
		super.onCreate();
	}
	
	@Override
	public void launchPlayer() {
		Intent playerIntent = new Intent(this, MainActivity.class);
		playerIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(playerIntent);
	}

}
