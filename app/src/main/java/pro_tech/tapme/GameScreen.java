package pro_tech.tapme;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameScreen extends AppCompatActivity {

    TextView scoreTextView;
    LinearLayout layout;
    int score, total, missed;
    int period, delay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameview);
        getSupportActionBar().hide();

        layout = (LinearLayout) findViewById(R.id.layout);
        layout.addView(new MyView(this));
        scoreTextView = (TextView) findViewById(R.id.textView);

    }

    public class MyView extends View{

        int positionX;
        int positionY;
        int radius;
        private Timer timer;
        private TimerTask timerTask;
        private Handler handler = new Handler();


        public MyView(Context context){
            super(context);
            score = 0; total=0; missed = 0; period = 2000; delay = 800;
            startTimer();

        }

        @Override
        protected void onDraw(final Canvas canvas){
            super.onDraw(canvas);

            Paint paint = new Paint();

            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.BLACK);
            canvas.drawPaint(paint);

            updatePosition();
            paint.setColor(getRandomColor());
            canvas.drawCircle(positionX ,positionY, radius ,paint); //radius is 100

            View.OnTouchListener touch = new View.OnTouchListener(){

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int x = (int) event.getX();
                    int y = (int) event.getY();

                    if(x<positionX && (positionX-x)<radius ){
                        if(y<positionY && (positionY-y)<radius){
                            timerTask.cancel();
                            score++;
                            redraw();
                            startTimer();
                        }
                        else if (y>positionY && (y-positionY)<radius){
                            timerTask.cancel();
                            score++;
                            redraw();
                            startTimer();
                        }
                    }
                    else if(x>positionX && (x-positionX)<radius){
                        if(y<positionY && (positionY-y)<radius){
                            timerTask.cancel();
                            score++;
                            redraw();
                            startTimer();
                        }
                        else if (y>positionY && (y-positionY)<radius){
                            timerTask.cancel();
                            score++;
                            redraw();
                            startTimer();
                        }
                    }
                    return true;
                }
            };
            this.setOnTouchListener(touch);
        }

        public void redraw(){
            this.invalidate();
            total++;
            missed = total-score;
            scoreTextView.setText("Total :"+total+"        Score :"+score+"        Missed :"+missed );
            if(missed>=4){
                timerTask.cancel();
                layout.removeAllViews();
                layout.addView(new GameOver(getContext()));
            }
        }
        private void updatePosition(){
            int x = getWidth();
            int y = getHeight();
            int maxWidth = getWidth()/10;
            int maxHeight = getHeight()/10;

            Random r = new Random(); //positionX = r.nextInt(max - min + 1) + min;
            positionX = r.nextInt(x-maxWidth)+(maxWidth/2);
            positionY = r.nextInt(y-maxHeight)+(maxHeight/2);

            radius = getWidth()/20;
        }
        public int getRandomColor(){
            Random rnd = new Random();
            return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        }

        private void startTimer(){
            timer = new Timer();
            timerTask = new TimerTask() {
                public void run() {
                    handler.post(new Runnable() {
                        public void run(){
                            redraw();
                        }
                    });
                }
            };
            timer.schedule(timerTask, period, delay );
        }
    }

    public class GameOver extends View{

        public GameOver(Context context){
            super(context);

        }

        @Override
        protected void onDraw(final Canvas canvas) {
            super.onDraw(canvas);

            Paint paint = new Paint();

            final String gover = "Game Over";
            final String restart = "Restart";

            paint.setColor(Color.WHITE);
            paint.setTextSize(getWidth()/7);
            final double aaaa = ((getWidth()/7)*((gover.length()/2)+1))/2;
            canvas.drawText("Game Over", (getWidth()/2)-(int)aaaa , getHeight()/2, paint);

            paint.setColor(Color.WHITE);
            paint.setTextSize(getWidth()/12);
            final double asas = ((getWidth()/12)*(restart.length()/2))/2;
            canvas.drawText("Restart", (getWidth()/2)-(int)asas , (getHeight()/2)+300, paint);

            View.OnTouchListener touch = new View.OnTouchListener() {
                int counter = 0;
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int x = (int) event.getX();
                    int y = (int) event.getY();

                    if( x >= (getWidth()/2)-(int)asas && x <= ((getWidth()/2)-(int)asas)+(getWidth()/12)*(restart.length()/2))
                    {
                        if( y <= (getHeight()/2)+300 && y >= (getHeight()/2)+300 - getWidth()/12 ){
                            layout.removeAllViews();
                            score = 0; total = 0; missed = 0;
                            scoreTextView.setText("Total :"+total+"        Score :"+score+"        Missed :"+missed );
                            layout.addView(new MyView(getContext()));
                        }
                    }
                    return true;
                }
            };
            this.setOnTouchListener(touch);
        }
    }
}
