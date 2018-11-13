package com.example.app;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 鵬仁 on 2017/5/9.
 */
public class PostureFragment extends Fragment {

    private View view;
    private ListView listview;
    List<String> list;
    List<Boolean> listShow;    // 這個用來記錄哪幾個 item 是被打勾的
    private CheckedTextView posture1;
    private MyDBHelper helper;
    private String date_str;
    private int p1;
    private int p2;
    private int p3;
    private int p4;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_posture, container, false);
        date_str = getArguments().getString("date_str");
        getResult();
        listview = (ListView)view.findViewById(R.id.listView);
        listview.setEnabled(false);
        list = new ArrayList<String>();
        list.add("正常");
        list.add("歪頭");
        list.add("撐手");
        list.add("趴睡");
        listShow = new ArrayList<Boolean>();
        listShow.add(check(p1));
        listShow.add(check(p2));
        listShow.add(check(p3));
        listShow.add(check(p4));
        ListAdapter adapterItem = new ListAdapter(getActivity(), list);
        listview.setAdapter(adapterItem);

        return view;
    }
    public boolean check(int c)
    {
        if(c==1)
            return true;
        else
            return false;
    }
    public void getResult()
    {

        try
        {
            helper = new MyDBHelper(this.getActivity(),"data.db",null,1);
            SQLiteDatabase db = helper.getReadableDatabase();
            Cursor c = null;

            c = db.rawQuery("select * from localDB where time = '"+date_str+"'", null);
            c.moveToFirst();   //將指標移至第一筆資料
            p1 = c.getInt(c.getColumnIndex("posture1"));
            p2 = c.getInt(c.getColumnIndex("posture2"));
            p3 = c.getInt(c.getColumnIndex("posture3"));
            p4 = c.getInt(c.getColumnIndex("posture4"));
            Log.d("dsd", "getResult: "+p1+p2+p3+p4);
            c.close();
            db.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public class ListAdapter extends BaseAdapter
    {
        private Activity activity;
        private List<String> mList;

        private LayoutInflater inflater = null;

        public ListAdapter(Activity a, List<String> list)
        {
            activity = a;
            mList = list;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount()
        {
            return mList.size();
        }

        public Object getItem(int position)
        {
            return position;
        }

        public long getItemId(int position)
        {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent)
        {
            View vi = convertView;
            if(convertView==null)
            {
                vi = inflater.inflate(R.layout.item_checklist, null);
            }

            //CheckedTextView chkBshow = (CheckedTextView) vi.findViewById(R.id.check1);
            posture1 = (CheckedTextView)vi.findViewById(R.id.posture1);

             //posture1.setChecked(check(p1));
            //posture2.setChecked(check(p2));
            //posture3.setChecked(check(p3));
            //posture4.setChecked(check(p4));
            posture1.setText(mList.get(position).toString());
            posture1.setChecked(listShow.get(position));
            return vi;
        }
    }

}
