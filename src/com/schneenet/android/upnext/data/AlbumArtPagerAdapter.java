package com.schneenet.android.upnext.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.schneenet.android.lib.musicclubplayer.MusicClubPlayerApplication.ArtLoaderCallback;
import com.schneenet.android.lib.musicclubplayer.media.Playable;
import com.schneenet.android.lib.musicclubplayer.media.Playlist;
import com.schneenet.android.upnext.R;
import com.schneenet.android.upnext.UpNextApplication;

public class AlbumArtPagerAdapter extends PagerAdapter {

	private Playlist mPlaylist;
	private LayoutInflater mLayoutInflater;
	private UpNextApplication mApplicationObj;
	
	public AlbumArtPagerAdapter(UpNextApplication appObj, Playlist playlist)
	{
		mPlaylist = playlist;
		mApplicationObj = appObj;
		mLayoutInflater = (LayoutInflater) appObj.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position)
	{
		Playable track = mPlaylist.getTrack(position, false);
		
		final View theView = mLayoutInflater.inflate(R.layout.album_art_page, container, false);
		final ImageView albumArtView = (ImageView) theView.findViewById(R.id.track_control_album_art);
		final TextView songTitleView = (TextView) theView.findViewById(R.id.track_control_label_title);
		final TextView artistView = (TextView) theView.findViewById(R.id.track_control_label_artist);
		
		theView.setTag(track.getKey());
		songTitleView.setText(track.getName());
		artistView.setText(track.getArtist());
		mApplicationObj.fetchArtImage(track.getArtUrl(), new ArtLoaderCallback()
		{
			@Override
			public void onArtLoaded(Bitmap bitmap)
			{
				albumArtView.setImageBitmap(bitmap);
			}
		});
		
		container.addView(theView, position);
		return theView;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object)
	{
		container.removeViewAt(position);
	}
	
	@Override
	public int getCount() {
		return mPlaylist.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object)
	{
		return view == object;
	}

}
