package com.example.veryfy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.veryfy.R;
import com.ionnex.veryfy.model.OCRModel;

import java.util.ArrayList;

public class OCRResultAdapter extends RecyclerView.Adapter<OCRResultAdapter.ViewHolder> {

    private ArrayList<OCRModel> homeList;

    private Context context;
    public OCRResultAdapter(Context context, ArrayList<OCRModel> homeList) {
        this.homeList = homeList;
        this.context = context;
    }

    public void setData(ArrayList<OCRModel> homeList) {
        this.homeList = homeList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() )
                .inflate(R.layout.adapter_ocr_result,parent,false);
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvKey.setText( homeList.get(position).getLabel().getDescription().toUpperCase() );
        holder.tvValue.setText(homeList.get(position).getValue().toUpperCase());
    }

    @Override
    public int getItemCount() {
        return homeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvKey;
        TextView tvValue;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            tvKey = itemView.findViewById(R.id.tvKey );
            tvValue = itemView.findViewById(R.id.tvValue );

        }
    }
}
