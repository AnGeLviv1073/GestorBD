package com.example.gestorbd;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TablaAdapter extends RecyclerView.Adapter<TablaAdapter.ViewHolder> {

    private List<String> tablas;
    private Context context;
    private String usuario, password, database;

    public TablaAdapter(Context context, List<String> tablas, String usuario, String password, String database) {
        this.context = context;
        this.tablas = tablas;
        this.usuario = usuario;
        this.password = password;
        this.database = database;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tabla, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String nombre = tablas.get(position);
        holder.txtTabla.setText(nombre);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, TableDataActivity.class);
            intent.putExtra("tabla", nombre);
            intent.putExtra("usuario", usuario);
            intent.putExtra("password", password);
            intent.putExtra("database", database);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return tablas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTabla;
        public ViewHolder(View itemView) {
            super(itemView);
            txtTabla = itemView.findViewById(R.id.txtTabla);
        }
    }
}
