package com.schneenet.android.upnext.media;

import com.schneenet.android.lib.musicclubplayer.media.Playable;

public class Song extends ContentObject implements Playable {

	protected Song(long id)
	{
		super(id);
	}
	
	/**
	 * Create a new instance from an ID
	 * 
	 * Looks up the song in the database
	 * @param id Song ID
	 * @return Instance of Song
	 */
	public static Song create(long id, long artistId, long albumId, String title, String artist, String album, String genre, String art, String url, int track)
	{
		Song s = new Song(id);
		s.artistId = artistId;
		s.albumId = albumId;
		s.name = title;
		s.artist = artist;
		s.album = album;
		s.genre = genre;
		s.art = art;
		s.url = url;
		s.track = track;
		return s;
	}
	
	public long artistId;
	public long albumId;
	public long time;
	public String name;
	public String artist;
	public String album;
	public String genre;
	public String art;
	public String url;
	public int track;
	
	public long getId()
	{
		return mId;
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public float getLength() {
		return time;
	}

	@Override
	public String getKey() {
		return this.getClass().getName() + ":" + String.valueOf(mId);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getArtist() {
		return artist;
	}

	@Override
	public String getAlbum() {
		return album;
	}

	@Override
	public String getArtUrl()
	{
		return art;
	}
}
