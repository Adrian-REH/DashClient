package com.example.administraciondashboard

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.administraciondashboard.Fragment.FragmentDashBoard
import kotlinx.android.synthetic.main.activity_refresh.*
import org.json.JSONException
import java.util.*

class ActivityRefresh : AppCompatActivity() {
    private val fragmentdashboard = FragmentDashBoard()
    private var EdtPA: TextView?=null
    private var EdtPP: TextView?=null

    private var TxtVDrop: AutoCompleteTextView?=null
    private var btnrfresh: Button?=null

    var sw1:Boolean=false
    var sw2:Boolean=false
    var URL:String?=null                //URL
    var CLAVE:String?=null                //URL
    var precioarchivo:Int=0
    var cantproveedores:Int=0

    val arraylis= ArrayList<String>() //LISTADO DE IDENTIFICACION DE PROVEEDORES
    val arraylisM= ArrayList<String>() //LISTADO DE IDENTIFICACION DE MAQUINAS
    val arraylisNCM= ArrayList<String>() //LISTADO DE CANTIDAD DE CLIENTES POR CADA MAQUINA
    val arraylisNCPM= ArrayList<String>() //LISTADO DE CANTIDAD DE CLIENTES POR CADA PROVEEDOR EN el MES
    val arraylisNCP= ArrayList<String>() //LISTADO DE CANTIDAD DE CLIENTES POR CADA PROVEEDOR
    val arraylisNMP= ArrayList<String>() //LISTADO DE CANTIDAD DE MAQUINAS POR CADA PROVEEDOR



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refresh)
        EdtPA=findViewById(R.id.EdtPA)
        EdtPP=findViewById(R.id.EdtPP)

        TxtVDrop=findViewById(R.id.TxtVDrop)
        btnrfresh=findViewById(R.id.btnrfresh)


        if(intent.extras !=null){
            URL = intent.getStringExtra("URL").toString()
            CLAVE = intent.getStringExtra("clave").toString()
        }

        BuscMidList()
        try {
            //Ponemos a "Dormir" el programa durante los ms que queremos
            Thread.sleep((500).toLong())
        } catch (e: Exception) {
            println(e)
        }
        BuscPidList()

        switch1.setOnCheckedChangeListener { compoundButton, b ->
            if (b){
                sw1=true

                NMaqProList()
                NCliMesProList()

            }else  if (!b){
                sw1=false
                arraylisNMP.clear()
                arraylisNCP.clear()
                arraylisNCPM.clear()
            }
        }

        switch2.setOnCheckedChangeListener { compoundButton, b ->
            if (b){
                sw2=true
                NCliMaqList()
            }else  if (!b){
                sw2=false
                arraylisNCM.clear()

            }
        }
        
        
        
        
        
    }
    fun ClickRefresh(view: View){

        if (sw1){
            GuardaNClNClMesNMaqProList()
            Toast.makeText(this,"Los datos de los proveedores se ordenaron con exito", Toast.LENGTH_SHORT).show()
        }else if(sw2){
            GuardaNCliMaqList()
            Toast.makeText(this,"Los datos de la maquina se ordenaron con exito", Toast.LENGTH_SHORT).show()
        }else if(sw2&&sw1){
            GuardaNCliMaqList()
            Toast.makeText(this,"Todos los datos se ordenaron con existo", Toast.LENGTH_SHORT).show()
        }

        arraylis.clear()
        arraylisM.clear()
        arraylisNCM.clear()
        arraylisNCPM.clear()
        arraylisNCP.clear()
        arraylisNMP.clear()
        finish()
    }

     fun BuscPidList(){
         //Busca los PROVEEDOR ID en la tabla USUARIOS, GET(PASAN A UNA LISTA)

         val queue = Volley.newRequestQueue(this)
         //1busco las identificaciones de proveedor
         //hago una lista con ellos para luego buscar sus datos uno a uno sin tener que acceder a internet todo el tiempo
         val urlP = "http://$URL/api/usuariosedit.php?clave=$CLAVE"
         var jsonObjectRequestP= JsonObjectRequest(
             Request.Method.GET,urlP,null,
             { response ->

                 var jsonArray = response.getJSONArray("data")
                 for (i in 0 until jsonArray.length()){
                     var jsonObject= jsonArray.getJSONObject(i)
                     val soportados = jsonObject.getString("proveedorid").toString()
                      precioarchivo = jsonObject.getString("precioarch").toString().toInt()
                     //busco la sumatoria del precio de archivos
                     precioarchivo += precioarchivo

                     arraylis.add(soportados) }

                 val arrayAdapter = ArrayAdapter(this,R.layout.list_item_drop,arraylis)
                 TxtVDrop?.setAdapter(arrayAdapter)
                 //valor del promedio de archivos

             }, { error ->
            Toast.makeText(this,"ERROR $error",Toast.LENGTH_SHORT).show()
             }
         )
         queue.add(jsonObjectRequestP)

         cantproveedores= TxtVDrop!!.length()


     }
    fun NCliMesProList(){




        //Busca los clientes en la table CLIENTES con el valor PROVEEDOR ID, GET(PASAN A UNA LISTA)
        val queueB = Volley.newRequestQueue(this)
        //DATOS  DE LA FECHA DEL MES QUE VIENE
        val c = Calendar.getInstance()
        val day2: Int= c.get(Calendar.DAY_OF_MONTH)
        var month2: Int= c.get(Calendar.MONTH)
        var year2: Int= c.get(Calendar.YEAR)
        var fecha:Int?=null
        //VERIFICO  QUE LA FECHA DEL MES QUE VIENE TENGA CIERTAS CONDICIONES PARA ESCRIBIRSE A LA VARIABLE QUE LUEGO COMPROBARE
        if (month2<10){
            fecha = ("$year2"+"0"+"$month2$day2").toInt()

        }else{
            fecha = ("$year2$month2$day2").toInt()
        }

        for (j in 0 until arraylis.size){

            //llamo al valor del arraylist ordenado en el valor j
            val idmc: String = arraylis[j]
            val url1 = "http://$URL/api/clientesedit.php?proveedorid=$idmc"
            val jsonObjectRequestB= JsonObjectRequest(
                Request.Method.GET,url1,null,
                { response ->
                    try{
                        val jsonArray = response.getJSONArray("data")

                        var nclim=0

                        for (i in 0 until jsonArray.length()){
                            val jsonObject= jsonArray.getJSONObject(i)
                            val Fecha= jsonObject.getString("Fecha").toString()

                            //COMPRUEBO QUE LA FECHA DEL MES QUE VIENE SEA MENOR A LA FECHA DEL CLIENTE PARA QUE PUEDA ASI SABER SI SE VENCIO O NO
                            if ( Fecha.toInt() > fecha){
                                nclim=1+ nclim
                            }
                        }


                        arraylisNCPM.add(nclim.toString())
                        arraylisNCP.add(jsonArray.length().toString())


                    }catch (e: JSONException){
                        e.printStackTrace()
                    }

                }, { error ->
                    arraylisNCP.add("0")
                    arraylisNCPM.add("0")
                }
            )

            queueB.add(jsonObjectRequestB)

            try {
                //Ponemos a "Dormir" el programa durante los ms que queremos
                Thread.sleep((2000).toLong())
            } catch (e: Exception) {
                println(e)
            }


        }
        val arrayAdapter = ArrayAdapter(this,R.layout.list_item_drop,arraylisNCP)
        TxtVDrop?.setAdapter(arrayAdapter)


    }
    fun NMaqProList(){
        //Busca las maquinas en la tabla MAQUINA con el valor PROVEEDOR ID, GET(PASAN A UNA LISTA)

        val queue = Volley.newRequestQueue(this)
        for (j in 0 until arraylis.size){
            val idmc: String = arraylis[j]
            val urlM = "http://$URL/api/maquinaedit.php?proveedorid=$idmc"
            var jsonObjectRequestM= JsonObjectRequest(
                Request.Method.GET,urlM,null,
                { response ->
                    var jsonArray = response.getJSONArray("data")
                    //agarro el valor del jsonArray que es el numero de clientes encontrados que tienen esa misma identificacion de maquina
                    arraylisNMP.add(jsonArray.length().toString())


                }, { error ->
                    arraylisNMP.add("0")
                }
            )
            queue.add(jsonObjectRequestM)
            try {
                //Ponemos a "Dormir" el programa durante los ms que queremos
                Thread.sleep((2000).toLong())
            } catch (e: Exception) {
                println(e)
            }
        }

        val arrayAdapter = ArrayAdapter(this,R.layout.list_item_drop,arraylisNMP)
        TxtVDrop?.setAdapter(arrayAdapter)

    }

    fun BuscMidList(){
        //Busca las MAQUINA ID en la tabla MAQUINA, GET(PASAN A UNA LISTA)

        val queue = Volley.newRequestQueue(this)
        //1busco las identificaciones de proveedor
        //hago una lista con ellos para luego buscar sus datos uno a uno sin tener que acceder a internet todo el tiempo
        val urlP = "http://$URL/api/maquinaedit.php?clave=$CLAVE"
        var jsonObjectRequestP= JsonObjectRequest(
            Request.Method.GET,urlP,null,
            { response ->

                var jsonArray = response.getJSONArray("data")
                for (i in 0 until jsonArray.length()){
                    var jsonObject= jsonArray.getJSONObject(i)
                    val listmaquinas = jsonObject.getString("maquinaid").toString()
                    arraylisM.add(listmaquinas) }


            }, { error ->
                Toast.makeText(this,"ERROR $error",Toast.LENGTH_SHORT).show()
            }
        )
        queue.add(jsonObjectRequestP)





    }
    fun NCliMaqList(){
        //Busca los clientes en la tabla CLIENTES con el valor MAQUINA ID, GET(PASAN A UNA LISTA)

        val queue = Volley.newRequestQueue(this)
        for (j in 0 until arraylisM.size){
            val idmc: String = arraylisM[j].toString()
            val urlM = "http://$URL/api/clientesedit.php?maquinaid=$idmc"
            var jsonObjectRequestM= JsonObjectRequest(
                Request.Method.GET,urlM,null,
                { response ->
                    var jsonArray = response.getJSONArray("data")
                    //agarro el valor del jsonArray que es el numero de clientes encontrados que tienen esa misma identificacion de maquina
                    val nclientesm =jsonArray.length()
                    arraylisNCM.add(nclientesm.toString())
                }, { error ->
                    Toast.makeText(this,"ERROR $error",Toast.LENGTH_SHORT).show()
                }
            )
            queue.add(jsonObjectRequestM)
            try {
                //Ponemos a "Dormir" el programa durante los ms que queremos
                Thread.sleep((2000).toLong())
            } catch (e: Exception) {
                println(e)
            }
        }

        val arrayAdapter = ArrayAdapter(this,R.layout.list_item_drop,arraylisNCM)
        TxtVDrop?.setAdapter(arrayAdapter)


    }

    fun GuardaNCliMaqList(){

        for (j in 0 until arraylisM.size){
            //llamo al valor del arraylist ordenado en el valor j
            val M: String = arraylisM[j]
            val NCM: String = arraylisNCM[j]


            val url = "http://$URL/api/maquinaeditvalor.php"
            val queue= Volley.newRequestQueue(this)
            //con este parametro aplico el metodo POST
            var resultadoPost = object : StringRequest(Method.POST,url,
                Response.Listener<String> { response ->
                }, Response.ErrorListener { error ->
                    Toast.makeText(this,"Error en la conexion", Toast.LENGTH_LONG).show()
                }){
                override fun getParams(): MutableMap<String, String>? {
                    val parametros = HashMap<String,String>()
                    // Key y value
                    parametros.put("canclientes",NCM)
                    parametros.put("maquinaid",M)

                    return parametros
                }
            }
            // con esto envio o SEND todo
            queue.add(resultadoPost)
            try {
                //Ponemos a "Dormir" el programa durante los ms que queremos
                Thread.sleep((2000).toLong())
            } catch (e: Exception) {
                println(e)
            }

        }


    }

    fun GuardaNClNClMesNMaqProList(){


        for (j in 0 until arraylis.size){
            //llamo al valor del arraylist ordenado en el valor j
            val P: String = arraylis[j]
            
            val NCPM: String = arraylisNCPM[j]
            
            val NCP: String = arraylisNCP[j]
            
            val NMP: String = arraylisNMP[j]


            val url = "http://$URL/api/usuarioseditvalor.php"
            val queue= Volley.newRequestQueue(this)
            //con este parametro aplico el metodo POST
            var resultadoPost = object : StringRequest(Method.POST,url,
                Response.Listener<String> { response ->
                }, Response.ErrorListener { error ->
                    Toast.makeText(this,"Error en la conexion", Toast.LENGTH_LONG).show()
                }){
                override fun getParams(): MutableMap<String, String>? {
                    val parametros = HashMap<String,String>()
                    // Key y value
                    parametros.put("proveedorid",P)

                    parametros.put("nclientesmes",NCPM)
                    
                    parametros.put("nclientes",NCP)

                    parametros.put("cantmaquinas",NMP)

                    return parametros
                }
            }
            // con esto envio o SEND todo
            queue.add(resultadoPost)
            try {
                //Ponemos a "Dormir" el programa durante los ms que queremos
                Thread.sleep((2000).toLong())
            } catch (e: Exception) {
                println(e)
            }

        }


    }



}