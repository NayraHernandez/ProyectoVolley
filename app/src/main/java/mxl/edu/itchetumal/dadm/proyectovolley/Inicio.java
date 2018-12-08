package mxl.edu.itchetumal.dadm.proyectovolley;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Inicio extends AppCompatActivity implements View.OnClickListener {
    private TextView editRespuesta;
    private RequestQueue mQueue;

    Button btnConsumir;
    EditText editUrl;
    String respuestaServidor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        editRespuesta = findViewById(R.id.editRespuesta);
        mQueue = Volley.newRequestQueue(this);
        editUrl = (EditText) findViewById(R.id.editUrl);

        btnConsumir = (Button) findViewById(R.id.btnConsumir);
        Button buttonParse = findViewById(R.id.btnParsear);

        btnConsumir.setOnClickListener(this);
        buttonParse.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnConsumir) {
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = editUrl.getText().toString();

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            editRespuesta.setText("La respuesta es: " + response + "\n");
                            respuestaServidor = response;
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            editRespuesta.setText("¡No funciona el WebService!");
                        }
                    });
            queue.add(stringRequest);
        } else {
            jsonParse(respuestaServidor);
        }
    }

    private void jsonParse(String url) {
        url = editUrl.getText().toString();

        if (respuestaServidor != null) {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray jsonArray = response.getJSONArray("places");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject c = jsonArray.getJSONObject(i);

                                    String codigo = c.getString("Código");
                                    String nombre = c.getString("Nombre");
                                    String direccion = c.getString("Dirección");
                                    String foto = c.getString("Foto 1");

                                    editRespuesta.append("\nCódigo: " + codigo
                                            + "\nnombre: " + nombre
                                            + "\ndirección: " + direccion
                                            + "\nfoto 1: " + foto + "\n");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });

            mQueue.add(request);
        } else {
            Log.e("ParsearJSON", "No se obtuvo respuesta JSON del servidor");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Error al solicitar JSON del servidor. Revise el LogCat por posibles errores!",
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}