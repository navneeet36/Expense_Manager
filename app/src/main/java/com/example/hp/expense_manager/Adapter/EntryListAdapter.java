package com.example.hp.expense_manager.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.hp.expense_manager.R;
import com.example.hp.expense_manager.pojo.EntryList;

import java.util.ArrayList;

/**
 * Created by hp on 22-Jul-17.
 */

public class EntryListAdapter extends RecyclerView.Adapter<EntryListAdapter.CustomHolder> {

    Context mContext;
    LayoutInflater inflater;
    View view;
    ArrayList<EntryList> items;
    OnItemClickListener onItemClickListener;

    SparseBooleanArray expandArray;



    public EntryListAdapter(Context activity, ArrayList<EntryList> arrayList) {
        this.mContext = activity;
        inflater = LayoutInflater.from(mContext);
        this.items = arrayList;
        expandArray = new SparseBooleanArray();
        for (int i = 0; i < items.size(); i++)
            expandArray.put(i, false);
    }
    public interface OnItemClickListener {
        void onEventItemClick(EntryList entrylist);
        void EventItemClick(EntryList entrylist);


    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }



    @Override
    public EntryListAdapter.CustomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = inflater.inflate(R.layout.entrylistitem, parent, false);
        return new CustomHolder(view);
    }




    @Override
    public void onBindViewHolder(final CustomHolder holder, final int position) {
        EntryList item = items.get(position);
              holder.account.setText(item.getAccount());
        holder.date.setText(item.getDate());
        holder.time.setText(item.getTime());
        holder.discription.setText("BY : "+item.getUsername());
        holder.dis.setText("Dis : "+item.getDescription());
        if(item.getIncoming().equals("yes")) {
            holder.row.setBackgroundColor(Color.WHITE);
            holder.amount.setText("+  $"+item.getAmount());

        }
        if(item.getOutgoing().equals("yes")) {
            holder.row.setBackgroundColor(Color.YELLOW);
            holder.amount.setText("-  $"+item.getAmount());

        }
        holder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!expandArray.get(position))
                    expandArray.put(position, true);
                else expandArray.put(position, false);
                notifyDataSetChanged();
            }
        });


        if (expandArray.get(position, true)) {

            holder.update.setVisibility(View.VISIBLE);
            holder.delete.setVisibility(View.VISIBLE);
            holder.dis.setVisibility(View.VISIBLE);
        } else {

            holder.update.setVisibility(View.GONE);
            holder.delete.setVisibility(View.GONE);
            holder.dis.setVisibility(View.GONE);
        }
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null)
                    onItemClickListener.onEventItemClick(items.get(position));


            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null)
                    onItemClickListener.EventItemClick(items.get(position));

            }
        });





    }


    @Override
    public int getItemCount() {
        return items.size();
    }


    public class CustomHolder extends RecyclerView.ViewHolder {


        TextView amount,account,date,time;

        TextView discription,dis;
        Button update,delete;
        View row;

        public CustomHolder(View itemView) {
            super(itemView);
            row =itemView;
            amount=(TextView)itemView.findViewById(R.id.Amount);
            account = (TextView) itemView.findViewById(R.id.Account);
            date=(TextView)itemView.findViewById(R.id.date);
            time=(TextView)itemView.findViewById(R.id.time);
            dis=(TextView)itemView.findViewById(R.id.dis);
            discription=(TextView)itemView.findViewById(R.id.discription);
            update = (Button) itemView.findViewById(R.id.update);
            delete=(Button)itemView.findViewById(R.id.delete);



        }

    }
}

