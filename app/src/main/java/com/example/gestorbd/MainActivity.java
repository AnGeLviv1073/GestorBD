package com.example.gestorbd;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import android.view.View;
import android.content.Intent;

import com.android.volley.*;
import com.android.volley.toolbox.*;

import org.json.*;

public class MainActivity extends AppCompatActivity {

    EditText etUsuario, etPassword, etBaseDatos;
    Button btnConectar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsuario = findViewById(R.id.etUsuario);
        etPassword = findViewById(R.id.etPassword);
        etBaseDatos = findViewById(R.id.etBaseDatos);
        btnConectar = findViewById(R.id.btnConectar);

        // ✅ Prellenar los campos para pruebas
        etUsuario.setText("admo");
        etPassword.setText("123456");
        etBaseDatos.setText("gestpro1");

        btnConectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuario = etUsuario.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String baseDatos = etBaseDatos.getText().toString().trim();

                if (usuario.isEmpty() || password.isEmpty() || baseDatos.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject json = new JSONObject();
                try {
                    json.put("usuario", usuario);
                    json.put("password", password);
                    json.put("database", baseDatos);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // ⚠️ Cambia esta IP si estás usando teléfono real

                 String URL = "http://192.168.1.65:5000/conectar"; // para dispositivo real

                JsonObjectRequest request = new JsonObjectRequest(
                        Request.Method.POST,
                        URL,
                        json,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                if (response.optString("status").equals("ok")) {
                                    Intent intent = new Intent(MainActivity.this, TablesActivity.class);
                                    intent.putExtra("tablas", response.optJSONArray("tablas").toString());
                                    intent.putExtra("usuario", usuario);
                                    intent.putExtra("password", password);
                                    intent.putExtra("database", baseDatos);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(MainActivity.this, "Error: " + response.optString("mensaje"), Toast.LENGTH_LONG).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, "Error de conexión: " + error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                );

                Volley.newRequestQueue(MainActivity.this).add(request);
            }
        });
    }
}
