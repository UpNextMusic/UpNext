package com.schneenet.android.upnext.media;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.schneenet.android.upnext.media.playlist.LocalPlaylist;
import com.schneenet.android.upnext.media.playlist.LocalPlaylistDef;

/**
 * Provides methods for querying the device for music
 * 
 * @author Matt
 * 
 */
public class MediaAccessTask extends AsyncTask<LocalPlaylistDef, Void, LocalPlaylist>
{
	private Context mContext;
	private PlaylistLoadedCallback mCallback;

	public MediaAccessTask(Context ctxt)
	{
		mContext = ctxt;
	}

	public void loadPlaylistAsync(final PlaylistLoadedCallback cb, LocalPlaylistDef def)
	{
		mCallback = cb;
		execute(def);
	}

	/**
	 * Synchronous lookup of a Song by id
	 * @param id Song ID
	 * @return Song object
	 */
	public Song lookupSong(long id)
	{

		Cursor c = null;
		try
		{
			String[] projection = {
					MediaStore.Audio.Media._ID,
					MediaStore.Audio.Media.ARTIST_ID,
					MediaStore.Audio.Media.ALBUM_ID,
					MediaStore.Audio.Media.TITLE,
					MediaStore.Audio.Media.ARTIST,
					MediaStore.Audio.Media.ALBUM,
					MediaStore.Audio.Media.DURATION,
					MediaStore.Audio.Media.TRACK,
					MediaStore.Audio.Media.DATA
			};
			c = mContext.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, MediaStore.Audio.Media.IS_MUSIC + " = ? && " + MediaStore.Audio.Media._ID + " = ?", new String[] {
					"true", String.valueOf(id)
			}, null);
			if (c.moveToNext())
			{
				long _id = c.getLong(c.getColumnIndex(MediaStore.Audio.Media._ID));
				long artistId = c.getLong(c.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID));
				long albumId = c.getLong(c.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
				String title = c.getString(c.getColumnIndex(MediaStore.Audio.Media.TITLE));
				String artist = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST));
				String album = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM));
				String genre = ""; // TODO Genre
				String url = c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA));
				String art = ""; // TODO Album Art URL/URI
				int track = c.getInt(c.getColumnIndex(MediaStore.Audio.Media.TRACK));
				return Song.create(_id, artistId, albumId, title, artist, album, genre, art, url, track);
			}
			return null;
		} finally
		{
			if (c != null)
				c.close();
		}
	}
	
	/**
	 * Build a playlist from a list of song IDs
	 * @param ids 
	 * @return
	 */
	public LocalPlaylist inflatePlaylist(LocalPlaylistDef def)
	{
		// Turn an array of long into a playlist
		LocalPlaylist playlist = new LocalPlaylist();
		for (Long param : def.getTrackIds())
		{
			playlist.addTrack(lookupSong(param));
		}
		return playlist;
	}

	@Override
	protected LocalPlaylist doInBackground(LocalPlaylistDef... params)
	{
		return inflatePlaylist(params[0]);
	}

	@Override
	protected void onPostExecute(LocalPlaylist result)
	{
		mCallback.onPlaylistLoaded(result);
	}

	public interface PlaylistLoadedCallback
	{
		public void onPlaylistLoaded(LocalPlaylist playlist);
	}

}
