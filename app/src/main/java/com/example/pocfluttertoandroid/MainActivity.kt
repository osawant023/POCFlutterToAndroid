package com.example.pocfluttertoandroid

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pocfluttertoandroid.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.plugin.common.MethodChannel


class MainActivity : AppCompatActivity() {
    lateinit var binding  : ActivityMainBinding
    lateinit var flutterEngine : FlutterEngine
    lateinit var mSenderChannel : MethodChannel
    lateinit var mReceiverChannel : MethodChannel
    companion object{
        const val ENGINE_ID = "12345"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setContentView(binding.main)

        setUpFlutterEngine()
        onClickListeners()

    }

    private fun onClickListeners() {
        binding.btnNavigate.setOnClickListener {
            mSenderChannel = MethodChannel(
                flutterEngine
                    .dartExecutor
                    .binaryMessenger,
                "native_args"
            )

            val data: MutableMap<String?, Any?> = HashMap()
            data.put("platFormName", "Android Native")
            data.put("userId", "42")
            mSenderChannel.invokeMethod("initialScreen" , data)

            startActivity(
                FlutterActivity
                    .withCachedEngine(ENGINE_ID)
                    .build(this)
            )
        }
    }

    private fun setUpFlutterEngine() {
        //Instantiate a FlutterEngine.
        flutterEngine = FlutterEngine(this)

        // Start executing Dart code to pre-warm the FlutterEngine.
        flutterEngine.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
        )

        // Cache the FlutterEngine to be used by FlutterActivity.
        FlutterEngineCache
            .getInstance()
            .put(ENGINE_ID, flutterEngine)

        mReceiverChannel = MethodChannel(
            flutterEngine
                .dartExecutor
                .binaryMessenger,
            "flutter_navigation"
        )

        mReceiverChannel.setMethodCallHandler { call, result ->
            if (call.method.equals("close")) {
                val valueFromFlutter = call.arguments as HashMap<String?, String?>
                var mData = ""
                valueFromFlutter.forEach {
                    mData += it.key?.uppercase()+" : "+it.value+"\n"
                }
                binding.txtData.text = mData
                //onBackPressedDispatcher.onBackPressed()
            }
        }
    }
}