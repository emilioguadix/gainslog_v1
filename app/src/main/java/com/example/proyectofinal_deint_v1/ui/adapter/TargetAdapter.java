package com.example.proyectofinal_deint_v1.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinal_deint_v1.R;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.Exercise;
import com.example.proyectofinal_deint_v1.data.model.model.target.Target;
import com.github.ivbaranov.mli.MaterialLetterIcon;

import java.util.ArrayList;
import java.util.List;

public class TargetAdapter extends RecyclerView.Adapter<TargetAdapter.ViewHolder> {
    private List<Target> list;
    private onBoxDataClickListener listener;

    public interface onBoxDataClickListener extends View.OnClickListener{
        void onClick(Target target);
        void onLongClick(Target target);
    }

    public TargetAdapter(List<Target> list, onBoxDataClickListener listener) {
        if(list == null){list = new ArrayList<>();}
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
        holder.icon.setLetter(list.get(position).getNameTarget());
        holder.tvText.setText(list.get(position).getNameTarget());
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    //Para actualizar los datos de la lista -->
    //Usamos el metodo  notifyDataSetChanged(), para uqe la vista se anule y se vuelva a dibujar
    public void updateData(List<Target> list){
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

    public void delete(Target target){
        list.remove(target);
        notifyDataSetChanged();
    }

    public void add(Target target){
        list.add(target);
        notifyDataSetChanged();
    }
}
