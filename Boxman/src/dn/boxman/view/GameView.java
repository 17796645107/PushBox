package dn.boxman.view;

import dn.boxman.R;
import dn.boxman.activity.GameActivity;
import dn.boxman.util.MapList;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

//��Ϸ��ͼ��
public class GameView extends View {
	private int gate=0;//��ǰ����
	private GameActivity game=null;//����game
	public int[][]map=null;//��ǰ��ͼ
	private int width=0;//��
	private int height=0;//��
	private int mapRow=0;//��ͼ����
	private int mapColumn=0;//��ͼ����
	private int manX;//��������
	private int manY;//��������
	private float xoff=10;//��߾�
	private float yoff=20;//�ϱ߾�
	private int tileSize;//ͼƬ��С
	private int[][]tem;//ԭʼ��ͼ
	private Bitmap pic[]=null;
	//ͼƬ
	final int WALL=1;//ǽ
	final int GOAL=2;//Ŀ��
	final int ROAD=3;//·
	final int BOX=4;//����
	final int BOXATGOAL=5;//Ŀ������
	final int MAN=6;//��
	private Paint paint; //���廭��
	float currentX;//�û������λ��
	float currentY;//�û������λ��
	
	//���췽��,��ʼ������
	@SuppressWarnings("deprecation")
	public GameView(Context context,AttributeSet attrs){
		super(context, attrs);
		//ʵ����game
		game=(GameActivity) context;
		//��ȡ��ǰ��Ϸ��Ļ���
		WindowManager manager=game.getWindowManager();
		width=manager.getDefaultDisplay().getWidth();
		height=manager.getDefaultDisplay().getHeight();
		this.setFocusable(true);
		//��ʼ����ͼ��ͼƬ
		initMap();
		initPic();	
	}

	//��ʼ����ͼ
	public void initMap(){
		//���ݹ�����õ�ǰ��Ϸ��ͼ
		map=getMap(gate);
		//��ȡ��ͼ��������ϸ��Ϣ
		getMapDetail();
		getManPosition();
	}
	
	//��ȡ��ͼ
	public int[][] getMap(int grade){
		return MapList.getMap(grade);
	}
	
	//��ȡ��ͼ��ϸ��Ϣ
	private void getMapDetail(){
		mapRow=map.length;
		mapColumn=map[gate].length;
		xoff=30;
		yoff=60;
		int t=mapRow>mapColumn?mapRow:mapColumn;
		int s1=(int) Math.floor((width-2*xoff)/t);
		int s2=(int) Math.floor((height-yoff)/t);
		tileSize=s1<s2?s1:s2;
		this.tem=MapList.getMap(gate);
	}
	
	//��ȡ����λ��
	public void getManPosition(){
		for(int i=0;i<map.length;i++){
			for(int j=0;j<map[0].length;j++){
				if(map[i][j]==MAN){
					manX=i;
					manY=j;
					break;
				}
			}
		}
	}
	
	//��ʼ��ͼƬ��Դ
	private void initPic() {
		pic=new Bitmap[7];
		Resources r=this.getContext().getResources();
		loadPic(WALL,r.getDrawable(R.drawable.qiang));
		loadPic(GOAL,r.getDrawable(R.drawable.goal));
		loadPic(ROAD, r.getDrawable(R.drawable.lu));
		loadPic(BOX, r.getDrawable(R.drawable.xiangzi));
		loadPic(BOXATGOAL, r.getDrawable(R.drawable.boxgoal));
		loadPic(MAN, r.getDrawable(R.drawable.ren));
	}
	
	//����ͼƬ
	public void loadPic(int key,Drawable tile) {
		Bitmap bitmap=Bitmap.createBitmap(tileSize,tileSize,Bitmap.Config.ARGB_8888);
		Canvas canvas=new Canvas(bitmap);
		tile.setBounds(0, 0, tileSize, tileSize);
		tile.draw(canvas);
		pic[key]=bitmap;
	}
	
