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
//游戏活动类
public class GameActivity extends Activity{
	//电量广播
	private BatteryReceiver receiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);
	}
	
	//菜单
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater=new MenuInflater(this);
		inflater.inflate(R.menu.activity_main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	//菜单事件
	public boolean onOptionsItemSelected(MenuItem item){
		Intent intent=null;
		switch (item.getItemId()) {
			//帮助页面
			case R.id.menu_help:
				intent=new Intent(this,HelpActivity.class);
				startActivity(intent);
				break;
			//音乐设置
			case R.id.menu_music:
				intent=new Intent(this,MusicActivity.class);
				startActivity(intent);
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	//活动开始
	@Override
	protected void onStart() {
		super.onStart();
		//启动音乐服务
		Intent intent=new Intent(this, MusicService.class);
		startService(intent);
		//注册电量广播接收
		receiver=new BatteryReceiver();
		//创建过滤器
		IntentFilter filter=new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		//注册
		registerReceiver(receiver, filter);
	}
	
	//活动进入后台
	@Override
	protected void onPause() {
		super.onPause();
		Intent intent=new Intent(this,MusicService.class);
		stopService(intent);
		//注销电量广播
		unregisterReceiver(receiver);
	}
	
}
