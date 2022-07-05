package com.example.administraciondashboard.Adapter

class Model(val nombre:String,val Fecha:String,val telefono:String,val nmaquina:String,val tokenid:String) {
}
class Maquina(val canclientes:String,val soportados:String,val fecha:String,val numero:String,val maquinaid:String) {
}
class MaquinaData(val stock:String,val cpu:String,val ram:String,val red:String,val xporx:String,val soport:String,val precio:String) {
}
class ListadeProveedores(val proveedorid:String) {
}
class ListadeNClientesP(val nclip: String) {
}
class ListadeNClientesM(val nclim: String) {
}
class ListadeNMaquinasP(val nmaqp: String) {
}
class ListadeMaquinas(val maquinaid: String) {
}
class ListaProveedor(val usuario: String,val password: String,val proveedorid: String,val nclientes: String,val clientesop: String,val cantmaquinas: String,) {
}
class FECHA(val fecha: String) {
}