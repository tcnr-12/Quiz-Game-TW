package tw.tcnrcloud110.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class Q0311 extends AppCompatActivity{
    private TextView t001;
//    private Button b001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.q0311);
        setupViewComponent();
    }

    private void setupViewComponent() {
        Intent intent = this.getIntent();
        String mode_title = intent.getStringExtra("class_title");
        this.setTitle(mode_title);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        t001 = (TextView) findViewById(R.id.q0311_t001);
        int ruletexth = displayMetrics.heightPixels * 7 / 10;
        t001.getLayoutParams().height = ruletexth;
//        b001 = (Button) findViewById(R.id.q0311_b001);
//        b001.setOnClickListener(this);
    }

//    @Override
//    public void onClick(View v) {
//        this.finish();
//    }
    @Override
    public void onBackPressed() {
    //        super.onBackPressed();
    }
    //===========menu==============
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.q0300, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent01;
        switch (item.getItemId()) {
            case R.id.q0300_m_back:
//                Toast.makeText(getApplicationContext(), "施工中", Toast.LENGTH_SHORT).show();

//                intent01=new Intent(M1405query.this,M1405.class);
//                //intent01.putExtra("title", getString(R.string.m_add));
//                startActivity(intent01);
                this.finish();
                break;


        }


        return super.onOptionsItemSelected(item);
    }
}