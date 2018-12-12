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

//游戏视图类
public class GameView extends View {
	private int gate=0;//当前关数
	private GameActivity game=null;//定义game
	public int[][]map=null;//当前地图
	private int width=0;//宽
	private int height=0;//高
	private int mapRow=0;//地图行数
	private int mapColumn=0;//地图列数
	private int manX;//人所在行
	private int manY;//人所在列
	private float xoff=10;//左边距
	private float yoff=20;//上边距
	private int tileSize;//图片大小
	private int[][]tem;//原始地图
	private Bitmap pic[]=null;
	//图片
	final int WALL=1;//墙
	final int GOAL=2;//目标
	final int ROAD=3;//路
	final int BOX=4;//箱子
	final int BOXATGOAL=5;//目标箱子
	final int MAN=6;//人
	private Paint paint; //定义画笔
	float currentX;//用户点击的位置
	float currentY;//用户点击的位置
	
	//构造方法,初始化设置
	@SuppressWarnings("deprecation")
	public GameView(Context context,AttributeSet attrs){
		super(context, attrs);
		//实例化game
		game=(GameActivity) context;
		//获取当前游戏屏幕宽高
		WindowManager manager=game.getWindowManager();
		width=manager.getDefaultDisplay().getWidth();
		height=manager.getDefaultDisplay().getHeight();
		this.setFocusable(true);
		//初始化地图和图片
		initMap();
		initPic();	
	}

	//初始化地图
	public void initMap(){
		//根据关数获得当前游戏地图
		map=getMap(gate);
		//获取地图和人物详细信息
		getMapDetail();
		getManPosition();
	}
	
	//获取地图
	public int[][] getMap(int grade){
		return MapList.getMap(grade);
	}
	
	//获取地图详细信息
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
	
	//获取人物位置
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
	
	//初始化图片资源
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
	
	//加载图片
	public void loadPic(int key,Drawable tile) {
		Bitmap bitmap=Bitmap.createBitmap(tileSize,tileSize,Bitmap.Config.ARGB_8888);
		Canvas canvas=new Canvas(bitmap);
		tile.setBounds(0, 0, tileSize, tileSize);
		tile.draw(canvas);
		pic[key]=bitmap;
	}
	
