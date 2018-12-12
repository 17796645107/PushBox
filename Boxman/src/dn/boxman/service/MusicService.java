package dn.boxman.service;

import dn.boxman.util.MusicHandle;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
//���ַ�����
public class MusicService extends Service {

	//�����ع��ķ���
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	//��������
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		MusicHandle.play(this);
		return super.onStartCommand(intent, flags, startId);
	}
	
	//���ٷ���
	@Override
	public void onDestroy() {
		super.onDestroy();
		MusicHandle.stop();
	}
}
