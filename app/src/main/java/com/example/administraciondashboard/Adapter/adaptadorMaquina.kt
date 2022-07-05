package com.example.proveedordashboard

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.administraciondashboard.Adapter.Maquina
import com.example.administraciondashboard.R
import com.example.administraciondashboard.databinding.FragmentAdministracionBinding
import kotlinx.android.synthetic.main.list_itemmaq.view.*




class adaptadorMaquina(val arrayList: ArrayList<Maquina>, val context: Context, val itemClickListener: onMaquinaItemClick):
    RecyclerView.Adapter<adaptadorMaquina.ViewHolder>() {

    interface onMaquinaItemClick{
        fun onMaquinaItemClick(maquinaid: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v= LayoutInflater.from(parent.context).inflate(R.layout.list_itemmaq,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int = arrayList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bintItem(arrayList[position],context)
        holder.itemView.setOnClickListener {
            val model=arrayList.get(position)

            var ccli:String=model.canclientes
            var sop:String=model.soportados
            var fecha:String=model.fecha
            var nmqn:String=model.numero
            var nmqid:String=model.maquinaid



            val intent= Intent(context, FragmentAdministracionBinding::class.java)
            intent.putExtra("canclientes",ccli)
            intent.putExtra("soportados",sop)
            intent.putExtra("fecha",fecha)
            intent.putExtra("numero",nmqn)
            intent.putExtra("maquinaid",nmqid)


        }
    }
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bintItem(model: Maquina, context: Context){
            itemView.RLview.setOnClickListener { itemClickListener.onMaquinaItemClick(model.maquinaid) }
            itemView.txtclient.text=model.canclientes
            itemView.txtsop.text=model.soportados
            itemView.txtfechamaq.text=model.fecha
            itemView.txtmqidm.text=model.maquinaid



        }
    }



}