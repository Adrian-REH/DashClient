package com.example.administraciondashboard.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.administraciondashboard.ActivityCliente
import com.example.administraciondashboard.ActivityDatosProveedor
import com.example.administraciondashboard.Adapter.Model
import com.example.administraciondashboard.databinding.FragmentClientesBinding

import com.example.proveedordashboard.Adapter.AdaptadorCustom
import com.example.proveedordashboard.Adapter.AdaptadorCustom.onClienteItemClick
import com.example.proveedordashboard.Adapter.AdapterRecomendado
import org.json.JSONException
import java.util.*
import kotlin.collections.ArrayList

class FragmentClientes : Fragment(), onClienteItemClick{

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bind = FragmentClientesBinding.inflate(layoutInflater)

        val arraylis= ArrayList<Model>()
        val displayList= ArrayList<Model>()


        //RECIBO LOS DATOS DEL ACTIVITY PRINCIPAL PARA GENERAR LA LISTA
        val data = arguments
        bind.datoenviado.text = data?.get("String").toString()
        val id2: String = bind.datoenviado.text.toString()
        val URL= data?.get("URL").toString()


        bind.buttreg.setOnClickListener {
            val intent = Intent (this@FragmentClientes.requireContext(), ActivityCliente::class.java)
            intent.putExtra("URL",URL)
            startActivity(intent)
        }


        //GENERA LA LISTA
        val queue = Volley.newRequestQueue(this@FragmentClientes.requireContext())
        val url = "http://$URL/api/clientesedit.php?clave=$id2"
        val jsonObjectRequest= JsonObjectRequest(
            Request.Method.GET,url,null,
            { response ->
                try{
                    val jsonArray = response.getJSONArray("data")
                    for (i in 0 until jsonArray.length()){
                        val jsonObject= jsonArray.getJSONObject(i)
                        val personaName= jsonObject.getString("nombre").toString()
                        val maqN= jsonObject.getString("nmaquina").toString()
                        val tid= jsonObject.getString("tokenid").toString()
                        val Fecha= jsonObject.getString("Fecha").toString()
                        val cel= jsonObject.getString("telefono").toString()
                        arraylis.add(Model(personaName,Fecha,cel,maqN,tid)) }

                    displayList.addAll(arraylis)
                    val AdaptadorCustom= AdaptadorCustom(displayList,this@FragmentClientes.requireContext(),this)
                    bind.recyclerView.layoutManager= LinearLayoutManager(this@FragmentClientes.requireContext())
                    bind.recyclerView.adapter=AdaptadorCustom



                }catch (e: JSONException){ e.printStackTrace() }



            }, {error -> }
        )
        queue.add(jsonObjectRequest)


        //BUSCA EN LA LISTA
        bind.btnSearch.setOnClickListener {

            val id = bind.txtSearch.text.toString()
            val queueB = Volley.newRequestQueue(this@FragmentClientes.requireContext())
            val url1 = "http://$URL/api/clientesedit.php?tokenid=$id"
            val jsonObjectRequestB= JsonObjectRequest(
                Request.Method.GET,url1,null,
                { response ->
                    arraylis.clear()
                    displayList.clear()
                    try{
                        val jsonArray = response.getJSONArray("data")
                        for (i in 0 until jsonArray.length()){
                            val jsonObject= jsonArray.getJSONObject(i)
                            val personaName= jsonObject.getString("nombre").toString()
                            val maqN= jsonObject.getString("nmaquina").toString()
                            val tid= jsonObject.getString("tokenid").toString()
                            val Fecha= jsonObject.getString("Fecha").toString()
                            val cel= jsonObject.getString("telefono").toString()
                            arraylis.add(Model(personaName,Fecha,cel,maqN,tid))




                        }
                        displayList.addAll(arraylis)
                        val AdaptadorCustom= AdaptadorCustom(displayList,this@FragmentClientes.requireContext(),this)
                        bind.recyclerView.layoutManager= LinearLayoutManager(this@FragmentClientes.requireContext())
                        bind.recyclerView.adapter=AdaptadorCustom

                    }catch (e: JSONException){
                        e.printStackTrace()
                    }



                }, { error ->

                }
            )
            queueB.add(jsonObjectRequestB)



        }
         bind.btnvenc.setOnClickListener {

            val queueB = Volley.newRequestQueue(this@FragmentClientes.requireContext())
            val url1 = "http://$URL/api/clientesedit.php?clave=dExterTable"
            val jsonObjectRequestB= JsonObjectRequest(
                Request.Method.GET,url1,null,
                { response ->
                    arraylis.clear()
                    displayList.clear()
                    try{
                        val jsonArray = response.getJSONArray("data")
                        //DATOS  DE LA FECHA DEL MES QUE VIENE
                        val c = Calendar.getInstance()
                        val day2: Int= c.get(Calendar.DAY_OF_MONTH)
                        var month2: Int= c.get(Calendar.MONTH)
                        var year2: Int= c.get(Calendar.YEAR)
                        var fecha:Int?=null
                        //VERIFICO  QUE LA FECHA DEL MES QUE VIENE TENGA CIERTAS CONDICIONES PARA ESCRIBIRSE A LA VARIABLE QUE LUEGO COMPROBARE
                        if (month2<10){
                            fecha = ("$year2"+"0"+"$month2$day2").toInt()
                            if(day2<10){
                                fecha = ("$year2"+"0"+"$month2"+"0"+"$day2").toInt()
                            }
                        } else if(day2<10){
                            fecha = ("$year2$month2"+"0"+"$day2").toInt()
                        }else{
                            fecha = ("$year2$month2$day2").toInt()
                        }

                        for (i in 0 until jsonArray.length()){
                            val jsonObject= jsonArray.getJSONObject(i)
                            val personaName= jsonObject.getString("nombre").toString()
                            val maqN= jsonObject.getString("nmaquina").toString()
                            val tid= jsonObject.getString("tokenid").toString()
                            val Fecha= jsonObject.getString("Fecha").toString()
                            val cel= jsonObject.getString("telefono").toString()

                            //COMPRUEBO QUE LA FECHA DEL MES QUE VIENE SEA MENOR A LA FECHA DEL CLIENTE PARA QUE PUEDA ASI SABER SI SE VENCIO O NO
                            if ( Fecha.toInt() > fecha){
                                arraylis.add(Model(personaName,Fecha,cel,maqN,tid))
                            }

                        }
                        displayList.addAll(arraylis)
                        val AdaptadorCustom= AdaptadorCustom(displayList,this@FragmentClientes.requireContext(),this)
                        bind.recyclerView.layoutManager= LinearLayoutManager(this@FragmentClientes.requireContext())
                        bind.recyclerView.adapter=AdaptadorCustom

                    }catch (e: JSONException){
                        e.printStackTrace()
                    }



                }, { error ->

                }
            )
            queueB.add(jsonObjectRequestB)



        }








        return bind.root

    }

    override fun onMaquinaItemClick(tokenid: String) {
        val intent = Intent(this@FragmentClientes.requireContext(), ActivityCliente::class.java)
        val data = arguments
        val URL= data?.get("URL").toString()
        intent.putExtra("tokenid",tokenid)
        intent.putExtra("URL",URL)
        startActivity(intent)
    }


}