package com.yt.IntelligentPigsystem.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MySqlHelper extends SQLiteOpenHelper {
    public static final String MYDB = "pig.db";
    public static final int VERSION = 1;

    public MySqlHelper(@Nullable Context context) {
        super(context, MYDB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //T-sql语句
        String sql = "create table users(tel text primary key not null,name text not null,password text not null)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public static class SignHelper {
        private MySqlHelper dbHelper;
        private Context context;

        public SignHelper(Context context) {
            this.context = context;
            dbHelper = new MySqlHelper(context);
        }

        public void SignUp(String tel, String name, String passwd) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("tel", tel);
            values.put("name", name);
            values.put("password", passwd);
            long result = db.insert("users", null, values);
            if (result != -1) {
                Toast.makeText(context, "注册成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "注册失败", Toast.LENGTH_SHORT).show();
            }
            db.close();
        }

        public void Forget(String tel, String name, String newPasswd) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("password", newPasswd);
            int rows = db.update("users", values, "tel=? AND name=?", new String[]{tel, name});
            db.close();
            if (rows > 0) {
                Toast.makeText(context, "密码更新成功", Toast.LENGTH_SHORT).show();
            } else {
                if (rows == 0) {
                    Toast.makeText(context, "密码更新失败", Toast.LENGTH_SHORT).show();
                } else {
                    //Log.d("ForgetActivity", "Rows affected: " + rows);
                    Toast.makeText(context, "密码更新失败,未知原因~", Toast.LENGTH_SHORT).show();
                }
            }
        }
        /*
        * 1.要查询的数据是什么？
        * 2.在哪里查？
        * 3.查询的结果是什么？（查到/没查到）
        * */
        public boolean Login(SQLiteDatabase db,String tel,String password){
            boolean isLogin = false;
            Cursor cursor = db.query("users",null,null,null,null,null,null,null);
            //将游标移动至表中的第一条记录
            cursor.moveToFirst();
            //限制游标只在表内
            while (!cursor.isAfterLast()){
                //
                String tels = cursor.getString(cursor.getColumnIndexOrThrow("tel"));
                String pass = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                //将获取的数据与输入的进行匹配
                if (tel.equals(tels)&&password.equals(pass)){
                    //如果匹配就将isLogin置真
                    isLogin = true;
                    break;
                }
                cursor.moveToNext();
                Log.e("Error","查到数据");
            }
            if (!isLogin){
                Toast.makeText(context.getApplicationContext(),"账号或密码不匹配！",Toast.LENGTH_SHORT).show();
            }
            cursor.close();
            return isLogin;

        }
        public void Delete(String tel){
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String where = "tel=?";
            String[] args = {tel};
            db.delete("users", where, args);
            db.close();
        }
        public String getUsernameByTel(String tel) {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String username = null;

            // SQL 查询语句
            String query = "SELECT username FROM users WHERE tel = ?";
            Cursor cursor = db.rawQuery(query, new String[]{tel});

            if (cursor.moveToFirst()) {
                // 获取第一行的第一列的数据
                username = cursor.getString(0);
            }
            cursor.close();
            return username;
        }


    }
}
