package dn.boxman.service;

import dn.boxman.util.MusicHandle;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
//音乐服务类
public class MusicService extends Service {

	//必须重构的方法
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	//启动服务
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		MusicHandle.play(this);
		return super.onStartCommand(intent, flags, startId);
	}
	
	//销毁服务
	@Override
	public void onDestroy() {
		super.onDestroy();
		MusicHandle.stop();
	}
}
