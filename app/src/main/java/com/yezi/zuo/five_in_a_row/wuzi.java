package com.yezi.zuo.five_in_a_row;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zuo on 2016/10/4.
 */
public class wuzi extends View{

    private int mPaneWidth;
    private float mLineHeight;
    private int MAX_LINE = 10;
    private int MAX_COUUNT_IN_LINE =5;

    private Paint mpaint = new Paint();

    private Bitmap whitePiece ;
    private Bitmap blackPiece ;
    public static boolean over = true;

    private float ratioPieceOfLineHeight = 3*1.0f/4;

    public static boolean Iswhite=true;//白棋先手
    public static ArrayList<Point> whiteArray = new ArrayList<>();
    public static ArrayList<Point> blackArray = new ArrayList<>();

    private boolean IsGameOver;
    private boolean IsWhiteWin;//true 白赢，否则黑赢




    public wuzi(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mpaint.setAntiAlias(true);
        mpaint.setDither(true);
        mpaint.setStyle(Paint.Style.STROKE);

        whitePiece = BitmapFactory.decodeResource(getResources(),R.drawable.w);
        blackPiece = BitmapFactory.decodeResource(getResources(),R.drawable.b);
    }

    protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
        int withSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = Math.min(withSize,heightSize);

        if(widthMode == MeasureSpec.UNSPECIFIED){
            width = heightSize;
        }else if(heightMode == MeasureSpec.UNSPECIFIED){
            width = withSize;
        }
        setMeasuredDimension(width,width);


    }

    @Override
    protected void onSizeChanged(int w,int h,int oldw,int oldh){
        super.onSizeChanged(w,h,oldw,oldh);

        mPaneWidth =w;
        mLineHeight = mPaneWidth*1.0f/MAX_LINE;

        int pieceWidth =(int )(mLineHeight*ratioPieceOfLineHeight);

        whitePiece =Bitmap.createScaledBitmap(whitePiece,pieceWidth,pieceWidth,false);
        blackPiece =Bitmap.createScaledBitmap(blackPiece,pieceWidth,pieceWidth,false);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(over) {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_UP) {

                int x = (int) event.getX();
                int y = (int) event.getY();

                Point p = getValidPoint(x, y);

                if (whiteArray.contains(p) || blackArray.contains(p)) {
                    return false;
                }

//            Point p = new Point(x,y);
                if (Iswhite) {
                    whiteArray.add(p);
                } else {
                    blackArray.add(p);
                }

                invalidate();
                Iswhite = !Iswhite;
                return true;

            }
        }
        return true;
    }

    private Point getValidPoint(int x, int y) {

        return new Point((int)(x/mLineHeight),(int)(y/mLineHeight));
    }


    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        MainActivity.tv.setText("");
        drawBoard(canvas);
        drawPiece(canvas);
        checkGameOver(canvas);
    }

    private void checkGameOver(Canvas canvas) {
        boolean whiteWin =checkFiveInLine(whiteArray);
        boolean blackWin =checkFiveInLine(blackArray);

        if(whiteWin||blackWin){
            IsGameOver =true;
            IsWhiteWin = whiteWin;

            String text = IsWhiteWin?"白棋胜利":"黑棋胜利";
            over=false;
//            Toast.makeText(getContext(),text,Toast.LENGTH_LONG).show();
            MainActivity.tv.setText(text);

        }


    }

    private boolean checkFiveInLine(List<Point> points) {

        for(Point p : points){

            int x=p.x;
            int y =p.y;
            boolean win =checkHorizontal(x,y,points);
            if(win){
                return true;
            }
            win = checkleftDiagonal(x,y,points);
            if(win){
                return true;
            }
            win = checkrightDiagonal(x,y,points);
            if(win){
                return true;
            }
            win = checkVertical(x,y,points);
            if(win){
                return true;
            }

        }


        return false;
    }


    /**
     * 判断x,y位置的棋子，是否横向有相邻五个
     * @param x
     * @param y
     * @param points
     * @return
     */
    private boolean checkHorizontal(int x, int y, List<Point> points) {

        int count =1;
        //寻左边4个
        for(int i =1;i<MAX_COUUNT_IN_LINE;i++){
            if(points.contains(new Point(x-i,y))){
                count++;
            }else{
                break;
            }
        }
        if(count==MAX_COUUNT_IN_LINE)
        {
            return true;
        }
        //寻右边4个
        for(int i =1;i<MAX_COUUNT_IN_LINE;i++){
            if(points.contains(new Point(x+i,y))){
                count++;
            }else{
                break;
            }
        }
        if(count==MAX_COUUNT_IN_LINE)
        {
            return true;
        }
        return false;
    }

    /**
     * 判断x,y位置的棋子，是否垂向有相邻五个
     * @param x
     * @param y
     * @param points
     * @return
     */

    private boolean checkVertical(int x, int y, List<Point> points) {

        int count =1;
        //寻下边4个
        for(int i =1;i<MAX_COUUNT_IN_LINE;i++){
            if(points.contains(new Point(x,y+i))){
                count++;
            }else{
                break;
            }
        }
        if(count==MAX_COUUNT_IN_LINE)
        {
            return true;
        }
        for(int i =1;i<MAX_COUUNT_IN_LINE;i++){
            if(points.contains(new Point(x,y-i))){
                count++;
            }else{
                break;
            }
        }
        if(count==MAX_COUUNT_IN_LINE)
        {
            return true;
        }
        return false;
    }

    /**
     * 判断x,y位置的棋子，是否左斜向有相邻五个
     * @param x
     * @param y
     * @param points
     * @return
     */

    private boolean checkleftDiagonal(int x, int y, List<Point> points) {

        int count =1;

        for(int i =1;i<MAX_COUUNT_IN_LINE;i++){
            if(points.contains(new Point(x-i,y+i))){
                count++;
            }else{
                break;
            }
        }
        if(count==MAX_COUUNT_IN_LINE)
        {
            return true;
        }
        for(int i =1;i<MAX_COUUNT_IN_LINE;i++){
            if(points.contains(new Point(x+i,y-i))){
                count++;
            }else{
                break;
            }
        }
        if(count==MAX_COUUNT_IN_LINE)
        {
            return true;
        }
        return false;
    }

    /**
     * 判断x,y位置的棋子，是否右斜向有相邻五个
     * @param x
     * @param y
     * @param points
     * @return
     */
    private boolean checkrightDiagonal(int x, int y, List<Point> points) {

        int count =1;

        for(int i =1;i<MAX_COUUNT_IN_LINE;i++){
            if(points.contains(new Point(x-i,y-i))){
                count++;
            }else{
                break;
            }
        }
        if(count==MAX_COUUNT_IN_LINE)
        {
            return true;
        }
        for(int i =1;i<MAX_COUUNT_IN_LINE;i++){
            if(points.contains(new Point(x+i,y+i))){
                count++;
            }else{
                break;
            }
        }
        if(count==MAX_COUUNT_IN_LINE)
        {
            return true;
        }
        return false;
    }

    private void drawPiece(Canvas canvas) {
        for(int i =0;i<whiteArray.size();i++){
            Point whitePoint =whiteArray.get(i);
            canvas.drawBitmap(whitePiece,(whitePoint.x+
                    (1-ratioPieceOfLineHeight)/2)*mLineHeight,
                    (whitePoint.y+(1-ratioPieceOfLineHeight)/2)*mLineHeight,null);
        }

        for(int i =0;i<blackArray.size();i++){
            Point blackPoint =blackArray.get(i);

            canvas.drawBitmap(blackPiece,(blackPoint.x+
                            (1-ratioPieceOfLineHeight)/2)*mLineHeight,
                    (blackPoint.y+(1-ratioPieceOfLineHeight)/2)*mLineHeight,null);
        }
    }

    private void drawBoard(Canvas canvas) {

        int w =mPaneWidth;
        float lineHeight = mLineHeight;

        for(int i = 0;i<MAX_LINE;i++){
            int statrx=(int)(lineHeight/2);
            int endx = (int)(w-lineHeight/2);

            int y = (int)((0.5+i)*lineHeight);
            canvas.drawLine(statrx,y,endx,y,mpaint);
            canvas.drawLine(y,statrx,y,endx,mpaint);
        }
    }

    private static final String CUN = "instance";
    private static final String CUN_GAMEOVER = "instance";

    private static final String CUN_WHITEARRAY = "instance_white";

    private static final String CUN_BLACKARRAY = "instance_black";
    @Override
    protected Parcelable onSaveInstanceState(){
        Bundle bundle =new Bundle();
        bundle.putParcelable(CUN,super.onSaveInstanceState());
        bundle.putBoolean(CUN_GAMEOVER,IsGameOver);
        bundle.putParcelableArrayList(CUN_BLACKARRAY,blackArray);
        bundle.putParcelableArrayList(CUN_WHITEARRAY,whiteArray);
        return bundle;
    }
    public void reStart(){
        whiteArray.clear();
        blackArray.clear();
        IsWhiteWin=false;
        Iswhite=true;
        invalidate();
        over=true;
    }

    public void quit_step(){
        if(Iswhite&&!blackArray.isEmpty()){
            blackArray.remove(blackArray.size()-1);
            Iswhite=false;
            invalidate();
        }else if(!whiteArray.isEmpty()){
            whiteArray.remove(whiteArray.size()-1);
            Iswhite=true;
            invalidate();
        }else{
            Toast.makeText(getContext(),"没有棋子可退",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state){
        if(state instanceof Bundle){
            Bundle bundle = (Bundle)state;
            IsGameOver =bundle.getBoolean(CUN_GAMEOVER);
            whiteArray = bundle.getParcelableArrayList(CUN_WHITEARRAY);
            blackArray=bundle.getParcelableArrayList(CUN_BLACKARRAY);
            super.onRestoreInstanceState(bundle.getParcelable(CUN));
            return;
        }
        super.onRestoreInstanceState(state );
    }



}
