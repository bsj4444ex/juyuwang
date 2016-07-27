package com.test.bsj444.juyuwang;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/5/31.
 */
public class BeginActivity extends Activity {

    Button in;
    EditText Username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.denglu);
        in = (Button) BeginActivity.this.findViewById(R.id.in);
        Username=(EditText)BeginActivity.this.findViewById(R.id.username);
        in.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(BeginActivity.this,MainActivity.class);
                        String username=Username.getText().toString();
                        if (username.equals("")){
                            Toast.makeText(BeginActivity.this,"请输入名字",Toast.LENGTH_LONG).show();
                        }
                        else{
                            intent.putExtra("User",username);
                            startActivity(intent);
                        }

                    }
                }
        );
    }
}
