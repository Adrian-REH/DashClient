package com.example.administraciondashboard.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.administraciondashboard.ActivityCliente
import com.example.administraciondashboard.ActivityRefresh
import com.example.administraciondashboard.Adapter.ListadeMaquinas
import com.example.administraciondashboard.Adapter.ListadeProveedores
import com.example.administraciondashboard.Adapter.Model
import com.example.administraciondashboard.databinding.FragmentDashBoardBinding

import kotlinx.android.synthetic.main.fragment_dash_board.*


class FragmentDashBoard : Fragment() {
    private var soport: Int=0
    private var soport2: Int=0
    private var soport3: Int=0
    private var soport4: Int=0
    private var NMPD: Int=0  //NUMERO DE MAQUINAS PROPIAS DESOCUPADAS
    private var NMPT: Int=0 //NUMERO DE MAQUINAS PROPIAS TRABAJANDO
    private var NP: Int=0  //NUMERO DE PROVEEDORES
    private var NCP: Int=0   //NUMERO DE CLIENTES PROPIOS

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val arraylis= ArrayList<ListadeProveedores>() //LISTADO DE IDENTIFICACION DE PROVEEDORES
        val arraylisM= ArrayList<ListadeMaquinas>()  //LISTADO DE IDENTIFICACION DE MAQUINAS
        val bind = FragmentDashBoardBinding.inflate(layoutInflater)


        val data = arguments
        bind.datoenviado.text = data?.get("String").toString()


        val URL= data?.get("URL").toString()
        bind.buttreg1.setOnClickListener {
            val intent = Intent (this@FragmentDashBoard.requireContext(), ActivityRefresh::class.java)

            intent.putExtra("URL",URL)
            intent.putExtra("clave",bind.datoenviado.text.toString())
            startActivity(intent)
        }


        //Me envian los datos de la actividad principal


        //Defino id2 para el dato de la actividad principal y queue para enviar los datos al finalizar la tarea
        val id2: String = bind.datoenviado.text.toString()
        val queue = Volley.newRequestQueue(this@FragmentDashBoard.requireContext())

        //TAREA 0 BUSCO EL NUMERO DE CLIENTES TOTALES
        val url1 = "http://$URL/api/clientesedit.php?clave=$id2"
        var jsonObjectRequest= JsonObjectRequest(
            Request.Method.GET,url1,null,
            { response ->
                var jsonArray = response.getJSONArray("data")
                bind.txtcli.setText(jsonArray.length().toString())

            }, { error ->

            }

        )
        queue.add(jsonObjectRequest)

        //TAREA 1 BUSCO EL NUMERO DE CLIENTES PROPIOS
        val urlCP = "http://$URL/api/clientesedit.php?proveedorid=propio"
        val jsonObjectRequestCP= JsonObjectRequest(
            Request.Method.GET,urlCP,null,
            { response ->
                var jsonArray = response.getJSONArray("data")
                bind.txtclimaq.setText(jsonArray.length().toString())
                NCP =jsonArray.length()
            }, { error ->
                bind.txtclimaq.text = "0"

            }
        )
        queue.add(jsonObjectRequestCP)

        //TAREA 2 BUSCO EL NUMERO DE CLIENTES NO PROPIOS
        val urlCNP = "http://$URL/api/clientesedit.php?clave=$id2&proveedorid=propio"
        val jsonObjectRequestCNP= JsonObjectRequest(
            Request.Method.GET,urlCNP,null,
            { response ->
                var jsonArray = response.getJSONArray("data")
                bind.txtncp.setText(jsonArray.length().toString())
                NCP =jsonArray.length()
            }, { error ->
                bind.txtncp.text = "0"

            }
        )
        queue.add(jsonObjectRequestCNP)



        //TAREA 3 BUSCO EL NUMERO DE MAQUINAS PROPIAS QUE TIENEN CERO CLIENTES
        val urlOM = "http://$URL/api/maquinaedit.php?canclientes=0&proveedorid=propio"
        val jsonObjectRequestOM= JsonObjectRequest(
            Request.Method.GET,urlOM,null,
            { response ->
                var jsonArray = response.getJSONArray("data")
                NMPD =jsonArray.length()
                bind.txtmqd.text =jsonArray.length().toString()

            }, { error ->
                bind.txtmqd.text =NMPD.toString()

            }
        )
        queue.add(jsonObjectRequestOM)



