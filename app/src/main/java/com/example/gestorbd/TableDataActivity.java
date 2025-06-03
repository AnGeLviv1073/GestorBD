package com.example.gestorbd;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.*;
import com.android.volley.toolbox.*;

import org.json.*;

import java.util.ArrayList;
import java.util.List;

public class TableDataActivity extends AppCompatActivity {

    ListView listaDatos;
    Button btnRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_data);

        listaDatos = findViewById(R.id.listaDatos);
        btnRegresar = findViewById(R.id.btnRegresar);

        String consulta = getIntent().getStringExtra("consulta");
        String usuario = getIntent().getStringExtra("usuario");
        String password = getIntent().getStringExtra("password");
        String database = getIntent().getStringExtra("database");

        JSONObject json = new JSONObject();
        try {
            json.put("consulta", consulta);
            json.put("usuario", usuario);
            json.put("password", password);
            json.put("database", database);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String URL = "http://192.168.1.65:5000/consulta";  // Asegúrate que esta ruta coincida con tu API Flask

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                json,
                response -> {
                    List<String> datos = new ArrayList<>();
                    JSONArray filas = response.optJSONArray("filas");
                    if (filas != null) {
                        for (int i = 0; i < filas.length(); i++) {
                            datos.add(filas.optJSONArray(i).toString());
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, datos);
                    listaDatos.setAdapter(adapter);
                },
                error -> Toast.makeText(this, "Error al cargar datos: " + error.toString(), Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(request);

        btnRegresar.setOnClickListener(v -> {
            Intent intent = new Intent(TableDataActivity.this, TablesActivity.class);
            intent.putExtra("usuario", usuario);
            intent.putExtra("password", password);
            intent.putExtra("database", database);
            startActivity(intent);
            finish();
        });
    }
}
