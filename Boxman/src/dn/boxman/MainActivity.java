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
	
	//�����˵�
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater=new MenuInflater(this);
		inflater.inflate(R.menu.activity_main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	//�˵���ť�¼�
	public boolean onOptionsItemSelected(MenuItem item){
		Intent intent=null;
		switch (item.getItemId()) {
			//��������
			case R.id.menu_help:
				intent=new Intent(MainActivity.this,HelpActivity.class);
				startActivity(intent);
				break;
			//��������
			case R.id.menu_music:
				intent=new Intent(MainActivity.this,MusicActivity.class);
				startActivity(intent);
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	//���水ť�¼�
	public void onClick(View view){
		Intent intent=null;
		switch (view.getId()) {
			//����Ϸ
			case R.id.btn_newGame:
				intent=new Intent(this,GameActivity.class);
				startActivity(intent);
				break;
			//������Ϸ
			case R.id.btn_about:
				intent=new Intent(this,AboutActivity.class);
				startActivity(intent);
			//�˳���Ϸ
			case R.id.btn_exit:
				finish();
				break;
		}
	}
	
	//���̨����
	@Override
	protected void onPause(){
		super.onPause();
		//ֹͣ���ַ���
		Intent intent=new Intent(this,MusicService.class);
		stopService(intent);
	}
	
	//�׼��
	@Override
	protected void onResume() {
		super.onResume();
		//�������ַ���
		Intent intent=new Intent(this, MusicService.class);
		startService(intent);
	}

	//�����
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d("dn", "MainActivity---onDestory()");
		//ֹͣ���ַ���
		Intent intent=new Intent(this,MusicService.class);
		stopService(intent);
		
		
	}
	
}
