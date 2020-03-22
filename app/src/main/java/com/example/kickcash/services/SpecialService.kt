package com.example.kickcash.services

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent



fun Start(x: Int): Int {
    Log.d("TAG", "message")
    return 1;
}


class SpecialService : AccessibilityService() {



    private var isPlay = false
    private fun getEventType(event: AccessibilityEvent): String {
        when (event.eventType) {
            AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED -> return "TYPE_NOTIFICATION_STATE_CHANGED"
            AccessibilityEvent.TYPE_VIEW_CLICKED -> return "TYPE_VIEW_CLICKED"
            AccessibilityEvent.TYPE_VIEW_FOCUSED -> return "TYPE_VIEW_FOCUSED"
            AccessibilityEvent.TYPE_VIEW_LONG_CLICKED -> return "TYPE_VIEW_LONG_CLICKED"
            AccessibilityEvent.TYPE_VIEW_SELECTED -> return "TYPE_VIEW_SELECTED"
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> return "TYPE_WINDOW_STATE_CHANGED"
            AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED -> return "TYPE_VIEW_TEXT_CHANGED"
        }
        return "default"
    }

    private fun getEventText(event: AccessibilityEvent): String {
        val sb = StringBuilder()
        for (s in event.text) {
            sb.append(s)
        }
        return sb.toString()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        val eventType = getEventType(event)
        val eventClassName = "" + event.className
        val eventPackageName = "" + event.packageName
        val eventTime = event.eventTime
        val eventText = getEventText(event)
        var flag: String
        var flag1: String
        Log.i("Event Type", eventType)
        Log.i("Event Class Name", eventClassName)
        Log.i("Event Package Name", eventPackageName)
        Log.i("Event Text", eventText)
        //eventClassName.contains("InCallActivity")
        if (("" + eventText).toLowerCase().contains("вызов") && !("" + eventText).toLowerCase().contains("входящие") && eventPackageName == "com.samsung.android.incallui"
                || eventPackageName == "com.whatsapp" && eventClassName == "android.widget.ImageView" && ("" + eventText).toLowerCase() == "звонок") {
            if (!isPlay) {

                Start(1);




                val intent = Intent()
                intent.action = "com.example.kickcash.services.MusicService"
                intent.putExtra("Command", "Play")
                applicationContext.sendBroadcast(intent)
                isPlay = true
            }
        } else if ((eventPackageName == "com.android.dialer" && ("" + eventText).toLowerCase().contains("заверш") && ("" + eventText).toLowerCase().contains("end")
                        || eventPackageName == "com.samsung.android.incallui" && ("" + eventText).toLowerCase().contains("заверш"))
                || eventClassName == "android.widget.Chronometer" && eventPackageName == "com.samsung.android.incallui"
                || eventClassName == "android.widget.Toast\$TN" && eventPackageName == "com.whatsapp" && ("" + eventText).toLowerCase() == "соединено"
                || eventClassName == "android.widget.ImageButton" && eventPackageName == "com.whatsapp" && ("" + eventText).toLowerCase().contains("завершить")) {
            val intent = Intent()
            intent.action = "com.example.kickcash.services.MusicService"
            intent.putExtra("Command", "Stop")
            applicationContext.sendBroadcast(intent)
            isPlay = false
        }
        Log.v(TAG, String.format(
                "onAccessibilityEvent: [type] %s [class] %s [package] %s [time] %s [text] %s",
                getEventType(event), event.className, event.packageName,
                event.eventTime, getEventText(event)))
    }

    override fun onInterrupt() {
        Log.v(TAG, "onInterrupt")
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.v(TAG, "onServiceConnected")
        val info = AccessibilityServiceInfo()
        info.flags = AccessibilityServiceInfo.DEFAULT
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
        serviceInfo = info
    }

    companion object {
        const val TAG = "SpecialService"
    }
}
