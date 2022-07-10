package com.example.administraciondashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_proveedor.*

class ActivityProveedor : AppCompatActivity() {
    var nota: Int =12
    var Ptxthost: EditText?=null     // Nombre
    var Ptxtuser: EditText?=null      // Token ID
    var Ptxtpass: EditText?=null    // Fecha
    var Ptxtmaquina: EditText?=null  // Maquina
    var Ptxtclient: EditText?=null      // Celular
    var Ptxtsoport: EditText?=null      // Celular
    var Ptxtclientmes: EditText?=null      // Celular
    var txtgenerado: TextView?=null      // Celular
    var Ptxtarch: TextView?=null      // Celular
    var Ptxtmaqcompradas: TextView?=null      // Celular
    var Pbtnsave: Button?=null      // Celular
    var Pbtnrestart: Button?=null      // Celular
    var pbtnBorrar: Button?=null      // Celular
    var f:String="0"
    var PID:String="0"
    var URL:String=""
    var IDproveedor:String=""

    var dia= 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proveedor)

        Ptxthost=findViewById(R.id.Ptxthost)      // Nombre
        Ptxtuser=findViewById(R.id.Ptxtuser)        // Token ID
        Ptxtpass=findViewById(R.id.Ptxtpass)    // Fecha
        Ptxtmaquina=findViewById(R.id.Ptxtmaquina)// Maquina
        Ptxtclient=findViewById(R.id.Ptxtclient)        // Cliente
        Ptxtsoport=findViewById(R.id.Ptxtsoport)        // Soportado
        Ptxtclientmes=findViewById(R.id.Ptxtclientmes)        // Clientes en el mes
        txtgenerado=findViewById(R.id.txtgenerado)        // Dinero o capital generado
        Ptxtarch=findViewById(R.id.Ptxtarch)        // Precio archivo
        Ptxtmaqcompradas=findViewById(R.id.Ptxtmaqcompradas)        // Precio archivo
        Pbtnsave=findViewById(R.id.Pbtnsave)        // Celular
        Pbtnrestart=findViewById(R.id.Pbtnrestart)        // Celular
        pbtnBorrar=findViewById(R.id.PbtnBorrar)        // Celular

        if(intent.extras !=null){
            if (intent.getStringExtra("proveedorid") !=null){
                Pbtngenerar.visibility = View.GONE
                IDproveedor=intent.getStringExtra("proveedorid").toString()
                Ptxthost?.setText(IDproveedor)
                clickRestaurarP(View(applicationContext))
            }
            URL = intent.getStringExtra("URL").toString()

        }

    }
    fun clickGuardarP(view: View) {
        //Primero Verifico si esta registrado el TOKEN ID

        val id =Ptxthost?.text.toString()
        val queue = Volley.newRequestQueue(this)
        val url = "http://$URL/api/usuarios.php?proveedorid=$id"
        val jsonObjectRequest= JsonObjectRequest(
            Request.Method.GET,url,null,
            { response ->
                //ESTA REGISTRADO

                val a = Ptxtclientmes?.text.toString().toInt() * Ptxtarch?.text.toString().toInt()
                txtgenerado?.text =a.toString()
                val url = "http://$URL/api/usuariosedit.php"
                val queue= Volley.newRequestQueue(this)
                //con este parametro aplico el metodo POST
                val resultadoPost = object : StringRequest(Method.POST,url,
                    Response.Listener<String> { response ->
                        Toast.makeText(this,"PROVEEDOR EDITADO!", Toast.LENGTH_SHORT).show()
                    }, Response.ErrorListener { error ->
                        Toast.makeText(this,"ERROR $error", Toast.LENGTH_SHORT).show()
                    }){
                    override fun getParams(): MutableMap<String, String>? {
                        val parametros = HashMap<String,String>()
                        // Key y value
                        parametros.put("proveedorid",Ptxthost?.text.toString())
                        parametros.put("usuario",Ptxtuser?.text.toString())
                        parametros.put("password",Ptxtpass?.text.toString())
                        parametros.put("cantmaquinas",Ptxtmaquina?.text.toString())
                        parametros.put("nclientes",Ptxtclient?.text.toString())
                        parametros.put("clientesop",Ptxtsoport?.text.toString())
                        parametros.put("nclientesmes",Ptxtclientmes?.text.toString())
                        parametros.put("precioarch",Ptxtarch?.text.toString())
                        parametros.put("totalgenerado",txtgenerado?.text.toString())
                        parametros.put("compra",Ptxtmaqcompradas?.text.toString())
                        return parametros






                    }
                }
                // con esto envio o SEND todo
                queue.add(resultadoPost)

            }, { error ->

                val a = Ptxtclientmes?.text.toString().toInt() * Ptxtarch?.text.toString().toInt()
                txtgenerado?.text =a.toString()
                //NO ESTA REGISTRADO
                val url = "http://$URL/api/usuarios.php"
                val queue= Volley.newRequestQueue(this)
                //con este parametro aplico el metodo POST
                var resultadoPost = object : StringRequest(Method.POST,url,
                    Response.Listener<String> { response ->
                        Toast.makeText(this,"PROVEEDOR REGISTRADO!", Toast.LENGTH_SHORT).show()
                    }, Response.ErrorListener { error ->
                        Toast.makeText(this,"ERROR $error", Toast.LENGTH_SHORT).show()
                    }){
                    override fun getParams(): MutableMap<String, String>? {
                        val parametros = HashMap<String,String>()
                        // Key y value
                        parametros.put("proveedorid",Ptxthost?.text.toString())
                        parametros.put("usuario",Ptxtuser?.text.toString())
                        parametros.put("password",Ptxtpass?.text.toString())
                        parametros.put("cantmaquinas",Ptxtmaquina?.text.toString())
                        parametros.put("nclientes",Ptxtclient?.text.toString())
                        parametros.put("clientesop",Ptxtsoport?.text.toString())
                        parametros.put("nclientesmes",Ptxtclientmes?.text.toString())
                        parametros.put("precioarch",Ptxtarch?.text.toString())
                        parametros.put("totalgenerado",a.toString())
                        parametros.put("compra",Ptxtmaqcompradas?.text.toString())

                        return parametros
                    }
                }
                // con esto envio o SEND todo
                queue.add(resultadoPost)


            }




        )
        queue.add(jsonObjectRequest)

        Ptxtmaqcompradas?.text = (Ptxtclientmes?.text.toString().toInt() * Ptxtarch?.text.toString().toInt()).toString()

    }
    fun clickRestaurarP(view: View){
        val queue = Volley.newRequestQueue(this)
        val url = "http://$URL/api/usuarios.php?proveedorid=$IDproveedor"
        val jsonObjectRequest= JsonObjectRequest(
            Request.Method.GET,url,null,
            { response ->
                Ptxtsoport?.setText(response.getString("clientesop"))
                Ptxtclient?.setText(response.getString("nclientes"))
                Ptxtuser?.setText(response.getString("usuario"))
                Ptxtpass?.setText(response.getString("password"))
                Ptxtmaquina?.setText(response.getString("cantmaquinas"))
                Ptxtclientmes?.setText(response.getString("nclientesmes"))
                Ptxtarch?.setText(response.getString("precioarch"))
                txtgenerado?.setText(response.getString("totalgenerado"))
            }, { error ->

            }
        )
        queue.add(jsonObjectRequest) }

    fun ClickBorrarP(view: View) {
        val id =Ptxthost?.text.toString()
        val url = "http://$URL/api/proveedor.php?proveedorid=$id"

        val queue = Volley.newRequestQueue(this)

        val resultadoDelete = object : StringRequest(
            Request.Method.DELETE,url,
            Response.Listener { response ->
                Pbtngenerar.visibility = View.VISIBLE

                Toast.makeText(this,"El usuario se borro de forma exitosa", Toast.LENGTH_LONG).show()
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"El usuario se borro de forma exitosa", Toast.LENGTH_LONG).show()
            }
        ){

        }
        queue.add(resultadoDelete)
    }

    fun ClickGenerarP(view: View) {


        val id =Ptxthost?.text.toString()
        val queue = Volley.newRequestQueue(this)
        val url = "http://$URL/api/usuarios.php?proveedorid=$id"
        val jsonObjectRequest= JsonObjectRequest(
            Request.Method.GET,url,null,
            { response ->
                Toast.makeText(this,"Este Proveedor ID Existe!", Toast.LENGTH_SHORT).show()

            }, { error ->

                //hay que darle un lapso de tiempo porque no podra hacerlo tarda demasiado en responder el srv



                        GenerarProveedorID()

                        val id2 =PID
                        val queue2 = Volley.newRequestQueue(this)
                        val url2 = "http://$URL/api/usuarios?proveedorid=$id2"
                        val jsonObjectRequest2= JsonObjectRequest(
                            Request.Method.GET,url2,null,
                            { response ->
                                //SI EXISTE ENTONCES DEBO GENERAR OTRO YA QUE NO LO ESTOY GUARDANDO POR ENDE ESTUVO AHI ANTES DE COMPROBARLO
                                Toast.makeText(this," Proveedor ID NO se pudo generar intenta con otro Usuario y Contraseña", Toast.LENGTH_SHORT).show()
                            }, { error ->

                                Toast.makeText(this," Proveedor ID! GENERADO EXITOSAMENTE", Toast.LENGTH_SHORT).show()


                                //COMO NO EXISTE ENTONCES YA ENCONTRE UN CODIGO ID QUE SEA UNICO ENTRE TODOS
                                /*DEBO GENERAR UN CODIGO UNICO PARA CADA PROVEEDOR COMO LO HAGO?*/
                            }
                        )
                        queue2.add(jsonObjectRequest2)

                        // Actualizamos la condicion


                    Ptxthost?.setText(PID)




                /*DEBO GENERAR UN CODIGO UNICO PARA CADA PROVEEDOR COMO LO HAGO?*/
            }
        )
        queue.add(jsonObjectRequest)



    }
    fun GenerarProveedorID(){

            f= Ptxtuser?.text.toString() +Ptxtpass?.text.toString()
            val c = f.toByteArray()
            PID= Integer.toHexString(c.hashCode())

    }




}