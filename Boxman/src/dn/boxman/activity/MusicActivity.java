package dn.boxman.activity;


import dn.boxman.R;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class MusicActivity extends PreferenceActivity{

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.music_setting);
	}
	
	public static boolean isMusicChecked(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("music", true);
	}
}
