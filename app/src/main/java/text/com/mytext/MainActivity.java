package text.com.mytext;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv_button = findViewById(R.id.tv_button);
        TextView tv_button2 = findViewById(R.id.tv_button2);
        tv_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SelectActivity.class);
                intent.putExtra("type", 0);
                startActivity(new Intent(MainActivity.this, SelectActivity.class));
            }
        });
        tv_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SelectActivity.class);
                intent.putExtra("type", 1);
                startActivity(new Intent(intent));
            }
        });
    }
}
