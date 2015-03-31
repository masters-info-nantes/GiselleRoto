package com.example.jeremy.testdrawer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;
import android.util.AttributeSet;

/**
 * Created by X on 14/03/15.
 */
public class DrawZone extends View{

	private static final float TOUCH_TOLERANCE = 4;

	public int width;
	public int height;
	private Bitmap mBitmap;
	private Canvas mCanvas;
	private Path mPath;
	private Paint mBitmapPaint;
	Context context;
	private Paint circlePaint;
	private Path circlePath;
	private Paint mPaint;
	private float mX;
	private float mY;

	public DrawZone(Context c){
		super(c);
		context = c;
		initPainters();
	}
	
	public DrawZone(Context c, AttributeSet attrs){
		super(c,attrs);
		context = c;
		initPainters();
	}
	
	public DrawZone(Context c, AttributeSet attrs, int defStyle){
		super(c,attrs,defStyle);
		context = c;
		initPainters();
	}
	
	private void initPainters() {
		mPath = new Path();
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);
		circlePaint = new Paint();
		circlePath = new Path();
		circlePaint.setAntiAlias(true);
		circlePaint.setColor(Color.BLUE);
		circlePaint.setStyle(Paint.Style.STROKE);
		circlePaint.setStrokeJoin(Paint.Join.MITER);
		circlePaint.setStrokeWidth(4.0f);
		
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setColor(Color.BLACK);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(12);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawBitmap(mBitmap,0,0,mBitmapPaint);
		canvas.drawPath(mPath,mPaint);
		canvas.drawPath(circlePath,circlePaint);
	}
	
	private void touchStart(float x, float y) {
		mPath.reset();
		mPath.moveTo(x, y);
		mX = x;
		mY = y;
	}
	
	private void touchMove(float x, float y) {
		float dx = Math.abs(x - mX);
		float dy = Math.abs(y - mY);
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
			mX = x;
			mY = y;
			
			circlePath.reset();
			circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
		}
	}
	
	private void touchEnd() {
		mPath.lineTo(mX, mY);
		circlePath.reset();
		// commit the path to our offscreen
		mCanvas.drawPath(mPath,  mPaint);
		// kill this so we don't double draw
		mPath.reset();
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		float x = event.getX();
		float y = event.getY();
		
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				touchStart(x, y);
				invalidate();
				break;
			case MotionEvent.ACTION_MOVE:
				touchMove(x, y);
				invalidate();
				break;
			case MotionEvent.ACTION_UP:
				touchEnd();
				invalidate();
				break;
		}
		return true;
	}

	public int getToolColor(){
		return this.mPaint.getColor();
	}
	
	public float getToolWidth(){
		return this.mPaint.getStrokeWidth();
	}
	
	public void setToolColor(int color){
		this.mPaint.setColor(color);
	}
	
	public void setToolWidth(float size){
		this.mPaint.setStrokeWidth(size);
	}
}
