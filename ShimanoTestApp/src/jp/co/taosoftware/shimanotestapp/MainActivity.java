package jp.co.taosoftware.shimanotestapp;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;


public class MainActivity extends Activity {
	
	Handler mHandler = new Handler();
	TextView mTextView;
	String[][] mData = {{"山田", "太郎"}, {"鈴木", "花子"},{"島野", "英司"}};
	private static final String FILE_NAME = "sample.json";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView)findViewById(R.id.the_text_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_write_json:
            	writeJsonData();
            	return true;

            case R.id.menu_read_json:
            	readJsonData();
            	return true;

            default:
                Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
                break;
        }
        
        return false;
    }
    
    private void writeJsonData() {
    	// スレッド起動
        (new Thread(new Runnable() {
        	String message = "かいたよ";
            @Override
            public void run() {
                // JasonDataを書き出す,
            	try {
            		FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            		//Create Json Object
            		JsonFactory 	jf = new JsonFactory();
            		JsonGenerator	jg = jf.createJsonGenerator(fos); 
            		jg.writeStartObject();
            		for (int i = 0; i < mData.length; i++) {
            			if (mData[i].length == 2) {
           					jg.writeObjectFieldStart("Person");
           					jg.writeStringField("lastName", mData[i][0]); 
           					jg.writeStringField("firstName", mData[i][1]);
           					jg.writeEndObject();
            			}
            		}
            		jg.writeEndObject();
            		jg.close();
            		fos.close();
            	} catch (Exception ex) {
            		Log.e("Shimano-Test", "File Write Error!!");
            		message = String.valueOf("書き込みエラー");
            	}
            	
                /**
                 * Handlerのpostメソッドを使ってUIスレッドに処理をdispatchします
                */
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        //取得したイメージをImageViewに設定
                        mTextView.setText(message);
                    }
                });
            }
        })).start();
    }


    private void readJsonData() {
    	// スレッド起動
        (new Thread(new Runnable() {
        	String message = "よんだよ";
            @Override
            public void run() {
                // JasonDataを読み込む
            	try {
            		FileInputStream fis = openFileInput(FILE_NAME);
            		//ObjectMapper mapper = ObjectMapper();
            		
            	} catch (Exception ex) {
            		Log.e("Shimano-Test", "File Read Error!!");
            		message = String.valueOf("読み込みエラー");
            	}
                /**
                 * Handlerのpostメソッドを使ってUIスレッドに処理をdispatchします
                */
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        //取得したイメージをImageViewに設定
                        mTextView.setText(message);
                    }
                });
            }
        })).start();
    }
}
