package com.schneenet.android.upnext;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.schneenet.android.lib.musicclubplayer.PlayerObserver;
import com.schneenet.android.lib.musicclubplayer.PlayerService;
import com.schneenet.android.lib.musicclubplayer.media.Playlist;
import com.schneenet.android.upnext.data.AlbumArtPagerAdapter;
import com.schneenet.android.upnext.data.AlbumArtPagerAdapter.TrackSelectedListener;
import com.schneenet.android.upnext.data.PlaylistAdapter;
import com.schneenet.android.upnext.data.PlaylistAdapter.OnItemDeleteListener;
import com.schneenet.android.upnext.media.MediaAccessTask;
import com.schneenet.android.upnext.media.MediaAccessTask.PlaylistLoadedCallback;
import com.schneenet.android.upnext.media.playlist.LocalPlaylist;
import com.schneenet.android.upnext.media.playlist.LocalPlaylistDef;

public class MainActivity extends FragmentActivity implements PlayerObserver, OnItemDeleteListener, TrackSelectedListener
{
	
	private ViewGroup mPlaylistView;
	private ViewPager mTrackPager;
	private TextView mLabelTime;
	private TextView mLabelTimeOf;
	private ImageView mIconStatus;
	//private SeekBar mSeekBar;
	private ProgressBar mProgressBar;
	private int mCurrentPage;
	
	private UpNextApplication mApplicationObj;
	private PlaylistAdapter mPlaylistAdapter;
	private AlbumArtPagerAdapter mAlbumArtAdapter;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mApplicationObj = (UpNextApplication) getApplication();
		
		// Inflate and define layout/views
		setContentView(R.layout.main_new);
		mPlaylistView = (ViewGroup) findViewById(R.id.playlist_view);
		mTrackPager = (ViewPager) findViewById(R.id.track_pager);
		mLabelTime = (TextView) findViewById(R.id.main_control_label_time);
		mLabelTimeOf = (TextView) findViewById(R.id.main_control_label_time_of);
		mIconStatus = (ImageView) findViewById(R.id.main_control_icon_status);
		mProgressBar = (ProgressBar) findViewById(R.id.main_control_progress);
		//mSeekBar = (SeekBar) findViewById(R.id.main_control_seek);
		
		// Create adapters
		mPlaylistAdapter = new PlaylistAdapter(this, this);
		mAlbumArtAdapter = new AlbumArtPagerAdapter(mApplicationObj, this, mTrackPager);
		
		// Register this activity as a player observer to receive callbacks about PlayerService events
		mApplicationObj.registerPlayerObserver(this);
		
		mTrackPager.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				mApplicationObj.sendCommand(PlayerService.ACTION_CONTROL_PLAYPAUSE);				
			}
		});
		
		mTrackPager.setOnPageChangeListener(new SimpleOnPageChangeListener()
		{
			@Override
			public void onPageSelected(int position)
			{
				if (position > mCurrentPage)
				{
					// Next
					mApplicationObj.sendCommand(PlayerService.ACTION_CONTROL_NEXT);
				}
				else
				{
					// Previous
					mApplicationObj.sendCommand(PlayerService.ACTION_CONTROL_PREV);
				}
				mCurrentPage = position;
			}
		});
		
		// TESTING ONLY
		debug();
		
	}
	
	public void onResume()
	{
		super.onResume();
		mApplicationObj.requestMetadataUpdate();
	}
	
	public void onDestroy()
	{
		super.onDestroy();
		mApplicationObj.unregisterPlayerObserver(this);
	}

	@Override
	public void onMetaDataUpdate(String title, String artist, String album,
			String albumArtUrl, String track, String trackOf, boolean playing,
			boolean repeat, boolean shuffle)
	{
		mIconStatus.setImageResource(playing ? android.R.drawable.ic_media_play : android.R.drawable.ic_media_pause);
		
	}

	@Override
	public void onProgressChanged(float position, float buffered, float duration)
	{
		//mSeekBar.setMax(Math.round(duration));
		//mSeekBar.setProgress(Math.round(position));
		//mSeekBar.setSecondaryProgress(Math.round(buffered));
		mProgressBar.setMax(Math.round(duration));
		mProgressBar.setProgress(Math.round(position));
		mProgressBar.setSecondaryProgress(Math.round(buffered));
		mLabelTimeOf.setText(DateUtils.formatElapsedTime(Math.round(duration)));
		mLabelTime.setText(DateUtils.formatElapsedTime(Math.round(position)));
	}

	@Override
	public void onPlaylistUpdate(Playlist playlist, int currentIndex)
	{
		// Grab the next two items in the playlist for the "Up Next" portion of the layout
		mPlaylistAdapter.setPlaylist(playlist);
		mPlaylistView.removeAllViews();
		for (int i = currentIndex + 1; i < currentIndex + 3 && i < playlist.size(); i++)
		{
			mPlaylistView.addView(mPlaylistAdapter.getView(i, null, mPlaylistView));
		}
		
		// Whole playlist is in the view pager
		mAlbumArtAdapter.setPlaylist(playlist);
		mTrackPager.setCurrentItem(currentIndex, true);
	}

	@Override
	public void onItemDelete(int index)
	{
		mApplicationObj.removeFromPlaylist(index);
	}
	
	
	/* ******************************************************************************** */
	/**
	 * DEBUGGING, MAKE A PLAYLIST FROM ALL MUSIC FILES AND START PLAYING IT
	 */
	void debug()
	{
		LocalPlaylistDef playlistDef = new LocalPlaylistDef();
		String[] projection = {
				MediaStore.Audio.Media._ID
		};
		Cursor c = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, MediaStore.Audio.Media.IS_MUSIC + " = 1", null, null);
		while (c.moveToNext())
		{
			playlistDef.addTrackId(c.getLong(c.getColumnIndex(MediaStore.Audio.Media._ID)));
		}
		MediaAccessTask mal = new MediaAccessTask(this);
		mal.loadPlaylistAsync(new PlaylistLoadedCallback() {
			@Override
			public void onPlaylistLoaded(LocalPlaylist playlist)
			{
				Log.e("MainActivity", "Playlist count = " + playlist.size());
				//mApplicationObj.enqueueTracks(playlist);
				mApplicationObj.playTracks(playlist, 0, true);
			}
		}, playlistDef);
	}

	@Override
	public void onTrackSelected(Playlist playlist, int index)
	{
		mApplicationObj.playTrackAt(index, false);
	}
	
}
