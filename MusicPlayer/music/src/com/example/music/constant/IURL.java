package com.example.music.constant;

public interface IURL {
	String ROOT = "http://192.168.204.1:8080/MusicServer/";
	String MUISICS_URL 					= 	ROOT + "loadMusics.jsp";
	String ACTION_MUSIC_PLAY 			= 	"com.example.music.MUSIC_PLAY";
	String ACTION_MUSIC_PAUSE 			= 	"com.example.music.MUSIC_PAUSE";
	String ACTION_MUSIC_UPDATEPROGRESS 	= 	"com.example.music.UPDATEPROGRESS";
	String ACTION_MUSIC_SEEKBAR_CHANGED = 	"com.example.music.SEEKBAR_CHANGED";
	String ACTION_MUSIC_ONCOMPLATION 	= 	"com.example.music.MUSIC_ONCOMPLATION";
	int PLAY_NEXT_MUSIC 				= 	-1;
	int PLAY_PREVIOUS_MUSIC 			= 	-2;
	int PLAY_CURRENT_MUSIC 				=	 0;
}