	//�û����������ƶ�����
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//��ȡ�û������λ��
		currentX = event.getX();
		currentY = event.getY();
		//�ָ��������ľ�������
		float x = xoff + manY * tileSize;
		float y = yoff + manX * tileSize;
		/*
		 * �жϵ����λ�����˵��Ǹ�����
		 * ͬʱ�����������λ���ƶ�
		 */
		if (currentY > y && (currentY < y + tileSize)) {
			if (currentX > x + tileSize) {
				moveRight();
			}
			if (currentX < x) {
				moveLeft();
			}
		}
		if (currentX > x && (currentX < x + tileSize)) {
			if (currentY > y + tileSize) {
				moveDown();
			}
			if (currentY < y) {
				moveUp();
			}
		}
		//�жϱ����Ƿ����
		if(gameFinished()){
			nextGate();
		}
		//ˢ����Ļ
		this.invalidate();
		
		return super.onTouchEvent(event);
	}

	//�����ؿ�
	private void nextGate() {
		if (gate < MapList.getCount() - 1) {
			gate++;
		}
		else{
			Toast.makeText(this.getContext(), "���һ����", Toast.LENGTH_SHORT).show();
		}
		
		reinitMap();
	}

	private void reinitMap() {
		initMap();
		initPic();
	}

	//����ڵ�ͼ���Ҳ����յ�Ŀ��������߿��ƶ������ӣ�����Ϸ����
	private boolean gameFinished() {
		boolean finish = true;
		for(int i = 0; i < mapRow; i++){
			for(int j = 0; j < mapColumn; j++){
				if(map[i][j] == GOAL || map[i][j] == BOX){
					finish = false;
				}
			}
		}
		return finish;
	}

	//���������ƶ���
	private void moveRight() {
		//�����ǰ�������ӻ�����Ŀ������������������һ��
		if (map[manX][manY + 1] == BOX || map[manX][manY + 1] == BOXATGOAL) {
			//�����ӻ�Ŀ�����������ǰ���ǲ���Ŀ���·���ǣ����ƶ�
			if (map[manX][manY + 2] == GOAL || map[manX][manY + 2] == ROAD) {
				/*
				 * ��Ŀ�����
				 * �������ǰ��·���������Ӵ���
				 * �����Ŀ�꣬����Ŀ����������Ӵ���
				 */
				map[manX][manY + 2] = map[manX][manY + 2] == GOAL ? BOXATGOAL : BOX;
				//�����ƶ����λ������������
				map[manX][manY + 1] = MAN;
				//�����ڵ�λ���ǻָ�Ϊ·����Ŀ������
				map[manX][manY] = roadOrGoal(manX,manY); 
				manY++;
			}
		}
		//�����ǰ����·����Ŀ�꣬��ֱ���ƶ�
		else{
			if (map[manX][manY + 1] == ROAD || map[manX][manY + 1] == GOAL) {
				map[manX][manY + 1] = MAN;
				map[manX][manY] = roadOrGoal(manX,manY);
				manY++;
			}
		}
	}
	
	//���������ƶ���
	private void moveLeft() {
		//�����ǰ�������ӻ�����Ŀ������������������һ��
		if (map[manX][manY - 1] == BOX || map[manX][manY - 1] ==BOXATGOAL) {
			//�����ӻ�Ŀ�����������ǰ���ǲ���Ŀ���·���ǣ����ƶ�
			if (map[manX][manY - 2] == GOAL || map[manX][manY - 2] == ROAD) {
				/*
				 * ��Ŀ�����
				 * �������ǰ��·���������Ӵ���
				 * �����Ŀ�꣬����Ŀ����������Ӵ���
				 */
				map[manX][manY - 2] = map[manX][manY - 2] == GOAL ? BOXATGOAL : BOX;
				//�����ƶ����λ������������
				map[manX][manY - 1] = MAN;
				//�����ڵ�λ���ǻָ�Ϊ·����Ŀ������
				map[manX][manY] = roadOrGoal(manX,manY); 
				manY--;
			}
		}
		//�����ǰ����·����Ŀ�꣬��ֱ���ƶ�
		else{
			if (map[manX][manY - 1] == ROAD || map[manX][manY - 1] == GOAL) {
				map[manX][manY - 1] = MAN;
				map[manX][manY] = roadOrGoal(manX,manY);
				manY--;
			}
		}
	}

	//���������ƶ���
	private void moveUp() {
		//�����ǰ�������ӻ�����Ŀ������������������һ��
		if (map[manX - 1][manY] == BOX || map[manX - 1][manY] ==BOXATGOAL) {
			//�����ӻ�Ŀ�����������ǰ���ǲ���Ŀ���·���ǣ����ƶ�
			if (map[manX -2 ][manY] == GOAL || map[manX - 2][manY] == ROAD) {
				/*
				 * ��Ŀ�����
				 * �������ǰ��·���������Ӵ���
				 * �����Ŀ�꣬����Ŀ����������Ӵ���
				 */
				map[manX - 2][manY] = map[manX - 2][manY] == GOAL ? BOXATGOAL : BOX;
				//�����ƶ����λ������������
				map[manX - 1][manY] = MAN;
				//�����ڵ�λ���ǻָ�Ϊ·����Ŀ������
				map[manX][manY] = roadOrGoal(manX,manY); 
				manX--;
			}
		}
		//�����ǰ����·����Ŀ�꣬��ֱ���ƶ�
		else{
			if (map[manX -1][manY] == ROAD || map[manX - 1][manY] == GOAL) {
				map[manX - 1][manY] = MAN;
				map[manX][manY] = roadOrGoal(manX,manY);
				manX--;
			}
		}
	}

	//���������ƶ���
	private void moveDown() {
		//�����ǰ�������ӻ�����Ŀ������������������һ��
				if (map[manX + 1][manY] == BOX || map[manX + 1][manY] ==BOXATGOAL) {
					//�����ӻ�Ŀ�����������ǰ���ǲ���Ŀ���·���ǣ����ƶ�
					if (map[manX + 2 ][manY] == GOAL || map[manX + 2][manY] == ROAD) {
						/*
						 * ��Ŀ�����
						 * �������ǰ��·���������Ӵ���
						 * �����Ŀ�꣬����Ŀ����������Ӵ���
						 */
						map[manX + 2][manY] = map[manX + 2][manY] == GOAL ? BOXATGOAL : BOX;
						//�����ƶ����λ������������
						map[manX + 1][manY] = MAN;
						//�����ڵ�λ���ǻָ�Ϊ·����Ŀ������
						map[manX][manY] = roadOrGoal(manX,manY); 
						manX++;
					}
				}
				//�����ǰ����·����Ŀ�꣬��ֱ���ƶ�
				else{
					if (map[manX + 1][manY] == ROAD || map[manX + 1][manY] == GOAL) {
						map[manX + 1][manY] = MAN;
						map[manX][manY] = roadOrGoal(manX,manY);
						manX++;
					}
				}
	}
	
	/*
	 * �����ڵ�λ��ԭ����·����Ŀ������
	 * ʹ��ԭʼ��ͼtem���ж�
	 * ���µ�ͼ�����ڵ�λ����ԭʼ��ͼ��ʲô��ɫ
	 */
	private int roadOrGoal(int x, int y) {
		int result = ROAD;
		if (tem[x][y] == GOAL) {
			result = GOAL;
		}
		
		return result;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for(int i=0;i<mapRow;i++){
			for(int j=0;j<mapColumn;j++){
				if(map[i][j]!=0){
					canvas.drawBitmap(pic[map[i][j]], xoff+j*tileSize, yoff+i*tileSize,paint);
				}
			}
		}
	}
}
