package dn.boxman.activity;


import dn.boxman.R;
import dn.boxman.receiver.BatteryReceiver;
import dn.boxman.service.MusicService;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
//��Ϸ���
public class GameActivity extends Activity{
	//�����㲥
	private BatteryReceiver receiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);
	}
	
	//�˵�
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater=new MenuInflater(this);
		inflater.inflate(R.menu.activity_main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	//�˵��¼�
	public boolean onOptionsItemSelected(MenuItem item){
		Intent intent=null;
		switch (item.getItemId()) {
			//����ҳ��
			case R.id.menu_help:
				intent=new Intent(this,HelpActivity.class);
				startActivity(intent);
				break;
			//��������
			case R.id.menu_music:
				intent=new Intent(this,MusicActivity.class);
				startActivity(intent);
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	//���ʼ
	@Override
	protected void onStart() {
		super.onStart();
		//�������ַ���
		Intent intent=new Intent(this, MusicService.class);
		startService(intent);
		//ע������㲥����
		receiver=new BatteryReceiver();
		//����������
		IntentFilter filter=new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		//ע��
		registerReceiver(receiver, filter);
	}
	
	//������̨
	@Override
	protected void onPause() {
		super.onPause();
		Intent intent=new Intent(this,MusicService.class);
		stopService(intent);
		//ע�������㲥
		unregisterReceiver(receiver);
	}
	
}
