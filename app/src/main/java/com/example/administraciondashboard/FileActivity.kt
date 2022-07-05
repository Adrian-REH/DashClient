package com.example.administraciondashboard

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.*
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.io.IOException

class FileActivity : AppCompatActivity() {

    private lateinit var edtxt: EditText
    private lateinit var webView: WebView
    private lateinit var swp: SwipeRefreshLayout
    private val SELECT_ACTIVITY=50
    var a:Int=0
    lateinit var downloadListener: DownloadListener
    var writeAccess = true
    var readAccess = true
    lateinit var context: Context
    lateinit var activity: Activity
    private var imageData: ByteArray? = null
    //private val postURL: String = "https://ptsv2.com/t/pn4n0-1645790098/post" // remember to use your own api

    private var postURL: String? = null
    private var BASE_URL: String? = null
    private val PERMISSION_REQUEST_CODE = 1234
    private var md5user: String? = null
    private var md5pass: String? = null
    private var URL: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file)


        edtxt=findViewById(R.id.edtxt)
        webView=findViewById(R.id.webv)
        swp=findViewById(R.id.swp)
        if(intent.extras !=null){

            URL = intent.getStringExtra("URL").toString()

        }
        md5user = "Servers123"
        md5pass = "Servers123"
        BASE_URL = "http://$URL/srvclay/index.php"
        postURL = "http://$URL/srvclay/acceso.php"


        webView.webChromeClient=object : WebChromeClient(){
        }

        //Refresh
        swp.setOnRefreshListener {
            webView.reload()
        }


        //Web
        context = applicationContext
        activity = this
        /** Chequea los permisos de Escritura y Lectura */
        checkReadAccess()
        checkWriteAccess()

        /** Crea una escucha de Descarga */
        createDownloadListener()
        /** Crea un display de descarga completada*/
        onDownloadComplete()
        /** Configura la vista del WebView */
        configureWebView()
    }
    fun clickSubir(view: View){
        if (edtxt.text.toString()!="" && a==1){
            uploadImage()
        }else if(edtxt.text.toString()==""){
            Toast.makeText(this,"Escribe el nombre del archivo a Subir", Toast.LENGTH_SHORT).show()

        }else if( a==0){
            Toast.makeText(this,"Debes seleccionar el archivo a Subir", Toast.LENGTH_SHORT).show()
        }
        webView.reload()


    }
    fun clickSelect(view: View){
        ArchivoController.selectArchivo(this,SELECT_ACTIVITY)
        a=1
    }

    fun clickBorrar(view: View){
        if (edtxt.text.toString()!=""){
            deleteArchivo()
        }else{
            Toast.makeText(this,"Primero escribe el nombre del archivo a borrar", Toast.LENGTH_SHORT).show()

        }
        webView.reload()

    }

    private fun uploadImage() {
        imageData?: return
        val request = object : VolleyFileUploadRequest(
            Method.POST,
            postURL!!,
            Response.Listener {
                Toast.makeText(this,"PERFECTAMENTE ENVIADO", Toast.LENGTH_SHORT).show()
            },
            Response.ErrorListener {

                Toast.makeText(this,"ERROR $it", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String>? {
                val parametros = HashMap<String,String>()
                // Key y value
                parametros.put("usuario",md5user!!)
                parametros.put("password",md5pass!!)
                return parametros

            }
            override fun getByteData(): MutableMap<String, FileDataPart> {
                var params = HashMap<String, FileDataPart>()
                params["docs"] = FileDataPart(edtxt.text.toString(), imageData!!, "file")

                return params
            }



        }
        Volley.newRequestQueue(this).add(request)

    }
    private fun deleteArchivo(){
        val queue= Volley.newRequestQueue(this)
        //con este parametro aplico el metodo POST
        var resultadoPost = object : StringRequest(Method.POST,postURL,
            Response.Listener<String> { response ->
                webView.reload()
                Toast.makeText(this,"ARCHIVO BORRADO!", Toast.LENGTH_LONG).show()
            }, Response.ErrorListener { error ->
                Toast.makeText(this,"ERROR $error", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val parametros = HashMap<String,String>()
                // Key y value
                parametros.put("usuario",md5user!!)
                parametros.put("password",md5pass!!)
                parametros.put("borrar",edtxt?.text.toString())
                return parametros

            }
        }
        // con esto envio
        queue.add(resultadoPost)


    }

    private fun onDownloadComplete()
    {
        /**  Code that receives and handles broadcast intents sent by Context.sendBroadcast(Intent) */
        val onComplete = object : BroadcastReceiver() {
            override fun onReceive(ctxt: Context, intent: Intent) {
                Toast.makeText(context,"File Downloaded", Toast.LENGTH_LONG).show()
            }
        }

        /** Register to receives above broadcast */
        registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }
    @SuppressLint("SetJavaScriptEnabled")
    private fun configureWebView()
    {
        webView.webViewClient=object : WebViewClient(){
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                swp.isRefreshing =true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                swp.isRefreshing=false
            }
        }
        webView.settings.javaScriptEnabled = true

        webView.loadUrl(BASE_URL!!)

        webView.setDownloadListener(downloadListener)
    }
    private inner class MyWebViewClient : WebViewClient() {

        /**
         * Override to open URL in WebView
         * */
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }

        /**
         * Override to open URL in WebView
         * */
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            view.loadUrl(request.url.toString())
            return true
        }
    }
    private fun createDownloadListener()
    {


        /** A New Download Listener for our WebView */
        downloadListener = DownloadListener { url, userAgent, contentDescription, mimetype, contentLength ->

            val request = DownloadManager.Request(Uri.parse(url))

            request.allowScanningByMediaScanner()

            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

            val fileName = URLUtil.guessFileName(url, contentDescription, mimetype)


            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)


            val dManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager


            if(!writeAccess && !readAccess)
                dManager.enqueue(request)
            else
            {
                Toast.makeText(context,"Unable to download file. Required Privileges are not available.",
                    Toast.LENGTH_LONG).show()
                checkWriteAccess()
                checkReadAccess()
            }

        }
    }

    private fun checkWriteAccess()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            /**
             * Check for permission status.
             * */
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                {
                    val builder = AlertDialog.Builder(activity)
                    builder.setMessage("Required permission to write external storage to save downloaded file.")
                    builder.setTitle("Please Grant Write Permission")
                    builder.setPositiveButton("OK") { _, _->
                        ActivityCompat.requestPermissions(
                            activity,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            PERMISSION_REQUEST_CODE
                        )
                    }
                    builder.setNeutralButton("Cancel", null)
                    val dialog = builder.create()
                    dialog.show()
                } else {
                    ActivityCompat.requestPermissions(
                        activity,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        PERMISSION_REQUEST_CODE
                    )
                }
            }
            else {
                /**
                 * Already have required permission.
                 * */
                writeAccess = false
            }
        }
    }
    private fun checkReadAccess()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            /**
             * Check for permission status.
             * */
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE))
                {
                    val builder = AlertDialog.Builder(activity)
                    builder.setMessage("Required permission to write external storage to save downloaded file.")
                    builder.setTitle("Please Grant Write Permission")
                    builder.setPositiveButton("OK") { _, _->
                        ActivityCompat.requestPermissions(
                            activity,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            PERMISSION_REQUEST_CODE
                        )
                    }
                    builder.setNeutralButton("Cancel", null)
                    val dialog = builder.create()
                    dialog.show()
                } else {
                    ActivityCompat.requestPermissions(
                        activity,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        PERMISSION_REQUEST_CODE
                    )
                }
            }
            else {
                /**
                 * Already have required permission.
                 * */
                readAccess = false
            }
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    writeAccess=true
                } else {
                    // Permission denied
                    writeAccess=false
                    Toast.makeText(context,"Permission Denied. This app will not work with right permission.",
                        Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    override fun onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack()
        }else{
            super.onBackPressed()
        }

    }




    @Throws(IOException::class)
    private fun createImageData(uri: Uri) {
        val inputStream = contentResolver.openInputStream(uri)
        inputStream?.buffered()?.use {
            imageData = it.readBytes()

        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_ACTIVITY) {
            val uri = data?.data
            if (uri != null) {

                createImageData(uri)
                dumpImageMetaData(uri)
            }
        }

    }


    @SuppressLint("Range")
    fun dumpImageMetaData(uri: Uri) {

        // The query, because it only applies to a single document, returns only
        // one row. There's no need to filter, sort, or select fields,
        // because we want all fields for one document.
        val cursor: Cursor? = contentResolver.query(
            uri, null, null, null, null, null)

        cursor?.use {
            // moveToFirst() returns false if the cursor has 0 rows. Very handy for
            // "if there's anything to look at, look at it" conditionals.
            if (it.moveToFirst()) {

                // Note it's called "Display Name". This is
                // provider-specific, and might not necessarily be the file name.
                val displayName: String =
                    it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                Log.i(TAG, "Display Name: $displayName")
                edtxt.setText(displayName)

            }
        }
    }


}