package com.test.databasetest;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private MyDatabaseHelper dbHelper;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        dbHelper = new MyDatabaseHelper(this, "BookStore.db", null, 2);
        Button createDatabase = (Button)findViewById(R.id.create_database);
        
        createDatabase.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dbHelper.getWritableDatabase();
			}
		});
        
        Button addData = (Button)findViewById(R.id.add_data);
        addData.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				ContentValues values = new ContentValues();
				//开始组装第一条数据
				values.put("name","The Da Vinci Code");
				values.put("author","Dan Brown");
				values.put("pages", 454);
				values.put("price",16.69);
				db.insert("Book", null, values);
				values.clear();
				//开始组装第二条数据
				values.put("name","The Lost Symbol");
				values.put("author", "Dan Brown");
				values.put("pages", 510);
				values.put("price",19.95);
				db.insert("Book", null, values);
			}
		});
        
        Button updateData = (Button)findViewById(R.id.update_data);
        updateData.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				SQLiteDatabase db = dbHelper.getWritableDatabase();
			    ContentValues values = new ContentValues();
			    values.put("price", 10.99);
			    db.update("Book", values, "name = ?", new String[]{"The Da Vinci Code"});
			}
		});
        
        Button deletedata =(Button)findViewById(R.id.delete_data);
        deletedata.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
            	SQLiteDatabase db = dbHelper.getWritableDatabase();
            	db.delete("Book", "pages > ?",new String[]{"500"});
			}
		});
        
        Button querydata =(Button)findViewById(R.id.query_data);
        querydata.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
                 SQLiteDatabase db = dbHelper.getWritableDatabase();
                 //查询Book表中的所有数据
                 Cursor cursor = db.query("Book", null, null, null, null, null, null);
                 if(cursor.moveToFirst())
                 {
                	 do{
	                	 String name = cursor.getString(cursor.getColumnIndex("name"));
	                	 String author = cursor.getString(cursor.getColumnIndex("author"));
	                	 int pages = cursor.getInt(cursor.getColumnIndex("pages"));
	                	 double price = cursor.getDouble(cursor.getColumnIndex("price"));
	                	 
	                	 Log.d("MainActivity", "book name is " + name);
	                	 Log.d("MainActivity", "book author is " + author);
	                	 Log.d("MainActivity", "book pages is " + pages);
	                	 Log.d("MainActivity", "book price is " + price);
                	 }while(cursor.moveToNext());
                 }
                 cursor.close();
			}
		});
        
        Button replaceData = (Button)findViewById(R.id.replace_data);
        replaceData.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.beginTransaction();   //开启事物
                try
                {
                	db.delete("Book", null, null);
/*                	if(true)
                	{
                		throw new NullPointerException();
                	}
*/
/*					ContentValues values = new ContentValues();
                	values.put("name", "Game of Thrones");
                	values.put("author","George Martin");
                	values.put("pages",720);
                	values.put(price",20.85);      //这5行可由下面137行（下面一行）代替
*/
                	db.execSQL("insert into Book(name,author,pages,price) values(?,?,?,?)", new String[]{"Game of Thrones","George Martin","720","20.85"});
                	db.setTransactionSuccessful();
                }catch(Exception e)
                {
                	e.printStackTrace();
                }
                finally
                {
                	db.endTransaction();
                }
			}
		});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
