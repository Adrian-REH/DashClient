package com.example.administraciondashboard.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.administraciondashboard.*
import com.example.administraciondashboard.Adapter.*
import com.example.administraciondashboard.databinding.FragmentAdministracionBinding

import com.example.proveedordashboard.Adapter.AdapterRecomendado
import com.example.proveedordashboard.adaptadorMaquina
import org.json.JSONException


class FragmentAdministracion : Fragment(), AdapterRecomendado.onClickListener,
    adaptadorMaquina.onMaquinaItemClick,AdapterProveedor.onProveedorItemClick {

    private  val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(this@FragmentAdministracion.requireContext(), R.anim.rotate_open_anim) }
    private  val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(this@FragmentAdministracion.requireContext(), R.anim.rotate_close_anim) }
    private  val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(this@FragmentAdministracion.requireContext(), R.anim.to_bottom_anim) }
    private  val toBottom: Animation by lazy { AnimationUtils.loadAnimation(this@FragmentAdministracion.requireContext(), R.anim.from_bottom_anim) }

    private  var  clicked = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val bind = FragmentAdministracionBinding.inflate(layoutInflater)



        //RECIBO LOS DATOS DEL ACTIVITY PRINCIPAL PARA GENERAR DATOS DE LA LISTA MAQUINAS

        val data = arguments
        bind.datoenviado2.text = data?.get("String").toString()
        val URL= data?.get("URL").toString()

        //Animacion de boton
        bind.btnadd.setOnClickListener {
            if (!clicked){
                bind.btnaddp.visibility = View.VISIBLE
                bind.btnmaq.visibility = View.VISIBLE
                bind.btnmv.visibility = View.VISIBLE
                bind.btnarch.visibility = View.VISIBLE


                bind.btnaddp.startAnimation(fromBottom)
                bind.btnmaq.startAnimation(fromBottom)
                bind.btnmv.startAnimation(fromBottom)
                bind.btnarch.startAnimation(fromBottom)
                bind.btnadd.startAnimation(rotateClose)
            }else{
                bind.btnaddp.visibility = View.INVISIBLE
                bind.btnmaq.visibility = View.INVISIBLE
                bind.btnmv.visibility = View.INVISIBLE
                bind.btnarch.visibility = View.INVISIBLE

                bind.btnadd.startAnimation(rotateOpen)
                bind.btnaddp.startAnimation(toBottom)
                bind.btnmaq.startAnimation(toBottom)
                bind.btnmv.startAnimation(toBottom)
                bind.btnarch.startAnimation(toBottom)

            }
            clicked = !clicked
        }



        bind.btnarch.setOnClickListener {

            val intent = Intent (this@FragmentAdministracion.requireContext(), FileActivity::class.java)

            intent.putExtra("URL",URL)

            startActivity(intent)
        }
        bind.btnmaq.setOnClickListener {

            val intent = Intent (this@FragmentAdministracion.requireContext(), ActivityMaquina::class.java)

            intent.putExtra("URL",URL)
            startActivity(intent)
        }
        bind.btnaddp.setOnClickListener {
            val intent = Intent (this@FragmentAdministracion.requireContext(), ActivityProveedor::class.java)

            intent.putExtra("URL",URL)
            startActivity(intent)
        }
        bind.btnmv.setOnClickListener {
            val intent = Intent (this@FragmentAdministracion.requireContext(), ActivityMaquinaVenta::class.java)

            intent.putExtra("URL",URL)
            startActivity(intent)
        }

        val arraylis1= ArrayList<Maquina>()
        val displayList= ArrayList<Maquina>()
        val arraylisr= ArrayList<MaquinaData>()
        val displayListr= ArrayList<MaquinaData>()
        val arraylisP= ArrayList<ListaProveedor>()
        val displayListP= ArrayList<ListaProveedor>()




        //DATOS DE LA LISTA PROVEEDORES
        val idd: String = bind.datoenviado2.text.toString()
        var queueP = Volley.newRequestQueue(this@FragmentAdministracion.requireContext())
        var urlP = "http://$URL/api/usuariosedit.php?clave=$idd"
        var jsonObjectRequestrP= JsonObjectRequest(
            Request.Method.GET,urlP,null,
            { response ->
                try{
                    var jsonArray = response.getJSONArray("data")

                    for (i in 0 until jsonArray.length()){

                        var jsonObject= jsonArray.getJSONObject(i)
                        val user= jsonObject.getString("usuario").toString()
                        val pass= jsonObject.getString("password").toString()
                        val provid= jsonObject.getString("proveedorid").toString()
                        val ncli= jsonObject.getString("nclientes").toString()
                        val csop= jsonObject.getString("clientesop").toString()
                        val nmq= jsonObject.getString("cantmaquinas").toString()
                        arraylisP.add(ListaProveedor(user,pass,provid,ncli,csop,nmq))
                    }
                    displayListP.addAll(arraylisP)
                    val adapterProveedor= AdapterProveedor(displayListP,this@FragmentAdministracion.requireContext(),this)

                    bind.RVRecomendado.layoutManager= LinearLayoutManager(this@FragmentAdministracion.requireContext(), LinearLayoutManager.HORIZONTAL,false)
                    bind.RVRecomendado.adapter= adapterProveedor



                }catch (e: JSONException){
                    e.printStackTrace()
                }



            }, { error ->

                Toast.makeText(this@FragmentAdministracion.requireContext(),"ERROR $error", Toast.LENGTH_LONG).show()
            }
        )
        queueP.add(jsonObjectRequestrP)




        //DATOS DE LA LISTA RECOMENDACIONES DE MAQUINA
        var queuer = Volley.newRequestQueue(this@FragmentAdministracion.requireContext())
        var urlr = "http://$URL/api/vmaquinaedit.php"
        var jsonObjectRequestr= JsonObjectRequest(
            Request.Method.GET,urlr,null,
            { response ->
                try{
                    var jsonArray = response.getJSONArray("data")

                    for (i in 0 until jsonArray.length()){

                        var jsonObject= jsonArray.getJSONObject(i)
                        val socket= jsonObject.getString("stock").toString()
                        val cpu= jsonObject.getString("cpu").toString()
                        val ram= jsonObject.getString("ram").toString()
                        val red= jsonObject.getString("red").toString()
                        val xporx= jsonObject.getString("xporx").toString()
                        val soport= jsonObject.getString("soporteaprox").toString()
                        val precio= jsonObject.getString("precio").toString()
                        arraylisr.add(MaquinaData(socket,cpu,ram,red,xporx,soport,precio))
                    }
                    displayListr.addAll(arraylisr)
                    val adapterRecomendado=AdapterRecomendado(displayListr,this@FragmentAdministracion.requireContext(),this)
                    bind.ADVMaquindas.setHasFixedSize(true)
                    bind.ADVMaquindas.layoutManager= LinearLayoutManager(this@FragmentAdministracion.requireContext(), LinearLayoutManager.HORIZONTAL,false)
                    val snapHelper: SnapHelper = LinearSnapHelper()
                    snapHelper.attachToRecyclerView(bind.ADVMaquindas)
                    bind.ADVMaquindas.adapter= adapterRecomendado



                }catch (e: JSONException){
                    e.printStackTrace()
                }



            }, { error ->

                Toast.makeText(this@FragmentAdministracion.requireContext(),"ERROR $error", Toast.LENGTH_LONG).show()
            }
        )
        queuer.add(jsonObjectRequestr)



        //DATOS DE LA LISTA MAQUINAS PROPIAS
        val id2: String = bind.datoenviado2.text.toString()
        var queue = Volley.newRequestQueue(this@FragmentAdministracion.requireContext())
        var url = "http://$URL/api/maquinaedit.php?clave=$id2"
        var jsonObjectRequest= JsonObjectRequest(
            Request.Method.GET,url,null,
            { response ->
                try{
                    var jsonArray = response.getJSONArray("data")

                    for (i in 0 until jsonArray.length()){

                        var jsonObject= jsonArray.getJSONObject(i)

                        val canclientes= jsonObject.getString("canclientes").toString()
                        val soportados= jsonObject.getString("soportados").toString()
                        val fecha= jsonObject.getString("fecha").toString()
                        val numero= jsonObject.getString("numero").toString()
                        val maquinaid= jsonObject.getString("maquinaid").toString()
                        arraylis1.add(Maquina(canclientes,soportados,fecha,numero,maquinaid))


                    }
                    displayList.addAll(arraylis1)
                    val AdaptadorMaquina=
                        adaptadorMaquina(displayList,this@FragmentAdministracion.requireContext(),this)
                    bind.ADMaquinas.layoutManager= LinearLayoutManager(this@FragmentAdministracion.requireContext(), LinearLayoutManager.HORIZONTAL,false)
                    bind.ADMaquinas.adapter= AdaptadorMaquina



                }catch (e: JSONException){
                    e.printStackTrace()
                }



            }, { error ->

                Toast.makeText(this@FragmentAdministracion.requireContext(),"ERROR $error", Toast.LENGTH_LONG).show()
            }
        )
        queue.add(jsonObjectRequest)










        return bind.root
    }

    override fun onItemClick(xporx: String) {
        val intent = Intent(this@FragmentAdministracion.requireContext(), ActivityMaquinaVenta::class.java)
        val data = arguments
        val URL= data?.get("URL").toString()
        intent.putExtra("URL",URL)
        intent.putExtra("xporx",xporx)

        startActivity(intent)
    }

    override fun onMaquinaItemClick(maquinaid: String) {
        val intent = Intent(this@FragmentAdministracion.requireContext(), ActivityMaquina::class.java)
        val data = arguments
        val URL= data?.get("URL").toString()
        intent.putExtra("maquinaid",maquinaid)
        intent.putExtra("URL",URL)
        startActivity(intent)
    }

    override fun onProveedorItemClick(proveedorid: String) {
        val intent = Intent(this@FragmentAdministracion.requireContext(), ActivityProveedor::class.java)
        val data = arguments
        val URL= data?.get("URL").toString()
        intent.putExtra("proveedorid",proveedorid)
        intent.putExtra("URL",URL)
        startActivity(intent)
    }

}