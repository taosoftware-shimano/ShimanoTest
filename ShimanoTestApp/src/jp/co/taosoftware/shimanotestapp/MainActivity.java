package jp.co.taosoftware.shimanotestapp;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

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
	String[][] mData = {
			{"山田", "太郎01"}, {"鈴木", "花子02"},{"田中", "次郎03"},{"佐々木", "さち子04"},{"島野", "英司05"},
			{"山田", "太郎06"}, {"鈴木", "花子07"},{"田中", "次郎08"},{"佐々木", "さち子09"},{"島野", "英司10"},
			{"山田", "太郎11"}, {"鈴木", "花子12"},{"田中", "次郎13"},{"佐々木", "さち子14"},{"島野", "英司15"},
			{"山田", "太郎16"}, {"鈴木", "花子17"},{"田中", "次郎18"},{"佐々木", "さち子19"},{"島野", "英司20"},
			{"山田", "太郎21"}, {"鈴木", "花子22"},{"田中", "次郎23"},{"佐々木", "さち子24"},{"島野", "英司25"},
			{"山田", "太郎26"}, {"鈴木", "花子27"},{"田中", "次郎28"},{"佐々木", "さち子29"},{"島野", "英司30"},
			{"山田", "太郎31"}, {"鈴木", "花子32"},{"田中", "次郎33"},{"佐々木", "さち子34"},{"島野", "英司35"},
			{"山田", "太郎36"}, {"鈴木", "花子37"},{"田中", "次郎38"},{"佐々木", "さち子39"},{"島野", "英司40"},
			{"山田", "太郎41"}, {"鈴木", "花子42"},{"田中", "次郎43"},{"佐々木", "さち子44"},{"島野", "英司45"},
			{"山田", "太郎46"}, {"鈴木", "花子57"},{"田中", "次郎48"},{"佐々木", "さち子49"},{"島野", "英司50"}
			};
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
           					Thread.sleep(1000);	//休んでみる
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
        	String message = "";
            @Override
            public void run() {
            	ArrayList<Person> personList;
                // JasonDataを読み込む
            	try {
            		FileInputStream fis = openFileInput(FILE_NAME);
            		JsonFactory jsonFactory = new JsonFactory();  
            		JsonParser jp = jsonFactory.createJsonParser(fis); 
            		
            		//コレクションの作製
            		personList = new ArrayList<Person>();  
            		
            		jp.nextToken(); //Start Object
            		while (jp.nextToken() != JsonToken.END_OBJECT) {
                		String fieldName = jp.getCurrentName();
                		if ("Person".equals(fieldName)){
                			Person psn = new Person();
                			//1人分のデータ
                			jp.nextToken(); //StartObject
                			while (jp.nextToken() != JsonToken.END_OBJECT){
                				String itemName = jp.getCurrentName();
                				if ("lastName".equals(itemName)) {
                					psn.lastName = jp.getText();
                				} else {
                					if ("firstName".equals(itemName)){
                						psn.firstName = jp.getText();     
                					}
                				}
                			}
                			//1人分のデータ出来上がり＆格納
                			personList.add(psn);
                		}
        			}
            		//画面表示用文言の編集
                	 Iterator<Person> itr = personList.iterator();
                	 while (itr.hasNext() == true) {
                		 Person psn = itr.next();
                		 message += "姓=" + psn.lastName + "  " + "名=" + psn.firstName + "\n";
                	 }
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
