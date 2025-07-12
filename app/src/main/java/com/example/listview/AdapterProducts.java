package com.example.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.listview.classes.Productos;
import com.google.android.material.chip.Chip;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class AdapterProducts extends BaseAdapter {

    private final Context context;
    private final ArrayList<Productos> productos;
    private final LayoutInflater inflater;

    static class ViewHolder {
        ShapeableImageView imageView;
        Chip category;
        TextView title;
        TextView price;
        TextView description;

        ViewHolder(View view) {
            imageView = view.findViewById(R.id.image);
            category = view.findViewById(R.id.category);
            title = view.findViewById(R.id.title);
            price = view.findViewById(R.id.price);
            description = view.findViewById(R.id.description);
        }
    }


    public AdapterProducts(Context context, ArrayList<Productos> productos) {
        this.context = context;
        this.productos = productos;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return productos.size();
    }

    @Override
    public Productos getItem(int position) {
        return productos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_producto, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        bindProductData(holder, getItem(position));
        return convertView;
    }

    private void bindProductData(ViewHolder holder, Productos producto) {
        Glide.with(context)
                .load(producto.getImage())
                .centerInside()
                .into(holder.imageView);

        holder.title.setText(producto.getTitle());
        holder.price.setText(String.format("$%.2f", producto.getPrice()));
        holder.description.setText(producto.getDescription());
        holder.category.setText(producto.getCategory());
    }
}
