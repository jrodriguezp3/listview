package com.example.listview;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.listview.classes.Productos;
import com.example.listview.classes.VistaProducto;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private ExecutorService executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        executor = Executors.newSingleThreadExecutor();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Productos producto = (Productos) parent.getItemAtPosition(position);
                    if (producto != null) {
                        Gson gson = new Gson();
                        String productoJson = gson.toJson(producto);

                        Intent intent = new Intent(MainActivity.this, VistaProducto.class);
                        intent.putExtra("producto", productoJson);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Error al abrir el producto", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cargarProductos();

    }

    private void cargarProductos() {
        executor.execute(() -> {
            try {
                String response = HttpRequest.getRequest("https://fakestoreapi.com/products");
                mainHandler.post(() -> processResponse(response));
            } catch (Exception e) {
                mainHandler.post(() ->
                        Toast.makeText(MainActivity.this, "Error al obtener los productos", Toast.LENGTH_SHORT).show()
                );
            }
        });
    }

    private void processResponse(String response) {
        try {
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Productos>>() {
            }.getType();
            ArrayList<Productos> productos = gson.fromJson(response, listType);

            if (productos != null && !productos.isEmpty()) {
                AdapterProducts adapterProducts = new AdapterProducts(MainActivity.this, productos);
                listView.setAdapter(adapterProducts);
            } else {
                Toast.makeText(MainActivity.this, "No hay productos disponibles", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Error al procesar los productos", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }
}