package com.example.app;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.EventLogTags;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 鵬仁 on 2017/5/9.
 */
public class FocusFragment extends Fragment {

    private View view;
    private PieChart mChart;
    private MyDBHelper helper;
    private String[] x = new String[] { "專心", "不專心" };
    private float[] y;
    private String date_str;






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_focus, container, false);
        mChart = (PieChart) view.findViewById(R.id.piechart);
        date_str = getArguments().getString("date_str");
        float num=getResult();
        y = new float[2];
        y[0] = num;
        y[1] = 100 - num;

        setData(x.length);

        mChart.getDescription().setEnabled(false);

        mChart.setCenterText("專心時間");


        return view;
    }
    public float getResult()
    {
        float ratio=20;
        try
        {
            helper = new MyDBHelper(this.getActivity(),"data.db",null,1);
            SQLiteDatabase db = helper.getReadableDatabase();
            Cursor c = null;

            c = db.rawQuery("select * from localDB where time = '"+date_str+"'", null);
            c.moveToFirst();   //將指標移至第一筆資料
            ratio = (float)c.getDouble(c.getColumnIndex("focus_ratio"));
            //double ratiod = c.getDouble(c.getColumnIndex("focus_ratio"));
            String re = c.getString(c.getColumnIndex("time"));
            String r = c.getString(c.getColumnIndex("distance"));
            Log.d("dsdsds", "getResult: "+re);
            c.close();
            db.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return ratio;
    }



    private void setData(int count){
        ArrayList<String> xVals = new ArrayList<String>();

        ArrayList<PieEntry> yVals = new ArrayList<PieEntry>();

        for (int xi = 0; xi < count; xi++) {
            xVals.add(xi, x[xi]);

            yVals.add(new PieEntry(y[xi], x[xi]));
        }

        PieDataSet yDataSet = new PieDataSet(yVals, "%");

        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.GREEN);
        colors.add(Color.RED);
        yDataSet.setColors(colors);

        PieData data = new PieData(yDataSet);

        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(20f);
        data.setValueTextColor(Color.BLACK);

        mChart.setData(data);
        mChart.setEntryLabelTextSize(15f);
        mChart.invalidate();
    }
}
