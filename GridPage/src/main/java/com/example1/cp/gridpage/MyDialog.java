package com.example1.cp.gridpage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MyDialog extends Activity{
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_mydialog);
    Intent intent = getIntent();
    if(intent!=null){
      
      String myImage = intent.getStringExtra("myImage");
      String myTitle = intent.getStringExtra("myTitle");
      String myDescription = intent.getStringExtra("myDescription");

      ImageView image = (ImageView) findViewById(R.id.imageView1);
      //image.setImageResource(myImage);
      TextView title = (TextView) findViewById(R.id.textView1);
      title.setText(myTitle);
      TextView description = (TextView) findViewById(R.id.textView2);
      description.setText(myDescription);
    }
    
    
    
    
}
  public void closeDialog(View v){
    finish();
  }
}