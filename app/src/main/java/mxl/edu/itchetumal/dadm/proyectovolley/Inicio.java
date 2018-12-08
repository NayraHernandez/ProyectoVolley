package mxl.edu.itchetumal.dadm.proyectovolley;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Inicio extends AppCompatActivity implements View.OnClickListener {

    Button btnConsumir, btnParsear;
    EditText editUrl, editRespuesta;
    String respuesta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        //Obtener componentes de la interfaz con los que se interactuará
        editUrl = (EditText) findViewById(R.id.editUrl);
        editRespuesta = (EditText) findViewById(R.id.editRespuesta);
        btnConsumir = (Button) findViewById(R.id.btnConsumir);
        btnParsear = (Button) findViewById(R.id.btnParsear);

        btnConsumir.setOnClickListener(this);
        btnParsear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnConsumir ) {
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = editUrl.getText().toString();

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            editRespuesta.setText("La respuesta es: " + response);
                            respuesta = response;
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            editRespuesta.setText("¡No funciona el WebService!");
                        }
                    });

            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }
        else { //btnParsear
            parsearJSon(respuesta);
        }

    }

    public void parsearJSon(String respuestaJSon){
        if (respuestaJSon != null) {
            try {

                JSONArray arregloJson = new JSONArray(respuestaJSon);
                editRespuesta.setText("");

                // looping through All Contacts
                for (int i = 0; i < arregloJson.length(); i++) {
                    JSONObject c = arregloJson.getJSONObject(i);
                    String codigo = c.getString("Código");
                    String nombre = c.getString("Nombre");
                    String direccion = c.getString("Dirección");
                    String foto = c.getString("Foto");

                    //¿Qué hacer con cada valor de los atributos?
                    editRespuesta.setText(editRespuesta.getText().toString() + "\nCódigo: " + codigo
                            + "\nnombre: " + nombre
                            + "\ndirección: " + direccion
                            + "\nfoto: " + foto + "\n");

                }
            } catch (final JSONException e) {
                Log.e("ParsearJSON", "Json parsing error: " + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Error al parsear Json: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

            }

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