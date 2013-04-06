package com.schneenet.android.upnext.media.playlist;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.schneenet.android.lib.musicclubplayer.media.Playable;
import com.schneenet.android.lib.musicclubplayer.media.Playlist;

public class LocalPlaylist implements Playlist
{

	private ArrayList<Playable> mSongList;
	private ArrayList<Playable> mSongListRand;

	@Override
	public List<Playable> getTrackList()
	{
		return mSongList;
	}
	
	@Override
	public void addTrack(Playable track) {
		mSongList.add(track);
		randomize();
	}

	@Override
	public void append(Playlist plist)
	{
		addTracks(plist.getTrackList());
	}

	@Override
	public void addTracks(List<Playable> tracks)
	{
		mSongList.addAll(tracks);
		randomize();
	}
	
	@Override
	public Playable deleteTrackAt(int position)
	{
		return mSongList.remove(position);
	}
	
	@Override
	public boolean deleteTrack(Playable track)
	{
		return mSongList.remove(track);
	}

	@Override
	public void clear() {
		mSongList.clear();
		mSongListRand.clear();
	}

	@Override
	public Playable getTrack(int index, boolean shuffle) {
		return shuffle ? mSongListRand.get(index) : mSongList.get(index);
	}

	@Override
	public int size() {
		return mSongList.size();
	}

	/**
	 * Get the track number of the song in the playlist
	 * 
	 * @param s
	 *            Playable to search for
	 * @return Track number 1 -> size() or -1 if the song is not in the list
	 */
	@Override
	public int getTrackNumber(Playable s) {
		final int x = mSongList.indexOf(s);
		return x < 0 ? x : x + 1;
	}
	
	@Override
	public int getTrackPosition(Playable s, boolean shuffle)
	{
		if (shuffle) {
			return mSongListRand.indexOf(s);
		} else {
			return mSongList.indexOf(s);
		}
	}

	public int syncPlaylistPosition(boolean shuffle, Playable nowPlaying) {
		return shuffle ? mSongListRand.indexOf(nowPlaying) : mSongList.indexOf(nowPlaying);
	}

	@Override
	public boolean isSamePlaylist(Playlist other) {
		if (size() != other.size())
			return false; // Playlists are different because of size
		final int n = size();
		for (int i = 0; i < n; i++) {
			// Playlists are difference because the item's key at "i"
			if (!getTrack(i, false).getKey().equals(other.getTrack(i, false).getKey()))
				return false;
			// Playlists are different because the item's length at "i"
			if (getTrack(i, false).getLength() != other.getTrack(i, false).getLength())
				return false;
			// Playlists are difference because the item's URL at "i"
			if (!getTrack(i, false).getUrl().equals(other.getTrack(i, false).getUrl()))
				return false;
		}
		// You made it through the entire playlist and everything matched,
		// therefore...
		return true; // Playlists are identical
	}

	void randomize() {
		ArrayList<Playable> tracks = new ArrayList<Playable>(mSongList);
		mSongListRand.clear();
		Random rand = new Random(System.currentTimeMillis());
		final int n = mSongList.size();
		for (int i = 0; i < n; i++) {
			mSongListRand.add(tracks.remove(rand.nextInt(tracks.size())));
		}
	}
	
}
