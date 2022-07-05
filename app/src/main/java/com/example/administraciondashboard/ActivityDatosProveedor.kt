package com.example.administraciondashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class ActivityDatosProveedor : AppCompatActivity() {

    var Dtxtpro: TextView?=null      // MAQUINA ID
    var Dtxtarch: TextView?=null         // EL HOST DE LA MAQUINA
    var Dtxtc: TextView?=null         // CLIENTES SOPORTADOS
    var Dtxtnmaq: TextView?=null         // NUMERO DE MAQUINA ASIGNADO POR EL PROVEEDOR
    var Dtxtcmes: TextView?=null    // NUMERO DE CLIENTES QUE OCUPAN LA MAQUINA
    var txtGnc: TextView?=null         // CONTRASEÑA
    var URL:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datos_proveedor)

        Dtxtpro=findViewById(R.id.Dtxtpro)        // MAQUINA ID
        Dtxtc=findViewById(R.id.Dtxtc)        // MAQUINA ID
        Dtxtnmaq=findViewById(R.id.Dtxtnmaq)        // MAQUINA ID
        Dtxtcmes=findViewById(R.id.Dtxtcmes)        // MAQUINA ID
        Dtxtarch=findViewById(R.id.Dtxtarch)        // MAQUINA ID
        txtGnc=findViewById(R.id.txtGnc)        // MAQUINA ID

        if(intent.extras !=null){
            Dtxtpro?.setText(intent.getStringExtra("proveedorid").toString())
            URL = intent.getStringExtra("URL").toString()
            clickRestaurarV()
        }
    }


    fun clickRestaurarV(){
        val id =Dtxtpro?.text.toString()
        val queue = Volley.newRequestQueue(this)
        val url = "http://$URL/api/usuarios.php?proveedorid=$id"
        val jsonObjectRequest= JsonObjectRequest(
            Request.Method.GET,url,null,
            { response ->
                Dtxtpro?.setText(response.getString("proveedorid"))
                Dtxtc?.setText(response.getString("nclientes"))
                Dtxtnmaq?.setText(response.getString("cantmaquinas"))
                Dtxtcmes?.setText(response.getString("nclientesmes"))
                Dtxtarch?.setText(response.getString("precioarch"))
                txtGnc?.setText(response.getString("totalgenerado"))
            }, { error ->
                Toast.makeText(this,error.toString(), Toast.LENGTH_LONG).show()
            }
        )
        queue.add(jsonObjectRequest)

    }
}