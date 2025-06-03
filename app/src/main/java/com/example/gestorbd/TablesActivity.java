package com.example.gestorbd;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.List;

public class TablesActivity extends AppCompatActivity {

    RecyclerView recyclerTablas;
    Button btnSalir, btnConsultar;
    EditText etConsulta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tables);

        recyclerTablas = findViewById(R.id.recyclerTablas);
        btnSalir = findViewById(R.id.btnSalir);
        btnConsultar = findViewById(R.id.btnConsultar);
        etConsulta = findViewById(R.id.etConsulta);

        // Obtener datos del Intent
        String tablasJson = getIntent().getStringExtra("tablas");
        String usuario = getIntent().getStringExtra("usuario");
        String password = getIntent().getStringExtra("password");
        String database = getIntent().getStringExtra("database");

        List<String> tablas = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(tablasJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                tablas.add(jsonArray.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Configurar RecyclerView
        recyclerTablas.setLayoutManager(new LinearLayoutManager(this));
        recyclerTablas.setAdapter(new TablaAdapter(this, tablas, usuario, password, database));

        // Acción del botón "Salir"
        btnSalir.setOnClickListener(v -> finish());

        // Acción del botón "Consultar"
        btnConsultar.setOnClickListener(v -> {
            String consulta = etConsulta.getText().toString().trim();

            if (consulta.isEmpty()) {
                Toast.makeText(this, "Escribe una consulta SQL", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validación básica para evitar consultas peligrosas
            if (!consulta.toLowerCase().startsWith("select")) {
                Toast.makeText(this, "Solo se permiten consultas SELECT", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(TablesActivity.this, TableDataActivity.class);
            intent.putExtra("consulta", consulta);
            intent.putExtra("usuario", usuario);
            intent.putExtra("password", password);
            intent.putExtra("database", database);
            startActivity(intent);
        });
    }
}
