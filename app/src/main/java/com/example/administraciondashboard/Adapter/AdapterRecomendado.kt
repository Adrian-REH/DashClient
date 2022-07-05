package com.example.proveedordashboard.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.administraciondashboard.Adapter.MaquinaData
import com.example.administraciondashboard.R
import com.example.administraciondashboard.databinding.FragmentAdministracionBinding
import kotlinx.android.synthetic.main.list_itemrecom.view.*



class AdapterRecomendado (val arrayList: ArrayList<MaquinaData>,
                          val context: Context,
                          val itemClickListener: onClickListener
):
    RecyclerView.Adapter<AdapterRecomendado.ViewHolder>() {
    interface onClickListener{
        fun onItemClick(xporx: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v= LayoutInflater.from(parent.context).inflate(R.layout.list_itemrecom,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bintItem(arrayList[position],context,position)
        holder.itemView.setOnClickListener {
            val model=arrayList.get(position)

            var qsck:String=model.stock
            var qcpu:String=model.cpu
            var qram:String=model.ram
            var qred:String=model.red
            var qxx:String=model.xporx
            var qsopp:String=model.soport
            var qprecio:String=model.precio



            val intent= Intent(context, FragmentAdministracionBinding::class.java)
            intent.putExtra("stock",qsck)
            intent.putExtra("cpu",qcpu)
            intent.putExtra("ram",qram)
            intent.putExtra("red",qred)
            intent.putExtra("xporx",qxx)
            intent.putExtra("soporteaprox",qsopp)
            intent.putExtra("precio",qprecio)


        }
    }
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bintItem(model: MaquinaData, context: Context,position: Int){



            itemView.cardview.setOnClickListener { itemClickListener.onItemClick(model.xporx) }

            itemView.txtsock.text=model.stock
            itemView.txtcpu.text=model.cpu
            itemView.txtram.text=model.ram
            itemView.txtmbs.text=model.red
            itemView.txtcalidad.text=model.xporx
            itemView.txtprecio.text=model.precio
            itemView.txtcantidad.text=model.soport



        }
    }



}