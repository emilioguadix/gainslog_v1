package com.example.proyectofinal_deint_v1.ui.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinal_deint_v1.R;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.WorkData;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.serie.Serie;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.serie.TypeSerie;

import java.util.ArrayList;
import java.util.List;

public class WorkDataAdapter extends RecyclerView.Adapter<WorkDataAdapter.ViewHolder> {

    private Context context;
    private List<WorkData> list;
    private WorkDataAdapter.onWorkDataClickListener listener;

    public interface onWorkDataClickListener extends View.OnClickListener{
        void onClick(WorkData workData);
        void onLongClick(WorkData workData);
    }

    public WorkDataAdapter(Context context, List<WorkData> list, WorkDataAdapter.onWorkDataClickListener listener) {
        if(list == null){list = new ArrayList<>();}
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.log_workdata_item,parent,false);
        return new WorkDataAdapter.ViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvTitle.setText(list.get(position).getNameExercise());
        ArrayAdapter adapter = new ArrayAdapter(context,android.R.layout.simple_list_item_1,list.get(position).getSerieList());
        holder.serieList.setAdapter(adapter);
    }

    //Para actualizar los datos de la lista -->
    //Usamos el metodo  notifyDataSetChanged(), para uqe la vista se anule y se vuelva a dibujar
    public void updateData(List<WorkData> list){
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void delete(WorkData workData){
        list.remove(workData);
        notifyDataSetChanged();
    }

    public void add(WorkData workData){
        list.add(workData);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ListView serieList;

        public ViewHolder(@NonNull View itemView, final WorkDataAdapter.onWorkDataClickListener listener) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitleLog);
            serieList = itemView.findViewById(R.id.lv_cv_WorkData);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(list.get(getAdapterPosition()));
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onLongClick(list.get(getAdapterPosition()));
                    return  true;
                }
            });
        }
    }
}
