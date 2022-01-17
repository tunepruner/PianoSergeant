package com.tunepruner.musictraining.repositories

import android.content.Context
import android.content.pm.PackageManager
import android.media.midi.MidiDeviceInfo
import android.media.midi.MidiDeviceStatus
import android.media.midi.MidiInputPort
import android.media.midi.MidiManager
import android.media.midi.MidiOutputPort
import android.media.midi.MidiReceiver
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.tunepruner.musictraining.midi.NoteReceiver
import com.tunepruner.musictraining.midi.MidiFramer
import com.tunepruner.musictraining.ui.LOG_TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class IncomingMidiSource(context: Context) {
    private val _flowOfNotes: MutableStateFlow<String?> = MutableStateFlow(null)
    val flowOfNotes: StateFlow<String?> = _flowOfNotes
    init {
        doMidiStuff(context)
    }

    private fun doMidiStuff(context: Context) {
        Log.i(LOG_TAG, "onDeviceAdded: ")

        if (context.packageManager?.hasSystemFeature(PackageManager.FEATURE_MIDI) == true) {


            val m =
                context.getSystemService(Context.MIDI_SERVICE) as MidiManager

            //to do things when plugging in or unplugging
            m.registerDeviceCallback(object : MidiManager.DeviceCallback() {
                override fun onDeviceAdded(info: MidiDeviceInfo?) {
                    super.onDeviceAdded(info)
                    Log.i(LOG_TAG, "onDeviceAdded: ")

//                    otherThingsYouCanDo(info)

                    //To access a MIDI device you need to open it first. The open is asynchronous so you need to provide a callback for completion. You can specify an optional Handler if you want the callback to occur on a specific Thread.
                    m.openDevice(
                        info,
                        { device ->
                            //If you want to send a message to a MIDI Device then you need to open an “input” port with exclusive access.
                            val inputPort: MidiInputPort = device.openInputPort(/*index*/0)
                            class MyReceiver : MidiReceiver() {
                                @Throws(IOException::class)
                                override fun onSend(
                                    data: ByteArray, offset: Int,
                                    count: Int, timestamp: Long
                                ) {
                                    val loggingReceiver =
                                        NoteReceiver {
                                            CoroutineScope(Dispatchers.Main).launch {
                                                _flowOfNotes.value = it
                                            }
                                        }
                                    val framer = MidiFramer(loggingReceiver)
                                    framer.send(data, offset, count, timestamp)
                                }

                                init {
//                                    binding.chord.text = "my receiver created"
                                }
                            }

                            val outputPort: MidiOutputPort = device.openOutputPort(/*index*/0)
                            outputPort.connect(MyReceiver())
                            Log.i(LOG_TAG, "openDevice: ")
                        },
                        Handler(Looper.getMainLooper())
                    )

                }

                private fun otherThingsYouCanDo(info: MidiDeviceInfo?) {
                    //You can query the number of input and output ports.
                    val numInputs: Int? = info?.inputPortCount
                    val numOutputs: Int? = info?.outputPortCount

                    //The MidiDeviceInfo has a bundle of properties.
                    val properties = info?.properties
                    val manufacturer = properties?.getString(MidiDeviceInfo.PROPERTY_MANUFACTURER)
                    //Other properties include PROPERTY_PRODUCT, PROPERTY_NAME, PROPERTY_SERIAL_NUMBER

                    //You can get the names and types of the ports from a PortInfo object. The type will be either TYPE_INPUT or TYPE_OUTPUT.
                    val portInfos: Array<out MidiDeviceInfo.PortInfo>? = info?.ports
                    val portName: String? = portInfos?.get(0)?.name
                    if (portInfos?.get(0)?.type == MidiDeviceInfo.PortInfo.TYPE_INPUT) {
                    }
                }

                override fun onDeviceRemoved(info: MidiDeviceInfo) {
                    super.onDeviceRemoved(info)
                }

                // Update port open counts so user knows if the device is in use.
                override fun onDeviceStatusChanged(status: MidiDeviceStatus) {
                    super.onDeviceStatusChanged(status)
                }
            }, Handler(Looper.getMainLooper()))


        }
    }
}