package com.example.proyectofinal_deint_v1.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinal_deint_v1.R;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.Exercise;
import com.github.ivbaranov.mli.MaterialLetterIcon;

import java.util.ArrayList;
import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ViewHolder> {
    private List<Exercise> list;
    private onBoxDataClickListener listener;

    public interface onBoxDataClickListener extends View.OnClickListener{
        void onClick(Exercise exercise);
        void onLongClick(Exercise exercise);
    }

    public ExerciseAdapter(List<Exercise> list, onBoxDataClickListener listener) {
        if(list == null){list = new ArrayList<Exercise>();}
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.boxdata_item,parent,false);
        return new ViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.icon.setLetter(list.get(position).getName());
        holder.tvText.setText(list.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    //Para actualizar los datos de la lista -->
    //Usamos el metodo  notifyDataSetChanged(), para uqe la vista se anule y se vuelva a dibujar
    public void updateData(List<Exercise> list){
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        MaterialLetterIcon icon;
        TextView tvText;

        public ViewHolder(@NonNull View itemView,final onBoxDataClickListener listener) {
            super(itemView);
            icon = itemView.findViewById(R.id.iconLetter);
            tvText = itemView.findViewById(R.id.tvBoxData);
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

    public void delete(Exercise exercise){
        list.remove(exercise);
        notifyDataSetChanged();
    }

    public void add(Exercise exercise){
        list.add(exercise);
        notifyDataSetChanged();
    }
}
