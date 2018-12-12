package dn.boxman.util;


import dn.boxman.activity.MusicActivity;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.os.Environment;
import static dn.boxman.util.MusicContextUtil.*;

//音乐控制类
public class MusicHandle {
	//音乐播放器
	private static MediaPlayer player;
	//当前音乐播放位置
	private static int position=0;
	//音乐文件资源,SD卡
	private static String path=Environment.getExternalStorageDirectory().getPath()+MUSIC_PATH+MUSIC_NAME;
	
	//根据用户的选择播放音乐
	public static void play(Context context){
		if (MusicActivity.isMusicChecked(context)) {
			play();
		}
	}
	
	//播放音乐 (访问控制修饰符一定要设置成private！,否则MusicService会调用两次play方法)
	private static void play(){
		//创建MediaPlayer对象
		player=new MediaPlayer();
		try {
			player.setDataSource(path);
			player.setLooping(true);//循环播放
			//播放状态判断
			//从暂停开始播放
			if(position!=0){
				player.prepare();
				player.seekTo(position);
				//监听器，暂停-继续音乐时触发
				player.setOnSeekCompleteListener(new OnSeekCompleteListener() {
					@Override
					public void onSeekComplete(MediaPlayer mp) {
						player.start();
					}
				});
			}
			//从新播放
			else{
				player.prepare();
				player.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//停止音乐
	public static void stop(){
		if(player!=null && player.isPlaying()){
			player.stop();
			player=null;
			position=0;
		}
	}
	
	//暂停音乐
	public static void pause(){
		if(player.isPlaying()){
			//获取当前音乐播放位置
			position=player.getCurrentPosition();
			player.pause();
		}	
	}
}
