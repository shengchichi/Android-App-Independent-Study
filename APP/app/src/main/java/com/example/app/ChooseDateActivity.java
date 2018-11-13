package com.example.app;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChooseDateActivity extends AppCompatActivity {

    private Button chooseBtn;
    private TextView textDaySelected;
    private MyDBHelper helper;
    private RecyclerView mRecyclerView;
    private DateAdapter mAdapter;
    private String post_date = null;
    private String[] dates;
    //private ArrayList<String> dates = new ArrayList<String>();
    private Context context;
    private int id=0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_date);
        context = this;
        init();
        //連線
        String url="http://192.168.137.1/select.php";
        //連接資料庫 取得當天所有時間  start connecting to database
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        // 使用Request.Builder設定一個連線必要的資訊，如網址則使用url()方法定義，
        // 完成後再呼叫build方法即產生HTTP的請求(Request)，此時還未連線至主機
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String json = response.body().string();
                Log.d("OKHTTP", json);
                //解析JSON
                parseJSON(json);

            }
            @Override
            public void onFailure(Call call, IOException e) {
                //告知使用者連線失敗
            }
        });

        //end connecting
        //Thread.sleep(3000);

        //connect = new ConnectDB();
        //connect.start();
        //選擇日期

        chooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendarDialog();
            }
        });
        GetDataFromSQLite();
        mAdapter = new DateAdapter(this, dates);
        updateRV();
        mAdapter.notifyDataSetChanged();
        Log.d("qweeqe", "onCreate: ");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_id = item.getItemId();

        if(item_id == R.id.phonecall)
        {
            Intent intent = new Intent(this, PhoneActivity.class);
            startActivity(intent);
        }
        return true;
    }
    protected void parseJSON(String str) {
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<JsonData>>() {
        }.getType();
        ArrayList<JsonData> jsonArr = gson.fromJson(str, listType);
        StringBuffer sb = new StringBuffer();
        helper = new MyDBHelper(this, "data.db", null, 1);
        SQLiteDatabase mDB = null;
        mDB = helper.getWritableDatabase();
        Cursor c = null;
        c = mDB.rawQuery("select * from localDB",null);
        c.moveToFirst();   //將指標移至第一筆資料
        id = c.getCount();
        //Log.d("row", "parseJSON: "+id);
        for (JsonData obj : jsonArr) {
            // Insert by raw SQL
            //Log.d("qwqw", "parseJSON: "+obj.getId());
            if(id<obj.getId())
            {
                mDB.execSQL("INSERT INTO localDB ( time, address, posture1, posture2, posture3, posture4, distance, focus_ratio) " +
                        "VALUES ('" + obj.getTime() + "','" + obj.getAddress() + "',"
                        + obj.getPosture1() + "," + obj.getPosture2() + "," + obj.getPosture3() + ","
                        + obj.getPosture4() + ",'" + obj.getDistance() + "'," + obj.getFocus_ratio() +
                        ")");
            }
        }
        c.close();
        mDB.close();

        //Log.d("qweqeqw", "parseJSON: done!");
    }

    private void init() {
        chooseBtn = (Button) findViewById(R.id.choose);
        textDaySelected = (TextView) findViewById(R.id.daySelected);
        //取得系統時間
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());
        post_date = formatter.format(curDate);
        textDaySelected.setText(post_date);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

    }

    private void showCalendarDialog() {

        final String old = textDaySelected.getText().toString();

        final View item = LayoutInflater.from(ChooseDateActivity.this).inflate(R.layout.dialog_calendar, null, false);

        final CalendarView calendarView = (CalendarView) item.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                String yyyy = String.format("%04d",year);
                String mm = String.format("%02d",month+1);
                String dd = String.format("%02d",dayOfMonth);
                String date = yyyy + "-" + mm + "-" + dd;
                post_date = date;
                textDaySelected.setText(date);
                //Log.d("hit", "onSelectedDayChange: ");
            }
        });

        new AlertDialog.Builder(ChooseDateActivity.this)
                .setView(item)
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        GetDataFromSQLite();
                        mAdapter = new DateAdapter(context, dates);
                        updateRV();
                        mAdapter.notifyDataSetChanged();
                        //Log.d("here", "onClick: " +dates[0]);

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        textDaySelected.setText(old);
                    }
                })
                .show();
    }

    public void GetDataFromSQLite()
    {
        helper = new MyDBHelper(this, "data.db", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = null;
        Log.d("dsd", "GetDataFromSQLite: "+post_date);
        cursor = db.rawQuery("select * from localDB where time >= '"+post_date+
                "' and time <= date('"+post_date+"','+1 day')",null );

        int rows_num = cursor.getCount();//取得資料表列數
        Log.d("dsds", "GetDataFromSQLite: "+rows_num);
        if (rows_num != 0) {
            dates = new String[rows_num];
            cursor.moveToFirst();   //將指標移至第一筆資料
            for (int i = 0; i < rows_num; i++) {
                dates[i] = cursor.getString(cursor.getColumnIndex("time"));
                cursor.moveToNext();//將指標移至下一筆資料
            }
            cursor.close(); //關閉Cursor
            //db.close();
        } else {
            dates = new String[0];
        }
    }
    private void updateRV()
    {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);



        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(context, ContentActivity.class);
                intent.putExtra("date_str", dates[position]);
                startActivity(intent);
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.VERTICAL));

    }


    class DateAdapter extends RecyclerView.Adapter<DateAdapter.ViewHolder>{

        private OnItemClickListener mOnItemClickListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener){
            this.mOnItemClickListener = onItemClickListener;
        }
        private LayoutInflater mLayoutInflater;
        private String[] mDataset;
        private Context context;

        public DateAdapter(Context context, String[] dataset){
            this.mLayoutInflater = LayoutInflater.from(context);
            this.mDataset = dataset;
            this.context = context;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mLayoutInflater.inflate(R.layout.item_date, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            viewHolder.mDate = (TextView) view.findViewById(R.id.time);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.mDate.setText(mDataset[position]);

            if(mOnItemClickListener != null){
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(holder.itemView, position);
                        Toast.makeText(context, "You click item " + mDataset[position], Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return mDataset.length;
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            public TextView mDate;

            public ViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
