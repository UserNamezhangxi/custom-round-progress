package huawei.com.yuanview;


import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends Activity {

    private int[] color={Color.parseColor("#cccccc"),Color.parseColor("#cccccc"),Color.parseColor("#417EFC")};  // 设置动画的颜色
    private float[] degree = {0,0.75F,1.0F}; //动画颜色渲染的角度
    private RoundProgressView round;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        round = (RoundProgressView) findViewById(R.id.roundCircle);
        round.setRoundColor(color,degree);
        round.updataDegree(0);

        Button btnStart = (Button) findViewById(R.id.btn_start);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                round.setRoundColor(color,degree);
                round.startAnimation();
            }
        });

        Button btnStop = (Button) findViewById(R.id.btn_stop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                round.stopAnimation();
            }
        });

        final EditText etProg = (EditText) findViewById(R.id.et_prog);

        Button btnProg = (Button) findViewById(R.id.btn_prog);
        btnProg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                round.updataDegree(Float.valueOf(etProg.getText().toString().trim()));
                round.startAnimFromDegree(0,5);
            }
        });

    }
}
