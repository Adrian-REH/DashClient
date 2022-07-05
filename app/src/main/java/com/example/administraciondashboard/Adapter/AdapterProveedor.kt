package com.example.administraciondashboard.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.administraciondashboard.R
import com.example.administraciondashboard.databinding.FragmentAdministracionBinding
import kotlinx.android.synthetic.main.list_iremprov.view.*
import kotlinx.android.synthetic.main.list_itemmaq.view.*
import kotlinx.android.synthetic.main.list_itemmaq.view.RLview
import kotlinx.android.synthetic.main.list_itemmaq.view.txtsop

class AdapterProveedor(val arrayList: ArrayList<ListaProveedor>, val context: Context, val itemClickListener: onProveedorItemClick):
RecyclerView.Adapter<AdapterProveedor.ViewHolder>() {

    interface onProveedorItemClick{
        fun onProveedorItemClick(proveedorid: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v= LayoutInflater.from(parent.context).inflate(R.layout.list_iremprov,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int = arrayList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bintItem(arrayList[position],context)
        holder.itemView.setOnClickListener {
            val model=arrayList.get(position)

            var user:String=model.usuario
            var pas:String=model.password
            var proid:String=model.proveedorid
            var ncli:String=model.nclientes
            var ncsop:String=model.clientesop
            var nmq:String=model.cantmaquinas



            val intent= Intent(context, FragmentAdministracionBinding::class.java)
            intent.putExtra("usuario",user)
            intent.putExtra("password",pas)
            intent.putExtra("proveedorid",proid)
            intent.putExtra("nclientes",ncli)
            intent.putExtra("clientesop",ncsop)
            intent.putExtra("cantmaquinas",nmq)


        }
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bintItem(model: ListaProveedor, context: Context){
            itemView.RLview.setOnClickListener { itemClickListener.onProveedorItemClick(model.proveedorid) }
            itemView.txtClient.text =model.nclientes
            itemView.txtsop.text=model.clientesop
            itemView.txtuser.text=model.usuario
            itemView.txtpass.text=model.password
            itemView.txtmaq.text=model.cantmaquinas



        }
    }



}