package com.example.hp.expense_manager.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hp.expense_manager.R;
import com.example.hp.expense_manager.pojo.AdminItem;

import java.util.ArrayList;


public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.CustomHolder> {

    Context mContext;
    LayoutInflater inflater;
    View view;
    ArrayList<AdminItem> items;

    public AdminAdapter(Context activity, ArrayList<AdminItem> arrayList) {
        this.mContext = activity;
        inflater = LayoutInflater.from(mContext);
        this.items = arrayList;
    }


    @Override
    public AdminAdapter.CustomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = inflater.inflate(R.layout.row_item, parent, false);//is layout ka n
        return new CustomHolder(view);
    }

    /*
    This method is written to open or close a particular section, by passing the position of the element
    and then calling notifydataSetchanged to make the changes visible
     */


    @Override
    public void onBindViewHolder(final CustomHolder holder, final int position) {
        AdminItem item = items.get(position);
        holder.icon.setImageResource(item.getIcon());
        holder.title.setText(item.getTitle());
        holder.containerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, items.get(position).getAction()));
            }
        });
    }


    @Override
    public int getItemCount() {
        return items.size();
    }


    public class CustomHolder extends RecyclerView.ViewHolder {


        ImageView icon;
        TextView title;
        View containerView;

        public CustomHolder(View itemView) {
            super(itemView);
            containerView = itemView;
            title = (TextView) itemView.findViewById(R.id.title);

            icon = (ImageView) itemView.findViewById(R.id.icon);


        }

    }
}


