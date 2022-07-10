package com.example.administraciondashboard

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.administraciondashboard.Adapter.Maquina
import com.example.proveedordashboard.adaptadorMaquina
import kotlinx.android.synthetic.main.activity_cliente.*
import org.json.JSONException
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.util.*
import kotlin.collections.HashMap

class ActivityCliente : AppCompatActivity(),AdapterView.OnItemClickListener {

    var txtname: EditText?=null     // Nombre
    var txttid: EditText?=null      // Token ID
    var txtfecha: EditText?=null    // Fecha
    var txtmaquina: EditText?=null  // Maquina
    var txtcel: EditText?=null      // Celular
    var txtuser: EditText?=null      // Celular
    var txtpass: EditText?=null      // Celular
    var URL:String?=null                //URL
    var f:String="0"
    var PID:String?=null                //URL
    var SWTCH:Boolean=false                //URL

    val arraylis= ArrayList<String>() //LISTADO DE IDENTIFICACION DE PROVEEDORES
    val arraylisP= ArrayList<String>() //LISTADO DE IDENTIFICACION DE PROVEEDORES
    val arraylis1= ArrayList<String>() //LISTADO DE IDENTIFICACION DE PROVEEDORES
    val arraylis2= ArrayList<String>() //LISTADO DE IDENTIFICACION DE PROVEEDORES

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente)

        txtname=findViewById(R.id.txtname)      // Nombre
        txttid=findViewById(R.id.txttid)        // Token ID
        txtfecha=findViewById(R.id.txtfecha)    // Fecha
        txtmaquina=findViewById(R.id.txtmaquina)// Maquina
        txtcel=findViewById(R.id.txtcel)        // Celular
        txtuser=findViewById(R.id.txtuser)        // Celular
        txtpass=findViewById(R.id.txtpass)        // Celular

        if(intent.extras !=null){
            URL = intent.getStringExtra("URL").toString()

            if (intent.getStringExtra("tokenid") !=null){
                txttid?.setText(intent.getStringExtra("tokenid").toString())
                clickRestaurar(View(applicationContext))
            }else{
                BuscPidList()

            }

        }
        swtc.setOnCheckedChangeListener { buttonView, isChecked ->
                SWTCH=isChecked

        }

    }

    fun clickGuardar(view: View){
        //Primero Verifico si esta registrado el TOKEN ID

        val id =txttid?.text.toString()
        val queue = Volley.newRequestQueue(this)
        val url = "http://$URL/api/clientes.php?tokenid=$id"
        val jsonObjectRequest= JsonObjectRequest(
            Request.Method.GET,url,null,
            { response ->
                //ESTA REGISTRADO


                val url = "http://$URL/api/clientesedit.php"
                val queue= Volley.newRequestQueue(this)
                //con este parametro aplico el metodo POST
                var resultadoPost = object : StringRequest(Method.POST,url,
                    Response.Listener<String> { response ->
                        Toast.makeText(this,"CLIENTE EDITADO!", Toast.LENGTH_LONG).show()
                    }, Response.ErrorListener { error ->
                        Toast.makeText(this,"ERROR al editar $error", Toast.LENGTH_LONG).show()
                    }){
                    override fun getParams(): MutableMap<String, String>? {
                        val parametros = HashMap<String,String>()
                        // Key y value
                        parametros.put("nombre",txtname?.text.toString())
                        parametros.put("tokenid",txttid?.text.toString())
                        parametros.put("fecha",txtfecha?.text.toString())
                        parametros.put("telefono",txtcel?.text.toString())
                        parametros.put("password",txtpass?.text.toString())
                        parametros.put("usuario",txtuser?.text.toString())
                        parametros.put("host",txthost?.text.toString())
                        parametros.put("maquinaid",txtMtid?.text.toString())
                        parametros.put("proveedorid",txtPtid?.text.toString())
                        parametros.put("sipago",SWTCH.toString())
                        return parametros
                    }
                }
                // con esto envio o SEND todo
                queue.add(resultadoPost)






            }, { error ->

                //NO ESTA REGISTRADO
                val url = "http://$URL/api/clientes.php"
                val queue= Volley.newRequestQueue(this)
                //con este parametro aplico el metodo POST
                var resultadoPost = object : StringRequest(Method.POST,url,
                    Response.Listener<String> { response ->
                        Toast.makeText(this,"CLIENTE REGISTRADO!", Toast.LENGTH_LONG).show()
                    }, Response.ErrorListener { error ->
                        Toast.makeText(this,"ERROR al Registar $error", Toast.LENGTH_LONG).show()
                    }){
                    override fun getParams(): MutableMap<String, String>? {
                        val parametros = HashMap<String,String>()
                        // Key y value
                        parametros.put("nombre",txtname?.text.toString())
                        parametros.put("tokenid",txttid?.text.toString())
                        parametros.put("fecha",txtfecha?.text.toString())
                        parametros.put("nmaquina",txtmaquina?.text.toString())
                        parametros.put("telefono",txtcel?.text.toString())
                        parametros.put("password",txtpass?.text.toString())
                        parametros.put("usuario",txtuser?.text.toString())
                        parametros.put("host",txthost?.text.toString())
                        parametros.put("maquinaid",txtMtid?.text.toString())
                        parametros.put("proveedorid",txtPtid?.text.toString())
                        return parametros
                    }
                }
                // con esto envio o SEND todo
                queue.add(resultadoPost)


            }




        )
        queue.add(jsonObjectRequest)



    }

    fun BuscPidList(){
        //Busca los PROVEEDOR ID en la tabla USUARIOS, GET(PASAN A UNA LISTA)
        arraylis.clear()
        arraylisP.clear()
        val queue = Volley.newRequestQueue(this)
        //1busco las identificaciones de proveedor
        //hago una lista con ellos para luego buscar sus datos uno a uno sin tener que acceder a internet todo el tiempo
        val urlP = "http://$URL/api/usuariosedit.php?clave=dExterTable"
        var jsonObjectRequestP= JsonObjectRequest(
            Request.Method.GET,urlP,null,
            { response ->

                var jsonArray = response.getJSONArray("data")
                for (i in 0 until jsonArray.length()){
                    var jsonObject= jsonArray.getJSONObject(i)
                    val soportados = jsonObject.getString("proveedorid").toString()
                    val user = jsonObject.getString("usuario").toString()
                    //busco la sumatoria del precio de archivos

                    arraylis.add(soportados)
                    arraylisP.add(user)
                }

                val arrayAdapter = ArrayAdapter(this,R.layout.list_item_drop,arraylisP)
                with(txtPtid){
                    setAdapter(arrayAdapter)
                    onItemClickListener = this@ActivityCliente

                }
                //valor del promedio de archivos

            }, { error ->
                Toast.makeText(this,"ERROR $error",Toast.LENGTH_SHORT).show()
            }
        )
        queue.add(jsonObjectRequestP)



    }

    fun BuscMPList(search:String){
        arraylis1.clear()
        //DATOS DE LA LISTA MAQUINAS PROPIAS
        var queue = Volley.newRequestQueue(this)
        var url = "http://$URL/api/maquinaedit.php?clave=dExterTable"
        var jsonObjectRequest= JsonObjectRequest(
            Request.Method.GET,url,null,
            { response ->
                try{
                    var jsonArray = response.getJSONArray("data")

                    for (i in 0 until jsonArray.length()){

                        var jsonObject= jsonArray.getJSONObject(i)
                        val prid= jsonObject.getString("proveedorid").toString()
                        val maquinaid= jsonObject.getString("maquinaid").toString()
                        val host= jsonObject.getString("host").toString()
                        if (search==prid){
                            arraylis1.add(maquinaid)
                            arraylis2.add(host)

                        }
                    }

                    val arrayAdapter = ArrayAdapter(this,R.layout.list_item_drop,arraylis1)
                    with(txtMtid){
                        setAdapter(arrayAdapter)
                        onItemClickListener = this@ActivityCliente

                    }


                }catch (e: JSONException){
                    e.printStackTrace()
                }



            }, { error ->

                Toast.makeText(this,"ERROR $error", Toast.LENGTH_LONG).show()
            }
        )
        queue.add(jsonObjectRequest)


    }

    fun clickRestaurar(view: View){
        val id =txttid?.text.toString()
        val queue = Volley.newRequestQueue(this)
        val url = "http://$URL/api/clientes.php?tokenid=$id"
        val jsonObjectRequest= JsonObjectRequest(
            Request.Method.GET,url,null,
            { response ->
                BuscPidList()
                txtname?.setText(response.getString("nombre"))
                txtfecha?.setText(response.getString("Fecha"))
                txtcel?.setText(response.getString("telefono"))
                txtmaquina?.setText(response.getString("nmaquina"))
                txtuser?.setText(response.getString("usuario"))
                txtpass?.setText(response.getString("password"))
                txthost?.setText(response.getString("host"))
                txtMtid?.setText(response.getString("maquinaid"))
                txtPtid?.setText(response.getString("proveedorid"))
                swtc.isChecked=response.getString("sipago").toBoolean()
            }, { error ->
                Toast.makeText(this,error.toString(), Toast.LENGTH_LONG).show()
            }
        )
        queue.add(jsonObjectRequest)

    }


    fun ClickBorrar(view: View){
        val id =txttid?.text.toString()
        val url = "http://$URL/api/clientes.php?tokenid=$id"

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


    fun ClickComprobar(view: View){
        val id =txttid?.text.toString()
        val queue = Volley.newRequestQueue(this)
        val url = "http://$URL/api/clientes.php?tokenid=$id"
        val jsonObjectRequest= JsonObjectRequest(
            Request.Method.GET,url,null,
            { response ->
                Toast.makeText(this,"Este Token ID Existe!", Toast.LENGTH_LONG).show()
            }, { error ->
                GeneraTokenID()
                val id2 =PID
                val queue2 = Volley.newRequestQueue(this)
                val url2 = "http://$URL/api/clientes?tokenid=$id2"
                val jsonObjectRequest2= JsonObjectRequest(
                    Request.Method.GET,url2,null,
                    { response ->
                        //SI EXISTE ENTONCES DEBO GENERAR OTRO YA QUE NO LO ESTOY GUARDANDO POR ENDE ESTUVO AHI ANTES DE COMPROBARLO
                        Toast.makeText(this," Token ID NO se pudo generar intenta con otro Usuario y Contraseña", Toast.LENGTH_LONG).show()
                    }, { error ->
                        Toast.makeText(this," Token ID! GENERADO EXITOSAMENTE", Toast.LENGTH_LONG).show()
                        //COMO NO EXISTE ENTONCES YA ENCONTRE UN CODIGO ID QUE SEA UNICO ENTRE TODOS
                        /*DEBO GENERAR UN CODIGO UNICO PARA CADA PROVEEDOR COMO LO HAGO?*/
                    }
                )
                queue2.add(jsonObjectRequest2)

                // Actualizamos la condicion


                txttid?.setText(PID)



            }
        )
        queue.add(jsonObjectRequest)
    }

    fun GeneraTokenID(){
        f= txtuser?.text.toString() + txtpass?.text.toString()
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
            txtfecha?.setText("$fecha")

        }else if (month<10){
            var fecha:Int = ("$year"+"0"+"$month$day").toInt()
            txtfecha?.setText("$fecha")
            if(day<10){
                fecha = ("$year"+"0"+"$month"+"0"+"$day").toInt()
                txtfecha?.setText("$fecha")
            }
        } else if(day<10){
            var fecha = ("$year$month"+"0"+"$day").toInt()
            txtfecha?.setText("$fecha")
        }else{
            txtfecha?.setText(year+month+day)
        }


      //  val datepicker = DatePickerFragment{day,month,year ->onDateSelected(day,month,year)}
    //    datepicker.show(supportFragmentManager,"datepicker")

    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val item = parent?.getItemAtPosition(position).toString()
        for (i in 0 until  arraylis.size){
            if (item==arraylisP[i]){
                BuscMPList(arraylis[i])
                txtPtid.setText(arraylis[i])

            }
        }
        for (i in 0 until  arraylis1.size){
            if (item==arraylis1[i]){
                val arrayAdapter = ArrayAdapter(this,R.layout.list_item_drop,arraylis2)
                with(txthost){
                    setAdapter(arrayAdapter)
                }

            }
        }

    }
    //   fun onDateSelected(day:Int,month:Int, year:Int){
 //       txtfecha?.setText("$day/$month/$year")
 //   }
    fun ClickWsp(view: View){
        val a=txtcel?.text.toString()
        val b=txtname?.text.toString()
        // Creating intent with action send
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=+549$a&text=Hola%20%$b")))

    }
}