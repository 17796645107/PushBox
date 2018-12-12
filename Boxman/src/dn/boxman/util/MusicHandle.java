package dn.boxman.util;


import dn.boxman.activity.MusicActivity;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.os.Environment;
import static dn.boxman.util.MusicContextUtil.*;

//���ֿ�����
public class MusicHandle {
	//���ֲ�����
	private static MediaPlayer player;
	//��ǰ���ֲ���λ��
	private static int position=0;
	//�����ļ���Դ,SD��
	private static String path=Environment.getExternalStorageDirectory().getPath()+MUSIC_PATH+MUSIC_NAME;
	
	//�����û���ѡ�񲥷�����
	public static void play(Context context){
		if (MusicActivity.isMusicChecked(context)) {
			play();
		}
	}
	
	//�������� (���ʿ������η�һ��Ҫ���ó�private��,����MusicService���������play����)
	private static void play(){
		//����MediaPlayer����
		player=new MediaPlayer();
		try {
			player.setDataSource(path);
			player.setLooping(true);//ѭ������
			//����״̬�ж�
			//����ͣ��ʼ����
			if(position!=0){
				player.prepare();
				player.seekTo(position);
				//����������ͣ-��������ʱ����
				player.setOnSeekCompleteListener(new OnSeekCompleteListener() {
					@Override
					public void onSeekComplete(MediaPlayer mp) {
						player.start();
					}
				});
			}
			//���²���
			else{
				player.prepare();
				player.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//ֹͣ����
	public static void stop(){
		if(player!=null && player.isPlaying()){
			player.stop();
			player=null;
			position=0;
		}
	}
	
	//��ͣ����
	public static void pause(){
		if(player.isPlaying()){
			//��ȡ��ǰ���ֲ���λ��
			position=player.getCurrentPosition();
			player.pause();
		}	
	}
}
