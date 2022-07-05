@file:Suppress("DEPRECATION")

package com.example.administraciondashboard

import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.graphics.toColor
import androidx.fragment.app.Fragment
import com.example.administraciondashboard.Fragment.FragmentAdministracion
import com.example.administraciondashboard.Fragment.FragmentClientes
import com.example.administraciondashboard.Fragment.FragmentDashBoard
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val fragmentadmin = FragmentAdministracion() //ADMINISTRACION
    private val fragmentclientes = FragmentClientes()        //CLIENTES
    private val fragmentdashboard = FragmentDashBoard()        //DASHBOARD
    private var idproveedor: TextView?=null                     //TEXTO DEL ID DEL PROVEEDOR
    public var valor: Int?=null                     //TEXTO DEL ID DEL PROVEEDOR
    var URL:String?=null      // Celular
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_AdministracionDashboard)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        //LLAMO AL TEXTO
        idproveedor=findViewById(R.id.idproveedor)
        replaceFragment(fragmentdashboard)

        //CUANDO DOY CLICK AL BOTON DE NAVEGACION ACCIONO
        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_dashboard -> {
                    replaceFragment(fragmentdashboard)

                    return@setOnNavigationItemSelectedListener true
                }
                R.id.ic_person -> {replaceFragment(fragmentclientes)
                    return@setOnNavigationItemSelectedListener true}
                R.id.ic_maquina -> {replaceFragment(fragmentadmin)
                    return@setOnNavigationItemSelectedListener true}
            }
            false
        }

        //ENVIO DATOS A LOS FRAGMENT
        idproveedor?.setText("dExterTable").toString()

       URL= "23herrera.xyz:81"
       // URL= "192.168.3.100"
        //ENVIO DATOS AL FRAGMENT
        val Bundle = Bundle()
        Bundle.putString("String",idproveedor?.text.toString())
        Bundle.putString("URL",URL.toString())
        fragmentclientes.arguments= Bundle
        fragmentadmin.arguments= Bundle
        fragmentdashboard.arguments= Bundle

    }





    private fun replaceFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }



}