	//用户交互处理，移动箱子
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//获取用户点击的位置
		currentX = event.getX();
		currentY = event.getY();
		//恢复人所处的具体坐标
		float x = xoff + manY * tileSize;
		float y = yoff + manX * tileSize;
		/*
		 * 判断点击的位置在人的那个方向，
		 * 同时让人往点击的位置移动
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
		//判断本关是否结束
		if(gameFinished()){
			nextGate();
		}
		//刷新屏幕
		this.invalidate();
		
		return super.onTouchEvent(event);
	}

	//更换关卡
	private void nextGate() {
		if (gate < MapList.getCount() - 1) {
			gate++;
		}
		else{
			Toast.makeText(this.getContext(), "最后一关了", Toast.LENGTH_SHORT).show();
		}
		
		reinitMap();
	}

	private void reinitMap() {
		initMap();
		initPic();
	}

	//如果在地图上找不到空的目标区域或者可移动的箱子，则游戏结束
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

	//人物向右移动→
	private void moveRight() {
		//如果人前面是箱子或者是目标区域的箱子则进行下一步
		if (map[manX][manY + 1] == BOX || map[manX][manY + 1] == BOXATGOAL) {
			//看箱子或目标区域的箱子前面是不是目标或路，是，则移动
			if (map[manX][manY + 2] == GOAL || map[manX][manY + 2] == ROAD) {
				/*
				 * 三目运算符
				 * 如果箱子前是路，则用箱子代替
				 * 如果是目标，则用目标区域的箱子代替
				 */
				map[manX][manY + 2] = map[manX][manY + 2] == GOAL ? BOXATGOAL : BOX;
				//箱子移动后的位置由人来代替
				map[manX][manY + 1] = MAN;
				//人所在的位置是恢复为路还是目标区域
				map[manX][manY] = roadOrGoal(manX,manY); 
				manY++;
			}
		}
		//如果人前面是路或者目标，则直接移动
		else{
			if (map[manX][manY + 1] == ROAD || map[manX][manY + 1] == GOAL) {
				map[manX][manY + 1] = MAN;
				map[manX][manY] = roadOrGoal(manX,manY);
				manY++;
			}
		}
	}
	
	//人物向左移动←
	private void moveLeft() {
		//如果人前面是箱子或者是目标区域的箱子则进行下一步
		if (map[manX][manY - 1] == BOX || map[manX][manY - 1] ==BOXATGOAL) {
			//看箱子或目标区域的箱子前面是不是目标或路，是，则移动
			if (map[manX][manY - 2] == GOAL || map[manX][manY - 2] == ROAD) {
				/*
				 * 三目运算符
				 * 如果箱子前是路，则用箱子代替
				 * 如果是目标，则用目标区域的箱子代替
				 */
				map[manX][manY - 2] = map[manX][manY - 2] == GOAL ? BOXATGOAL : BOX;
				//箱子移动后的位置由人来代替
				map[manX][manY - 1] = MAN;
				//人所在的位置是恢复为路还是目标区域
				map[manX][manY] = roadOrGoal(manX,manY); 
				manY--;
			}
		}
		//如果人前面是路或者目标，则直接移动
		else{
			if (map[manX][manY - 1] == ROAD || map[manX][manY - 1] == GOAL) {
				map[manX][manY - 1] = MAN;
				map[manX][manY] = roadOrGoal(manX,manY);
				manY--;
			}
		}
	}

	//人物向上移动↑
	private void moveUp() {
		//如果人前面是箱子或者是目标区域的箱子则进行下一步
		if (map[manX - 1][manY] == BOX || map[manX - 1][manY] ==BOXATGOAL) {
			//看箱子或目标区域的箱子前面是不是目标或路，是，则移动
			if (map[manX -2 ][manY] == GOAL || map[manX - 2][manY] == ROAD) {
				/*
				 * 三目运算符
				 * 如果箱子前是路，则用箱子代替
				 * 如果是目标，则用目标区域的箱子代替
				 */
				map[manX - 2][manY] = map[manX - 2][manY] == GOAL ? BOXATGOAL : BOX;
				//箱子移动后的位置由人来代替
				map[manX - 1][manY] = MAN;
				//人所在的位置是恢复为路还是目标区域
				map[manX][manY] = roadOrGoal(manX,manY); 
				manX--;
			}
		}
		//如果人前面是路或者目标，则直接移动
		else{
			if (map[manX -1][manY] == ROAD || map[manX - 1][manY] == GOAL) {
				map[manX - 1][manY] = MAN;
				map[manX][manY] = roadOrGoal(manX,manY);
				manX--;
			}
		}
	}

	//人物向下移动↓
	private void moveDown() {
		//如果人前面是箱子或者是目标区域的箱子则进行下一步
				if (map[manX + 1][manY] == BOX || map[manX + 1][manY] ==BOXATGOAL) {
					//看箱子或目标区域的箱子前面是不是目标或路，是，则移动
					if (map[manX + 2 ][manY] == GOAL || map[manX + 2][manY] == ROAD) {
						/*
						 * 三目运算符
						 * 如果箱子前是路，则用箱子代替
						 * 如果是目标，则用目标区域的箱子代替
						 */
						map[manX + 2][manY] = map[manX + 2][manY] == GOAL ? BOXATGOAL : BOX;
						//箱子移动后的位置由人来代替
						map[manX + 1][manY] = MAN;
						//人所在的位置是恢复为路还是目标区域
						map[manX][manY] = roadOrGoal(manX,manY); 
						manX++;
					}
				}
				//如果人前面是路或者目标，则直接移动
				else{
					if (map[manX + 1][manY] == ROAD || map[manX + 1][manY] == GOAL) {
						map[manX + 1][manY] = MAN;
						map[manX][manY] = roadOrGoal(manX,manY);
						manX++;
					}
				}
	}
	
	/*
	 * 人所在的位置原来是路还是目标区域
	 * 使用原始地图tem来判断
	 * 看新地图人所在的位置在原始地图是什么角色
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