        //TAREA 4 BUSCO EL NUMERO DE MAQUINAS PROPIAS TRABAJANDO
        val urlMP = "http://$URL/api/maquinaedit.php?proveedorid=propio"
        val jsonObjectRequestMP= JsonObjectRequest(
            Request.Method.GET,urlMP,null,
            { response ->
                var jsonArray = response.getJSONArray("data")
                NMPT =  jsonArray.length()
                bind.txtmqt.text = NMPT.toString()
            }, { error ->

            }
        )
        queue.add(jsonObjectRequestMP)



        //TAREA 5 BUSCO EL NUMERO DE MAQUINAS y SUMO LA CANTIDAD QUE SOPORTA
        bind.txtcsop.setText("")
        soport = 0
        val url = "http://$URL/api/maquinaedit.php?clave=$id2"
        var jsonObjectRequest1= JsonObjectRequest(
            Request.Method.GET,url,null,
            { response ->
                var jsonArray = response.getJSONArray("data")
                for (i in 0 until jsonArray.length()){
                    bind.txtcsop.clearComposingText()
                    var jsonObject= jsonArray.getJSONObject(i)
                    val soportados2 = jsonObject.getString("soportados").toInt()
                    soport = soportados2 + soport
                }
                bind.txtcsop.text=  soport.toString()

            }, { error ->

            }
        )
        queue.add(jsonObjectRequest1)




/*
         Hago ecuaciones con los datos obtenido
         CLIENTES TOTALES POR MAQUINA? rta: sumatoria de todas los clientes de cada maquina propia
         CLIENTES TOTALES POR PROVEEDOR? rta: sumatoria de todas las NCP
         MAQUINAS TOTALES DE PROVEEDORES? rta: sumatoria de todos los NMP



 *//*

//
        //1busco las identificaciones de proveedor
            //hago una lista con ellos para luego buscar sus datos uno a uno sin tener que acceder a internet todo el tiempo
        val urlP = "http://192.168.4.100/api/usuariosedit?clave=$id2"
        var jsonObjectRequestP= JsonObjectRequest(
            Request.Method.GET,urlP,null,
            { response ->
                var jsonArray = response.getJSONArray("data")
                for (i in 0 until jsonArray.length()){
                    var jsonObject= jsonArray.getJSONObject(i)
                    val soportados = jsonObject.getString("proveedorid")
                    arraylis.add(ListadeProveedores(soportados))
                }
            }, { error ->

            }
        )
        queue.add(jsonObjectRequestP)


        //2busco el numero de identificacion de maquinas de cada proveedor

        for (j in 0 until arraylis.size){
            val idm: String = arraylis[j].toString()
            val urlM = "http://192.168.4.100/api/maquinaedit?proveedorid=$idm"
        var jsonObjectRequestM= JsonObjectRequest(
            Request.Method.GET,urlM,null,
            { response ->
                var jsonArray = response.getJSONArray("data")
                for (i in 0 until jsonArray.length()){

                    var jsonObject= jsonArray.getJSONObject(i)
                    val soportados = jsonObject.getString("maquinaid")
                    arraylisM.add(ListadeMaquinas(soportados))
                }
            }, { error ->

            }
        )
        queue.add(jsonObjectRequestM)
        }

            // hago un if porque necesisto saber cuantos clientes tiene cada maquina para asi saber cuales funcionan y cuales no

        //REPITO EL PROCESO HASTA COMPLETAR EL ARRAYLISTM
        for (j in 0 until arraylisM.size){
            //llamo al valor del arraylist ordenado en el valor j
            val idmc: String = arraylisM[j].toString()
            //defino la url con el valor asignado por el array asi se completa el pedido al enviar todo
            val urlM = "http://192.168.4.100/api/clientesedit?maquinaid=$idmc"
            var jsonObjectRequestM= JsonObjectRequest(
                Request.Method.GET,urlM,null,
                { response ->
                    var jsonArray = response.getJSONArray("data")
                    //agarro el valor del jsonArray que es el numero de clientes encontrados que tienen esa misma identificacion de maquina
                    NMP =jsonArray.length()
                    //ENVIO EL NUMERO DE CLIENTES A LA BASE DE DATOS CON EL NUMERO DE IDENTIFICACION DE MAQUINA
                    // defino la url de nuevo para un POST de actualizacion de datos
                    val urlMs = "http://192.168.4.100/api/maquinaedit"
                    var resultadoPost = object : StringRequest(Request.Method.POST,urlMs,
                        Response.Listener<String> { response ->
                            Toast.makeText(this@FragmentDashBoard.requireContext(),"CLIENTE EDITADO!",Toast.LENGTH_LONG).show()
                        }, Response.ErrorListener { error ->
                            Toast.makeText(this@FragmentDashBoard.requireContext(),"ERROR $error",Toast.LENGTH_LONG).show()
                        }){
                        //con esta funcion envio los parametros que es uno solo a canclientes con el valor NMP
                        override fun getParams(): MutableMap<String, String>? {
                            val parametros = HashMap<String,String>()
                            // Key y value
                            parametros.put("canclientes",NMP.toString())
                            parametros.put("maquinaid",idmc)
                            return parametros
                        }
                    }
                    // con esto envio o SEND todo
                    queue.add(resultadoPost)






                }, { error ->

                }
            )
            queue.add(jsonObjectRequestM)




        }












        //3 busco la cantidad de clientes con la identificacion del proveedor LISTO

        for (j in 0 until arraylis.size){
            val idp: String = arraylis[j].toString()

            val urlcP = "http://192.168.4.100/api/clientesedit?proveedorid=$idp"
            var jsonObjectRequestcP= JsonObjectRequest(
                Request.Method.GET,urlcP,null,
                { response ->
                    var jsonArray = response.getJSONArray("data")
                    NCPT += jsonArray.length()

                }, { error ->

                }
            )
            queue.add(jsonObjectRequestcP)



        }
        //guardo en el texto el numero total de clientes por proveedores
        bind.txtprom.text = NCPT.toString()


        //REPIRO HASTA ALCANZAR TODOS LOS PROVEEDORES y sumo la cantidad de clientes de cada proveedor






*/







