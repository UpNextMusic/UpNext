package com.schneenet.android.upnext.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.schneenet.android.lib.musicclubplayer.media.Playable;
import com.schneenet.android.lib.musicclubplayer.media.Playlist;
import com.schneenet.android.upnext.R;
import com.schneenet.android.upnext.views.SwipeDismissTouchListener;
import com.schneenet.android.upnext.views.SwipeDismissTouchListener.OnDismissCallback;

public class PlaylistAdapter extends BaseAdapter implements OnDismissCallback {

	private Playlist mPlaylist;
	private LayoutInflater mLayoutInflater;
	private OnItemDeleteListener mItemDeleteListener;
	
	public PlaylistAdapter(Context context, OnItemDeleteListener itemDeleteListener)
	{
		mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mItemDeleteListener = itemDeleteListener;
	}
	
	public void setPlaylist(Playlist playlist)
	{
		mPlaylist = playlist;
		notifyDataSetChanged();
	}
	
	public Playable getSongItem(int position)
	{
		return mPlaylist.getTrack(position, false);
	}
	
	@Override
	public int getCount() {
		return mPlaylist.size();
	}

	@Override
	public Object getItem(int position) {
		return getSongItem(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		// Create a new view to use if we aren't already recycling one
		if (convertView == null)
		{
			convertView = mLayoutInflater.inflate(R.layout.playable_item, parent, false);
		}
		
		// Get reference to item at position
		Playable playable = getSongItem(position);
		
		// Get references to individual views
		TextView textViewPrimary = (TextView) convertView.findViewById(R.id.playable_text_primary);
		TextView textViewSecondary = (TextView) convertView.findViewById(R.id.playable_text_secondary);
		
		// Populate individual views with data from getSongItem
		textViewPrimary.setText(playable.getName());
		textViewSecondary.setText(playable.getArtist());
		
		// Add SwipeDismissTouchListener
		convertView.setOnTouchListener(new SwipeDismissTouchListener(convertView, Integer.valueOf(position), this));
		
		// Return it
		return convertView;
	}
	
	public interface OnItemDeleteListener
	{
		public void onItemDelete(int index);
	}

	@Override
	public void onDismiss(View view, Object token)
	{
		int position = ((Integer) token).intValue();
		mItemDeleteListener.onItemDelete(position);
	}

}
