package com.example.app;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.EntryXComparator;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by 鵬仁 on 2017/5/9.
 */
public class HabitFragment extends Fragment {

    private View view;
    private LineChart lineChart;
    private  String date_str;
    private MyDBHelper helper;
    private int[] dis;
    private String type;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_habit, container, false);

        lineChart= (LineChart) view.findViewById(R.id.lineChart);
        date_str = getArguments().getString("date_str");
        lineChart.getDescription().setEnabled(false);
        lineChart.setNoDataText("No Data");//没有数据时显示的文字
        lineChart.setNoDataTextColor(Color.BLUE);//没有数据时显示文字的颜色
        lineChart.setDrawBorders(false);//禁止绘制图表边框的线
        lineChart.setDrawGridBackground(false);
        Log.d("check", "onCreateView: ");
        parseResult(getResult());

        ArrayList<Entry> values1 = new ArrayList<>();
        ArrayList<Entry> values2 = new ArrayList<>();
        if(type=="C")
        {

            values1.add(new Entry(3,dis[0]));
            values1.add(new Entry(6,dis[1]));
            values1.add(new Entry(9,dis[2]));
            values1.add(new Entry(12,dis[3]));
            values1.add(new Entry(15,dis[4]));
            //LineDataSet每一个对象就是一条连接线
            LineDataSet set1;
            set1 = new LineDataSet(values1,"與螢幕平均距離");
            set1.setColor(Color.GREEN);
            set1.setCircleColor(Color.GREEN);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setValueTextSize(10f);
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);
            lineChart.setData(data);
            //绘制图表
            lineChart.invalidate();

        }
        else
        {
            values2.add(new Entry(3,dis[0]));
            values2.add(new Entry(6,dis[1]));
            values2.add(new Entry(9,dis[2]));
            values2.add(new Entry(12,dis[3]));
            values2.add(new Entry(15,dis[4]));
            LineDataSet set2;
            set2 = new LineDataSet(values2,"與書本平均距離   (cm/minute)");
            set2.setColor(Color.MAGENTA);
            set2.setCircleColor(Color.MAGENTA);
            set2.setLineWidth(1f);
            set2.setCircleRadius(3f);
            set2.setValueTextSize(10f);
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set2);
            LineData data = new LineData(dataSets);
            lineChart.setData(data);
            //绘制图表
            lineChart.invalidate();
        }


        XAxis xAxis = lineChart.getXAxis();
        xAxis.setEnabled(true);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(true);
        xAxis.setDrawLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setAvoidFirstLastClipping(true);

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);

        return view;
    }
    public String getResult()
    {
        String distance=null;
        try
        {
            helper = new MyDBHelper(this.getActivity(),"data.db",null,1);
            SQLiteDatabase db = helper.getReadableDatabase();
            Cursor c = null;

            c = db.rawQuery("select * from localDB where time = '"+date_str+"'", null);
            c.moveToFirst();   //將指標移至第一筆資料
            distance = c.getString(c.getColumnIndex("distance"));
            Log.d("habit", "getResult: "+distance);
            c.close();
            db.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return distance;
    }
    private void parseResult(String str)
    {

        String[] element = str.split("#",6);
        //Log.d("aa", "parseResult: "+element[5]);
        for (String token:element)
        {
            Log.d("sdsd", "parseResult: "+token);
        }
        type = element[0];
        dis = new int[element.length-1];
        for(int i=1;i<element.length;i++) {
            Log.d("sdsd", "parseResult: "+Integer.parseInt(element[i]));
            dis[i - 1] = Integer.parseInt(element[i]);
        }
    }
}
