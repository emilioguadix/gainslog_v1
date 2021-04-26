package com.example.proyectofinal_deint_v1.ui.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinal_deint_v1.R;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.Exercise;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.serie.Serie;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.serie.TypeSerie;
import com.github.ivbaranov.mli.MaterialLetterIcon;

import java.util.ArrayList;
import java.util.List;

public class SerieAdapter extends RecyclerView.Adapter<SerieAdapter.ViewHolder> {

    private Context context;
    private List<Serie> list;
    private SerieAdapter.onSerieClickListener listener;

    public interface onSerieClickListener extends View.OnClickListener{
        void onClick(Serie serie);
        void onLongClick(Serie serie);
    }

    public SerieAdapter(Context context,List<Serie> list, SerieAdapter.onSerieClickListener listener) {
        if(list == null){list = new ArrayList<>();}
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_serie,parent,false);
        return new SerieAdapter.ViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvTitle.setText("Set " + list.get(position).getNumSerie());
        holder.tvWeight.setText(String.valueOf(list.get(position).getWeight()) + " kg");
        holder.tvReps.setText(String.valueOf(list.get(position).getReps()) + " reps");
        holder.tvTypeSerie.setText(TypeSerie.getTypeString(context, list.get(position).getTypeSerie()));
        holder.tvIntensity.setText(list.get(position).getTypeIntensity() + " " + String.valueOf(list.get(position).getIntensity()));
        holder.tvTimeRest.setText(String.valueOf(list.get(position).getTimeRest()) + "s");
        if(list.get(position).isMarked()) {
            holder.star.setColorFilter(context.getResources().getColor(R.color.starColor), PorterDuff.Mode.SRC_ATOP);
        }
    }

    //Para actualizar los datos de la lista -->
    //Usamos el metodo  notifyDataSetChanged(), para uqe la vista se anule y se vuelva a dibujar
    public void updateData(List<Serie> list){
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void delete(Serie serie){
        list.remove(serie);
        notifyDataSetChanged();
    }

    public void add(Serie serie){
        list.add(serie);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvWeight;
        TextView tvReps;
        TextView tvIntensity;
        TextView tvTimeRest;
        TextView tvTypeSerie;
        ImageView star;

        public ViewHolder(@NonNull View itemView, final SerieAdapter.onSerieClickListener listener) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.num_serie);
            tvWeight = itemView.findViewById(R.id.tvWeight);
            tvReps = itemView.findViewById(R.id.tvReps);
            tvIntensity = itemView.findViewById(R.id.tvIntensity);
            tvTimeRest = itemView.findViewById(R.id.tvRestTime);
            tvTypeSerie = itemView.findViewById(R.id.typeSerie);
            star = itemView.findViewById(R.id.star);
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
