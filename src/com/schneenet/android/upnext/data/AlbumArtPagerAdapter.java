package com.schneenet.android.upnext.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
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

public class AlbumArtPagerAdapter extends PagerAdapter implements OnPageChangeListener {

	private Playlist mPlaylist;
	private UpNextApplication mApplicationObj;
	private TrackSelectedListener mTSListener;
	private LayoutInflater mInflater;
	
	public AlbumArtPagerAdapter(UpNextApplication appObj, TrackSelectedListener tsListener, ViewPager pager)
	{
		mApplicationObj = appObj;
		mTSListener = tsListener;
		mInflater = (LayoutInflater) appObj.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		pager.setAdapter(this);
	}
	
	public void setPlaylist(Playlist playlist)
	{
		mPlaylist = playlist;
		notifyDataSetChanged();
	}
	
	@Override
	public int getItemPosition(Object obj)
	{
		return POSITION_UNCHANGED;
	}
	
	@Override
	public View instantiateItem(ViewGroup container, int position)
	{
		Playable track = mPlaylist.getTrack(position, false);
		final View theView = mInflater.inflate(R.layout.album_art_page, container, false);
		if (track != null)
		{
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
		}
		container.addView(theView);
		return theView;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object)
	{
		container.removeView((View) object);
	}
	
	@Override
	public boolean isViewFromObject(View view, Object obj)
	{
		return view == obj;
	}
	
	@Override
	public int getCount()
	{
		return mPlaylist != null ? mPlaylist.size() : 0;
	}

	@Override
	public void onPageScrollStateChanged(int state)
	{
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
	{
	}

	@Override
	public void onPageSelected(int position)
	{
		// TODO For now, we just fire onPageSelected, in the future we want Spotify-like behavior where we can scroll through the pages for a few seconds and select a song
		mTSListener.onTrackSelected(mPlaylist, position);
	}
	
	public interface TrackSelectedListener
	{
		public void onTrackSelected(Playlist playlist, int index);
	}

}
