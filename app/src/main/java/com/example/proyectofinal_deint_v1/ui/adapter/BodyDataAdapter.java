package com.example.proyectofinal_deint_v1.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinal_deint_v1.R;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.bodyData.BodyData;

import java.util.ArrayList;
import java.util.List;

public class BodyDataAdapter extends RecyclerView.Adapter<BodyDataAdapter.ViewHolder> {

    private Context context;
    private List<BodyData> list;
    private onBodyDataClickListener listener;

    public interface onBodyDataClickListener extends View.OnClickListener{
        void onClick(BodyData bodyData);
        void onLongClick(BodyData bodyData);
    }

    public BodyDataAdapter(Context context, List<BodyData> list, onBodyDataClickListener listener) {
        if(list == null){list = new ArrayList<>();}
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.log_bodydata_item,parent,false);
        return new BodyDataAdapter.ViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvWeigth.setText(list.get(position).getWeight() == 0 ? "":String.valueOf(list.get(position).getWeight()) + "kg");
        holder.tvFatper.setText(list.get(position).getFatPer() == 0 ? "":String.valueOf(list.get(position).getFatPer()) + "%");
    }

    //Para actualizar los datos de la lista -->
    //Usamos el metodo  notifyDataSetChanged(), para uqe la vista se anule y se vuelva a dibujar
    public void updateData(List<BodyData> list){
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void delete(BodyData bodyData){
        list.remove(bodyData);
        notifyDataSetChanged();
    }

    public void add(BodyData bodyData){
        list.add(bodyData);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvWeigth;
        TextView tvFatper;

        public ViewHolder(@NonNull View itemView, final onBodyDataClickListener listener) {
            super(itemView);
            tvWeigth = itemView.findViewById(R.id.field_weight_it);
            tvFatper = itemView.findViewById(R.id.field_fatPer_it);
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
