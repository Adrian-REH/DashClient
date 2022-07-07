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
import org.json.JSONObject


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

        val arraylisCP= ArrayList<String>()
        val arraylisCT= ArrayList<String>()
        val arraylisC= ArrayList<String>()
        val arraylisD= ArrayList<String>()
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
                for (i in 0 until jsonArray.length()){
                    var JSONObject=jsonArray.getJSONObject(i)


                    if(JSONObject.getString("proveedorid").toString()=="propio"){
                        arraylisCP.add("$i")
                    }

                        arraylisCT.add("$i")


                }
                //NUMERO DE CLIENTES PROPIOS
                if (arraylisCP.size>0){
                    bind.txtclimaq.setText(arraylisCP.size.toString())
                    NCP =jsonArray.length()
                }else{
                    bind.txtclimaq.text = "0"

                }
                //NUMERO DE CLIENTES DE PROVEEDORES
                if (arraylisCT.size>0){
                    val numero=arraylisCT.size - arraylisCP.size
                    bind.txtncp.setText(numero.toString())
                    NCP =jsonArray.length()
                }else{
                    bind.txtncp.text = "0"

                }



            }, { error ->

            }

        )
        queue.add(jsonObjectRequest)




        //TAREA 3 BUSCO EL NUMERO DE MAQUINAS PROPIAS QUE TIENEN CERO CLIENTES
/*
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
*/

        arraylisCT.clear()
        arraylisCP.clear()
        bind.txtcsop.setText("")
        bind.txtmqt.setText("")
        bind.txtmqd.setText("")
        soport = 0


        //TAREA 4 BUSCO EL NUMERO DE MAQUINAS PROPIAS TRABAJANDO
        val urlMP = "http://$URL/api/maquinaedit.php?clave=$id2"
        val jsonObjectRequestMP= JsonObjectRequest(
            Request.Method.GET,urlMP,null,
            { response ->
                var jsonArray = response.getJSONArray("data")
                for (i in 0 until jsonArray.length()){
                    var JSONObject=jsonArray.getJSONObject(i)

                        if(JSONObject.getString("canclientes").toInt()>0){
                            arraylisC.add("$i")//Si hay clientes en la maquina
                        }else{
                            arraylisD.add("$i")//No hay clientes EN LA MAQUINA
                        }


                    val soportados2 = JSONObject.getString("soportados").toInt()
                    soport = soportados2 + soport
                }
                bind.txtcsop.text=  soport.toString()//Cantidad maxima de clientes que soporta

                bind.txtmqt.text =arraylisC.size.toString()//Cantidad de maquinas Desocupadas

                    bind.txtmqd.text =arraylisD.size.toString()//Cantidad de maquinas Ocupadas



                NMPT =  jsonArray.length()

            }, { error ->

            }
        )
        queue.add(jsonObjectRequestMP)



        //TAREA 5 BUSCO EL NUMERO DE MAQUINAS y SUMO LA CANTIDAD QUE SOPORTA








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
        bind.txtvalorpcm.setText("")
        soport4 = 0
        var contador1=0
        val urlPP = "http://$URL/api/usuariosedit.php?clave=$id2"
        var jsonObjectRequestPP= JsonObjectRequest(
            Request.Method.GET,urlPP,null,
            { response ->
                var jsonArray = response.getJSONArray("data")
                for (i in 0 until jsonArray.length()){
                    bind.txtmaqp.clearComposingText()
                    var jsonObject= jsonArray.getJSONObject(i)
                    val soportados2 = jsonObject.getString("totalgenerado").toInt()
                    soport4 = soportados2 + soport4
                    if(jsonObject.getString("proveedorid")=="propio"){

                        bind.txtvalorpcm.setText(jsonObject.getString("precioarch").toString())

                        contador1=  soportados2


                    }
                }
                bind.txtliquid.setText("$ $soport4")//TOTAL GENERADO
                bind.txtnp.setText("${jsonArray.length()}")//TOTAL GENERADO

                bind.txtmaqp.text=  "$ ${soport4-contador1}" //TOTAL GENERADO POR PROVEEDORES SOLAMENTE


            //    bind.txtprmp.text= (soport3/3).toString()
            }, { error ->

            }
        )
        queue.add(jsonObjectRequestPP)





        return bind.root
    }






}