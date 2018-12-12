package dn.boxman;

import dn.boxman.activity.AboutActivity;
import dn.boxman.activity.GameActivity;
import dn.boxman.activity.HelpActivity;
import dn.boxman.activity.MusicActivity;
import dn.boxman.service.MusicService;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	//创建菜单
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater=new MenuInflater(this);
		inflater.inflate(R.menu.activity_main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	//菜单按钮事件
	public boolean onOptionsItemSelected(MenuItem item){
		Intent intent=null;
		switch (item.getItemId()) {
			//音乐设置
			case R.id.menu_help:
				intent=new Intent(MainActivity.this,HelpActivity.class);
				startActivity(intent);
				break;
			//帮助界面
			case R.id.menu_music:
				intent=new Intent(MainActivity.this,MusicActivity.class);
				startActivity(intent);
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	//界面按钮事件
	public void onClick(View view){
		Intent intent=null;
		switch (view.getId()) {
			//新游戏
			case R.id.btn_newGame:
				intent=new Intent(this,GameActivity.class);
				startActivity(intent);
				break;
			//关于游戏
			case R.id.btn_about:
				intent=new Intent(this,AboutActivity.class);
				startActivity(intent);
			//退出游戏
			case R.id.btn_exit:
				finish();
				break;
		}
	}
	
	//活动后台运行
	@Override
	protected void onPause(){
		super.onPause();
		//停止音乐服务
		Intent intent=new Intent(this,MusicService.class);
		stopService(intent);
	}
	
	//活动准备
	@Override
	protected void onResume() {
		super.onResume();
		//启动音乐服务
		Intent intent=new Intent(this, MusicService.class);
		startService(intent);
	}

	//活动销毁
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d("dn", "MainActivity---onDestory()");
		//停止音乐服务
		Intent intent=new Intent(this,MusicService.class);
		stopService(intent);
		
		
	}
	
}
