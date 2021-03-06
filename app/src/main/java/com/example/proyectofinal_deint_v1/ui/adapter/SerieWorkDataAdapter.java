package com.example.proyectofinal_deint_v1.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.proyectofinal_deint_v1.R;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.serie.Serie;

import java.util.ArrayList;
import java.util.List;

public class SerieWorkDataAdapter extends ArrayAdapter<Serie> {
    // View lookup cache
    private static class ViewHolder {
        TextView name;
    }

    public SerieWorkDataAdapter(Context context, List<Serie> serieArrayList) {
        super(context, R.layout.serieworkdata_item , serieArrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Serie serie = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.serieworkdata_item, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.tvSerieString);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.name.setText(serie.toString());
        // Return the completed view to render on screen
        return convertView;
    }
}
