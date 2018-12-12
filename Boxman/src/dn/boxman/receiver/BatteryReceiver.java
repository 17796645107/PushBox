package dn.boxman.receiver;


import dn.boxman.R;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

//�����㲥����
public class BatteryReceiver extends BroadcastReceiver {

	private TextView tv_battery=null;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if(Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())){
			//��ǰ����
			int level=intent.getIntExtra("level", 0);
			//�ܵ���
			int total=intent.getIntExtra("scale", 100);
			StringBuffer sb=new StringBuffer("��ǰ����:");
			sb.append((level*100)/total);
			sb.append("%");
			//���
			String flag=intent.getStringExtra("flag");
			if("main".equals(flag)){
				tv_battery.findViewById(R.id.tx_battery);
				
				tv_battery.setText(sb.toString());
			}
			if("game".equals(flag)){
				Toast.makeText(context, sb, Toast.LENGTH_SHORT).show();
			}
			Toast.makeText(context, sb, Toast.LENGTH_SHORT).show();
		};
	}

}
