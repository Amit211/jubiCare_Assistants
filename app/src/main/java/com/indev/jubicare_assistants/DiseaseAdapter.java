package com.indev.jubicare_assistants;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.indev.jubicare_assistants.sqllite_db.SqliteHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DiseaseAdapter extends BaseAdapter {
    ArrayList<String> arrayList;
    Context context;
    LayoutInflater inflater;
    SqliteHelper sqliteHelper;
    HashMap<String, Integer> diseaseHM;
    ArrayList<String> diseaseAl;


    public DiseaseAdapter(Context context, ArrayList<String> symptoms) {
        this.arrayList = symptoms;
        this.context = context;
        inflater = LayoutInflater.from(context);
        sqliteHelper = new SqliteHelper(context);
        diseaseHM = new HashMap<>();
        diseaseAl = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new DiseaseAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.disease_adapter_layout, null);
            holder.disease_spinner = convertView.findViewById(R.id.disease_spinner);
            holder.symptomList = convertView.findViewById(R.id.symptomList);
            convertView.setTag(holder);
        } else {
            holder = (DiseaseAdapter.ViewHolder) convertView.getTag();
        }
        diseaseHM = sqliteHelper.getDisease();
        diseaseAl.clear();

        for (int i = 0; i < diseaseHM.size(); i++) {
            diseaseAl.add(diseaseHM.keySet().toArray()[i].toString().trim());
        }
        final List<KeyPairBoolData> listArray1 = new ArrayList<>();
        for (int i = 0; i < diseaseAl.size(); i++) {
            KeyPairBoolData h = new KeyPairBoolData();
            h.setId(i + 1);
            h.setName(diseaseAl.get(i));
            h.setSelected(false);
            listArray1.add(h);
        }
        diseaseAl.add(0, "Select Disease");
        ArrayAdapter arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, diseaseAl);
        holder.disease_spinner.setAdapter(arrayAdapter);
        holder.symptomList.setText(arrayList.get(position).toString());
        return convertView;
    }

    public class ViewHolder {
        Spinner disease_spinner;
        TextView symptomList;
    }
}
