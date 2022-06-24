package ru.rshb.flutter_screenshot_disable

import android.app.Activity
import android.content.Context
import android.view.WindowManager
import androidx.annotation.NonNull

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** FlutterScreenshotDisablePlugin */
 class FlutterScreenshotDisablePlugin: FlutterPlugin, MethodCallHandler, ActivityAware {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel
  private lateinit var applicationContext: Context
    private var activity: Activity? = null
    private var disableScreenshot: Boolean = false

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    onAttachedToEngine(flutterPluginBinding.applicationContext, flutterPluginBinding.binaryMessenger)
  }


  private fun onAttachedToEngine(applicationContext: Context, messenger: BinaryMessenger) {
    this.applicationContext = applicationContext
    this.channel = MethodChannel(messenger, "ru.rshb.flutter_screenshot_disable")
    this.channel.setMethodCallHandler(this)
  }

  private fun setDisableScreenshotStatus(disable: Boolean) {
    if (disable) { // Disable screenshot
      activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
    } else { // Enable screenshot
      activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
    }
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
      if(activity?.application == null){
          result.error(call.method,  "failed application registration", Exception("failed application registration"))
      }
    if (call.method == "disableScreenshot") {
      val disable = call.argument<Boolean>("disable") == true
      setDisableScreenshotStatus(disable)
      result.success("")
    } else {
      result.notImplemented()
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    activity = binding.activity
    setDisableScreenshotStatus(this.disableScreenshot)
  }

  override fun onDetachedFromActivity() {

  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    activity = binding.activity
    setDisableScreenshotStatus(this.disableScreenshot)
  }

  override fun onDetachedFromActivityForConfigChanges() {}
}
