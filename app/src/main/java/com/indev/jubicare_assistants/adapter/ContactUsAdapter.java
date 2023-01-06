package com.indev.jubicare_assistants.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.indev.jubicare_assistants.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactUsAdapter extends RecyclerView.Adapter<ContactUsAdapter.ViewHolder> {
    private Context context;
    private List<ContentValues> listModels;
    ClickListener clickListener;

    public ContactUsAdapter(Context context, List<ContentValues> listModels) {
        this.context = context;
        this.listModels = listModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_contact_us_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_name.setText(listModels.get(position).get("posts").toString());
        holder.tv_contact_no.setText("+91 "+ listModels.get(position).get("contact_no").toString());

        /*holder.tv_contact_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onListItemClick(position);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return listModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_contact_no)
        TextView tv_contact_no;
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.ll_contact_no)
        LinearLayout ll_contact_no;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ClickListener {
        void onItemClick(int position);

        void onListItemClick(int position);
    }

    public void onItemClick(ClickListener listener) {
        this.clickListener = listener;
    }
}
