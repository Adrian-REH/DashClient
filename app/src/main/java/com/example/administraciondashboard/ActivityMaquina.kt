package com.example.administraciondashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.util.*
import kotlin.collections.HashMap

class ActivityMaquina : AppCompatActivity() {


    var Mtxtc: TextView?=null         // EL HOST DE LA MAQUINA
    var txtMaqData: TextView?=null         // EL HOST DE LA MAQUINA
    var Mtxtfecha: TextView?=null         // NUMERO DE MAQUINA ASIGNADO POR EL PROVEEDOR
    var Mtxthost: TextView?=null    // NUMERO DE CLIENTES QUE OCUPAN LA MAQUINA
    var Mtxtpas: TextView?=null         // CONTRASEÑA
    var Mtxtsup: TextView?=null       // PRESIO QUE LE ASGINO A CADA MAQUINA DE FORMA CALCULADA
    var Mtxtuser: TextView?=null         // USUARIO
    var txtPData: TextView?=null         // Txt PROVEEDOR ID
    var Mbtnsave: Button?=null         // BOTON PARA GUARDAR
    var Mbtnrestart: Button?=null         // BOTON PARA RESTAURAR
    var Mbtnhoy: Button?=null         // BOTON PARA FECHA DE HOY
    var Mbtngenerar: Button?=null         // BOTON PARA GENERAR
    var MbtnBorrar: Button?=null         // BOTON PARA BORRAR
    var f:String="0"
    var PID:String="0"
    var URL:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maquina)

        Mtxtc = findViewById(R.id.Mtxtc)        // FECHA TEXTO
        txtMaqData = findViewById(R.id.txtMaqData)        // FECHA TEXTO
        Mtxtfecha=findViewById(R.id.Mtxtfecha)  // FECHA RESTANTE TEXTO
        Mtxthost=findViewById(R.id.Mtxthost)        // MAQUINA ID
        Mtxtpas=findViewById(R.id.Mtxtpas)            // EL HOST DE LA MAQUINA
        Mtxtsup=findViewById(R.id.Mtxtsup)            // CLIENTES SOPORTADOS
        Mtxtuser=findViewById(R.id.Mtxtuser)            // NUMERO DE MAQUINA ASIGNADO POR EL PROVEEDOR
        txtPData=findViewById(R.id.txtPData)        // Txt PROVEEDOR ID
        MbtnBorrar=findViewById(R.id.MbtnBorrar)        // BOTON PARA VENDER
        Mbtngenerar=findViewById(R.id.Mbtngenerar)        // BOTON PARA VENDER
        Mbtnhoy=findViewById(R.id.Mbtnhoy)        // BOTON PARA VENDER
        Mbtnrestart=findViewById(R.id.Mbtnrestart)        // BOTON PARA VENDER
        Mbtnsave=findViewById(R.id.Mbtnsave)        // BOTON PARA VENDER


        if(intent.extras !=null){

            URL = intent.getStringExtra("URL").toString()
            if (intent.getStringExtra("maquinaid") !=null){
                txtMaqData?.setText(intent.getStringExtra("maquinaid").toString())
                clickRestaurarM(View(applicationContext))
            }

        }

    }


    fun clickGuardarM(view: View){
        //Primero Verifico si esta registrada la MAQUINA ID
if(txtMaqData?.text.toString()!=""){
    val id =txtMaqData?.text.toString()
    val queue = Volley.newRequestQueue(this)
    val url = "http://$URL/api/maquina.php?maquinaid=$id"
    val jsonObjectRequest= JsonObjectRequest(
        Request.Method.GET,url,null,
        { response ->
            //ESTA REGISTRADO


            val url = "http://$URL/api/maquinaedit.php"
            val queue= Volley.newRequestQueue(this)
            //con este parametro aplico el metodo POST
            var resultadoPost = object : StringRequest(Method.POST,url,
                Response.Listener<String> { response ->
                    Toast.makeText(this,"MAQUINA EDITADA!", Toast.LENGTH_LONG).show()
                }, Response.ErrorListener { error ->
                    Toast.makeText(this,"Error en la conexion", Toast.LENGTH_LONG).show()
                }){
                override fun getParams(): MutableMap<String, String>? {
                    val parametros = HashMap<String,String>()
                    // Key y value

                    parametros.put("canclientes",Mtxtc?.text.toString())
                    parametros.put("fecha",Mtxtfecha?.text.toString())
                    parametros.put("soportados",Mtxtsup?.text.toString())
                    parametros.put("maquinaid",txtMaqData?.text.toString())
                    parametros.put("host",Mtxthost?.text.toString())
                    parametros.put("password",Mtxtpas?.text.toString())
                    parametros.put("usuario",Mtxtuser?.text.toString())
                    parametros.put("proveedorid",txtPData?.text.toString())

                    return parametros
                }
            }
            // con esto envio o SEND todo
            queue.add(resultadoPost)

        }, { error ->

            //NO ESTA REGISTRADO
            val url = "http://$URL/api/maquina.php"
            val queue= Volley.newRequestQueue(this)
            //con este parametro aplico el metodo POST
            var resultadoPost = object : StringRequest(Method.POST,url,
                Response.Listener<String> { response ->
                    Toast.makeText(this,"MAQUINA REGISTRADA CON EXITO!", Toast.LENGTH_LONG).show()
                }, Response.ErrorListener { error ->
                    Toast.makeText(this,"Error en la conexion", Toast.LENGTH_LONG).show()
                }){
                override fun getParams(): MutableMap<String, String>? {
                    val parametros = HashMap<String,String>()
                    // Key y value
                    parametros.put("canclientes",Mtxtc?.text.toString())
                    parametros.put("fecha",Mtxtfecha?.text.toString())
                    parametros.put("soportados",Mtxtsup?.text.toString())
                    parametros.put("maquinaid",txtMaqData?.text.toString())
                    parametros.put("host",Mtxthost?.text.toString())
                    parametros.put("password",Mtxtpas?.text.toString())
                    parametros.put("usuario",Mtxtuser?.text.toString())
                    parametros.put("proveedorid",txtPData?.text.toString())
                    return parametros
                }
            }
            // con esto envio o SEND todo
            queue.add(resultadoPost)


        }
    )
    queue.add(jsonObjectRequest)


}else{Toast.makeText(this,"Por favor ingrese una Maquina ID o Genere una nueva",Toast.LENGTH_SHORT).show()}


    }

















    fun clickRestaurarM(view: View){
        val id =txtMaqData?.text.toString()
        val queue = Volley.newRequestQueue(this)
        val url = "http://$URL/api/maquina.php?maquinaid=$id"
        val jsonObjectRequest= JsonObjectRequest(
            Request.Method.GET,url,null,
            { response ->
                Mtxtc?.setText(response.getString("canclientes"))
                Mtxtfecha?.setText(response.getString("fecha"))
                Mtxtsup?.setText(response.getString("soportados"))
                txtMaqData?.setText(response.getString("maquinaid"))
                Mtxthost?.setText(response.getString("host"))
                Mtxtpas?.setText(response.getString("password"))
                Mtxtuser?.setText(response.getString("usuario"))
                txtPData?.setText(response.getString("proveedorid"))
            }, { error ->
                Toast.makeText(this,error.toString(), Toast.LENGTH_LONG).show()
            }
        )
        queue.add(jsonObjectRequest) }

    fun ClickBorrarM(view: View){
        val id =txtMaqData?.text.toString()
        val url = "http://$URL/api/maquina.php?maquinaid=$id"

        val queue = Volley.newRequestQueue(this)

        var resultadoDelete = object : StringRequest(
            Request.Method.DELETE,url,
            Response.Listener { response ->
                Toast.makeText(this,"La Maquina se borro de forma exitosa", Toast.LENGTH_LONG).show()
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"La Maquina se borro de forma exitosa", Toast.LENGTH_LONG).show()
            }
        ){

        }
        queue.add(resultadoDelete)
    }

    fun ClickGenerarM(view: View){


        val id =txtMaqData?.text.toString()
        val queue = Volley.newRequestQueue(this)
        val url = "http://$URL/api/maquina?maquinaid=$id"
        val jsonObjectRequest= JsonObjectRequest(
            Request.Method.GET,url,null,
            { response ->
                Toast.makeText(this,"Este Maquina ID Existe!", Toast.LENGTH_LONG).show()

            }, { error ->

                var dia= 0
//hay que darle un lapso de tiempo porque no podra hacerlo tarda demasiado en responder el srv


                    GenerarMaquinaID()
                    var id2 =PID
                    val queue2 = Volley.newRequestQueue(this)
                    val url2 = "http://$URL/api/maquina.php?maquinaid=$id2"
                    val jsonObjectRequest2= JsonObjectRequest(
                        Request.Method.GET,url2,null,
                        { response ->
                            //SI EXISTE ENTONCES DEBO GENERAR OTRO YA QUE NO LO ESTOY GUARDANDO POR ENDE ESTUVO AHI ANTES DE COMPROBARLO
                            GenerarMaquinaID()
                        }, { error ->


                            Toast.makeText(this," Maquina ID! GENERADO EXITOSAMENTE $dia", Toast.LENGTH_LONG).show()


                            //COMO NO EXISTE ENTONCES YA ENCONTRE UN CODIGO ID QUE SEA UNICO ENTRE TODOS
                            /*DEBO GENERAR UN CODIGO UNICO PARA CADA PROVEEDOR COMO LO HAGO?*/
                        }
                    )
                    queue2.add(jsonObjectRequest2)
                    // Actualizamos la condicion



                txtMaqData?.setText(PID)




                /*DEBO GENERAR UN CODIGO UNICO PARA CADA PROVEEDOR COMO LO HAGO?*/
            }
        )
        queue.add(jsonObjectRequest)



    }
    fun GenerarMaquinaID(){
        f= Mtxthost?.text.toString()
        val c = f.toByteArray()
        PID= Integer.toHexString(c.hashCode())

    }
    fun ClickHora(view: View){
        val c = Calendar.getInstance()
        val day: Int= c.get(Calendar.DAY_OF_MONTH)
        var month: Int= c.get(Calendar.MONTH) + 1
        var year: Int= c.get(Calendar.YEAR)
        if(month.equals(13)){
            month = 1
            year += 1

            var fecha:Int = ("$year"+"0"+"$month$day").toInt()
            Mtxtfecha?.setText("$fecha")
        }else if (month<10){
            var fecha:Int = ("$year"+"0"+"$month$day").toInt()
            Mtxtfecha?.setText("$fecha")
        }else{
            Mtxtfecha?.setText("$year$month$day")
        }


        //  val datepicker = DatePickerFragment{day,month,year ->onDateSelected(day,month,year)}
        //    datepicker.show(supportFragmentManager,"datepicker")

    }


}