import 'dart:async';
import 'dart:io';

import 'package:flutter/services.dart';

class FlutterScreenshotDisable {
  /// Create a channel for communication
  static const MethodChannel _methodChannel = MethodChannel("ru.rshb.flutter_screenshot_disable");

  /// Disabling screenshots is only supported on the Android platform
  static Future<void> disableScreenshot(bool disable) async {
    if (Platform.isAndroid) {
      return await _methodChannel.invokeMethod("disableScreenshot", {"disable": disable});
    }
  }
}