        //TAREA 6 DEFINO EL CAPITAL GENERADO  PROPIO
        bind.txtlqm.setText("")
        soport2 = 0
        val urlPM = "http://$URL/api/usuariosedit.php?proveedorid=propio"
        var jsonObjectRequestPM= JsonObjectRequest(
            Request.Method.GET,urlPM,null,
            { response ->
                var jsonArray = response.getJSONArray("data")
                for (i in 0 until jsonArray.length()){
                    bind.txtlqm.clearComposingText()
                    var jsonObject= jsonArray.getJSONObject(i)
                    val soportados3 = jsonObject.getString("totalgenerado").toInt()
                    soport2 = soportados3 + soport2
                }
                bind.txtlqm.setText("$ $soport2")

            }, { error ->

            }
        )
        queue.add(jsonObjectRequestPM)



        //TAREA 7 DEFINO EL CAPITAL GENERADO  PROVEEDOR
        bind.txtmaqp.setText("")
        soport3 = 0
        val urlPP = "http://$URL/api/usuariosedit.php?clave=$id2&proveedorid=propio"
        var jsonObjectRequestPP= JsonObjectRequest(
            Request.Method.GET,urlPP,null,
            { response ->
                var jsonArray = response.getJSONArray("data")
                for (i in 0 until jsonArray.length()){
                    bind.txtmaqp.clearComposingText()
                    var jsonObject= jsonArray.getJSONObject(i)
                    val soportados2 = jsonObject.getString("totalgenerado").toInt()
                    soport3 = soportados2 + soport3
                }
                bind.txtmaqp.text=  "$ $soport3"
            //    bind.txtprmp.text= (soport3/3).toString()
            }, { error ->

            }
        )
        queue.add(jsonObjectRequestPP)




        //TAREA 8 DEFINO EL CAPITAL GENERADO
        bind.txtliquid.setText("")
        soport4 = 0
        val urlG= "http://$URL/api/usuariosedit.php?clave=$id2"
        var jsonObjectRequestG= JsonObjectRequest(
            Request.Method.GET,urlG,null,
            { response ->
                var jsonArray = response.getJSONArray("data")
                NP =jsonArray.length() -1
                bind.txtnp.text = NP.toString()
                for (i in 0 until jsonArray.length()){
                    bind.txtliquid.clearComposingText()
                    var jsonObject= jsonArray.getJSONObject(i)
                    val soportados2 = jsonObject.getString("totalgenerado").toInt()
                    soport4 = soportados2 + soport4

                }
                bind.txtliquid.setText("$ $soport4")

            }, { error ->

            }
        )
        queue.add(jsonObjectRequestG)


        //TAREA 9 PRECIO DEL ARCHIVO
        val urlA = "http://$URL/api/usuarios.php?proveedorid=propio"
        val jsonObjectRequestA= JsonObjectRequest(
            Request.Method.GET,urlA,null,
            { response ->
                bind.txtvalorpcm.setText(response.getString("precioarch"))
            }, { error ->

            }
        )
        queue.add(jsonObjectRequestA)




        return bind.root
    }






}