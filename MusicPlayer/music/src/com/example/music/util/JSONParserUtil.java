package com.example.music.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.music.bean.Music;

public class JSONParserUtil {
	public static List<Music> getMusics(JSONObject jsonObj) throws Exception {
		List<Music> musics = new ArrayList<Music>();
		String result = jsonObj.getString("result");
		if ("ok".equals(result)) {
			JSONArray jsonArray = jsonObj.getJSONArray("data");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonMusic = jsonArray.getJSONObject(i);
				Music music = new Music((String) jsonMusic.get("album"), (String) jsonMusic.getString("albumpic"),
						(String) jsonMusic.getString("author"), (String) jsonMusic.getString("composer"),
						(String) jsonMusic.getString("downcount"), (String) jsonMusic.getString("durationtime"),
						(String) jsonMusic.getString("favcount"), (String) jsonMusic.getString("id"),
						(String) jsonMusic.getString("musicpath"), (String) jsonMusic.getString("name"),
						(String) jsonMusic.getString("singer"));
				musics.add(music);
			}
		}
		return musics;
	}
}
