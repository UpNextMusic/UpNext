package com.schneenet.android.upnext.media.playlist;

import java.lang.reflect.Type;
import java.util.Iterator;

import android.content.Context;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.schneenet.android.lib.musicclubplayer.media.Playable;
import com.schneenet.android.lib.musicclubplayer.media.Playlist;
import com.schneenet.android.lib.musicclubplayer.media.PlaylistManager;
import com.schneenet.android.upnext.media.MediaAccessTask;

public class LocalPlaylistManager extends PlaylistManager implements JsonSerializer<LocalPlaylist>, JsonDeserializer<LocalPlaylist>
{
	
	private MediaAccessTask mMediaAccess;
	
	public LocalPlaylistManager(Context ctxt)
	{
		mMediaAccess = new MediaAccessTask(ctxt);
	}
	
	@Override
	public JsonElement serialize(LocalPlaylist src, Type typeOfSrc, JsonSerializationContext context)
	{
		JsonObject plObject = new JsonObject();
		JsonArray slArray = new JsonArray();
		Iterator<Playable> iter = src.getTrackList().iterator();
		while (iter.hasNext()) {
			Playable item = iter.next();
			slArray.add(context.serialize(item.getKey()));
		}
		plObject.add(PROP_SONGLIST, slArray);
		return plObject;
	}

	@Override
	public LocalPlaylist deserialize(JsonElement src, Type typeOfSrc, JsonDeserializationContext context) throws JsonParseException {
		JsonObject plObject = src.getAsJsonObject();
		LocalPlaylistDef newPlaylistDef = new LocalPlaylistDef();
		Iterator<JsonElement> iter = plObject.get(PROP_SONGLIST).getAsJsonArray().iterator();
		while (iter.hasNext()) {
			long key = iter.next().getAsLong();
			newPlaylistDef.addTrackId(key);
		}
		return mMediaAccess.inflatePlaylist(newPlaylistDef);
	}

	private static final String PROP_SONGLIST = "prop_Playlist_songList";
	
	@Override
	public String serializePlaylist(Playlist playlist) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Playlist deserializePlaylist(String jsonString) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Playlist newInstance() {
		return new LocalPlaylist();
	}

}