package tw.tcnrcloud110.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;


public class Q0304 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.q0304);
        setupViewComponent();
    }
    private void setupViewComponent(){
        Intent intent=this.getIntent();
        String mode_title = intent.getStringExtra("class_title");
        this.setTitle(mode_title);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int buttonwidth = displayMetrics.widthPixels /2;
        int buttondisth = displayMetrics.heightPixels /20;
        int buttondistw = displayMetrics.widthPixels /5;
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                buttonwidth, RelativeLayout.LayoutParams.WRAP_CONTENT);



        lp.setMargins(buttondistw, 0, 0,buttondisth );

    }
}
