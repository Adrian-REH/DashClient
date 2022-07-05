package com.example.administraciondashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class ActivityMaquinaVenta : AppCompatActivity() {

    var nota: Int =12
    var Vtxtcpu: EditText?=null     // Nombre
    var Vtxtram: EditText?=null      // Token ID
    var Vtxtred: EditText?=null    // Fecha
    var Vtxtstk: EditText?=null  // Maquina
    var Vtxtsup: EditText?=null      // Celular
    var Vtxtxpx: EditText?=null      // Celular
    var Vtxtprecio: EditText?=null      // Celular
    var URL:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maquina_venta)


        Vtxtxpx=findViewById(R.id.Vtxtxpx)      // Nombre
        Vtxtcpu=findViewById(R.id.Vtxtcpu)      // Nombre
        Vtxtram=findViewById(R.id.Vtxtram)    // Fecha
        Vtxtred=findViewById(R.id.Vtxtred)// Maquina
        Vtxtstk=findViewById(R.id.Vtxtstk)        // Celular
        Vtxtsup=findViewById(R.id.Vtxtsup)        // Celular
        Vtxtprecio=findViewById(R.id.Vtxtprecio)        // Celular
        if(intent.extras !=null){

            URL = intent.getStringExtra("URL").toString()

            if (intent.getStringExtra("xporx") !=null){
                Vtxtxpx?.setText(intent.getStringExtra("xporx").toString())
                clickRestaurarV(View(applicationContext))
            }

        }

    }

    fun clickGuardarV(view: View){
        //Primero Verifico si esta registrado el TOKEN ID

        val id =Vtxtxpx?.text.toString()
        val queue = Volley.newRequestQueue(this)
        val url = "http://$URL/api/vmaquina.php?xporx=$id"
        val jsonObjectRequest= JsonObjectRequest(
            Request.Method.GET,url,null,
            { response ->
                //ESTA REGISTRADO


                val url = "http://$URL/api/vmaquinaedit.php"
                val queue= Volley.newRequestQueue(this)
                //con este parametro aplico el metodo POST
                var resultadoPost = object : StringRequest(Method.POST,url,
                    Response.Listener<String> { response ->
                        Toast.makeText(this,"CLIENTE EDITADO!", Toast.LENGTH_LONG).show()
                    }, Response.ErrorListener { error ->
                        Toast.makeText(this,"ERROR $error", Toast.LENGTH_LONG).show()
                    }){
                    override fun getParams(): MutableMap<String, String>? {
                        val parametros = HashMap<String,String>()
                        // Key y value
                        parametros.put("cpu",Vtxtcpu?.text.toString())
                        parametros.put("ram",Vtxtram?.text.toString())
                        parametros.put("red",Vtxtred?.text.toString())
                        parametros.put("stock",Vtxtstk?.text.toString())
                        parametros.put("soporteaprox",Vtxtsup?.text.toString())
                        parametros.put("xporx",Vtxtxpx?.text.toString())
                        parametros.put("precio",Vtxtprecio?.text.toString())
                        return parametros
                    }
                }
                // con esto envio o SEND todo
                queue.add(resultadoPost)

            }, { error ->

                //NO ESTA REGISTRADO
                val url = "http://$URL/api/vmaquina.php"
                val queue= Volley.newRequestQueue(this)
                //con este parametro aplico el metodo POST
                var resultadoPost = object : StringRequest(Method.POST,url,
                    Response.Listener<String> { response ->
                        Toast.makeText(this,"CLIENTE REGISTRADO!", Toast.LENGTH_LONG).show()
                    }, Response.ErrorListener { error ->
                        Toast.makeText(this,"ERROR $error", Toast.LENGTH_LONG).show()
                    }){
                    override fun getParams(): MutableMap<String, String>? {
                        val parametros = HashMap<String,String>()
                        // Key y value
                        parametros.put("cpu",Vtxtcpu?.text.toString())
                        parametros.put("ram",Vtxtram?.text.toString())
                        parametros.put("red",Vtxtred?.text.toString())
                        parametros.put("stock",Vtxtstk?.text.toString())
                        parametros.put("soporteaprox",Vtxtsup?.text.toString())
                        parametros.put("xporx",Vtxtxpx?.text.toString())
                        parametros.put("precio",Vtxtprecio?.text.toString())
                        return parametros
                    }
                }
                // con esto envio o SEND todo
                queue.add(resultadoPost)


            }




        )
        queue.add(jsonObjectRequest)



    }
    fun clickRestaurarV(view: View){
        val id =Vtxtxpx?.text.toString()
        val queue = Volley.newRequestQueue(this)
        val url = "http://$URL/api/vmaquina.php?xporx=$id"
        val jsonObjectRequest= JsonObjectRequest(
            Request.Method.GET,url,null,
            { response ->
                Vtxtcpu?.setText(response.getString("cpu"))
                Vtxtram?.setText(response.getString("ram"))
                Vtxtred?.setText(response.getString("red"))
                Vtxtstk?.setText(response.getString("stock"))
                Vtxtsup?.setText(response.getString("soporteaprox"))
                Vtxtprecio?.setText(response.getString("precio"))
            }, { error ->
                Toast.makeText(this,"ERROR $error", Toast.LENGTH_LONG).show()
            }
        )
        queue.add(jsonObjectRequest)

    }
    fun ClickBorrarV(view: View){
        val id =Vtxtxpx?.text.toString()
        val url = "http://$URL/api/vmaquina.php?xporx=$id"

        val queue = Volley.newRequestQueue(this)

        var resultadoDelete = object : StringRequest(
            Request.Method.DELETE,url,
            Response.Listener { response ->
                Toast.makeText(this,"El usuario se borro de forma exitosa", Toast.LENGTH_LONG).show()
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"El usuario se borro de forma exitosa", Toast.LENGTH_LONG).show()
            }
        ){

        }
        queue.add(resultadoDelete)
    }


}