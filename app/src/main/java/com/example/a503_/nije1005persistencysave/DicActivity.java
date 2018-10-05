package com.example.a503_.nije1005persistencysave;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

// 데이터베이스 클래스(SQLiteOpenHelper 상속)
class WordDBHelper extends SQLiteOpenHelper{
    // 생성자
    public WordDBHelper(Context context){
        super(context, "engword.db", null, 1);
    }

    // 처음 사용할 때 호출되는 메소드
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table dic(_id integer primary key, autoincreament, eng text, kor text);");
    }
    // 버전이 변경되는 호출되는 메소드
    public void onUpgrade(SQLiteDatabase db, int old, int newVersion){
        // 테이블 삭제
        db.execSQL("drop table if exists dic");
        // 테이블 다시 생성
        onCreate(db);
    }

}

public class DicActivity extends AppCompatActivity {
    private WordDBHelper dbHelper;
    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dic);

        dbHelper = new WordDBHelper(this);
        result = (TextView)findViewById(R.id.result);


        // 삭제하기
        findViewById(R.id.delete).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.execSQL("delete from dic where eng = 'orange';");
                dbHelper.close();
                result.setText("삭제 성공");
            }
        });


        // 갱신 버튼을 눌렀을 때 - eng 값이 orange 인 데이터를 kor 값을 귤로 변경하기
        findViewById(R.id.update).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.execSQL("update dic set kor = '생귤탱귤' where eng = 'orange';");
                db.execSQL("delete from dic where eng = 'orange';");
                dbHelper.close();
                result.setText("수정 성공");
                }
        });


        // 조회 버튼을 눌럿을 때
        findViewById(R.id.select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                // select 구문 실행
                Cursor cursor = db.rawQuery("select eng, kor from dic", null);
                String resultText = "";
                // 데이터 전체 순회
                while (cursor.moveToNext()){
                    String eng = cursor.getString(0);
                    String kor = cursor.getString(1);
                    resultText = resultText + eng + ":" + kor + "\n";
                }
                result.setText(resultText);
                db.close();
            }
        });


        findViewById(R.id.insert).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.e("tag01","이벤트 발생");
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                // Log.e("tag02","데이터베이스 연결 에러");
                // SQL 로 insert
                /*
                db.execSQL("insert into dic(eng, kor) values('apple', '사과');");
                Log.e("tag03","데이터베이스 실행 에러");
                */

                // SQL 을 사용하지 않고 데이터를 삽입
                ContentValues values = new ContentValues();
                values.put("eng", "orange");
                values.put("kor", "오렌지");
                db.insert("dic", null, values);
                result.setText("삽입 성공");
                dbHelper.close();
            }
        });
    }
}
