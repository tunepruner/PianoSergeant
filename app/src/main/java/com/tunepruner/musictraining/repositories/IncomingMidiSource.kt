package com.tunepruner.musictraining.repositories

import android.content.Context
import android.content.pm.PackageManager
import android.media.midi.MidiDeviceInfo
import android.media.midi.MidiDeviceStatus
import android.media.midi.MidiManager
import android.media.midi.MidiOutputPort
import android.media.midi.MidiReceiver
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.datastore.preferences.core.edit
import com.google.gson.GsonBuilder
import com.tunepruner.musictraining.midi.MidiFramer
import com.tunepruner.musictraining.midi.MidiParser
import com.tunepruner.musictraining.midi.NoteReceiver
import com.tunepruner.musictraining.model.constants.LAST_NOTE
import com.tunepruner.musictraining.model.constants.dataStore
import com.tunepruner.musictraining.ui.LOG_TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException


data class LastNote(
    val data: ByteArray,
    val offset: Int,
    val count: Int,
    val timestamp: Long
)

class IncomingMidiSource(context: Context) {
    val dataStore = context.dataStore
    var lastNoteFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[LAST_NOTE] ?: ""
    }
    var lastNote: LastNote? = null

    val noteReceiver =
        NoteReceiver {
            Log.i(LOG_TAG, "received: $it")
            _flowOfNotes.value = it
        }
    var midiFramer: MidiFramer? = MidiFramer(noteReceiver)

    private val _flowOfNotes: MutableStateFlow<String?> = MutableStateFlow(null)
    val flowOfNotes: StateFlow<String?> = _flowOfNotes

    init {
        doMidiStuff(context)

        CoroutineScope(Dispatchers.Main).launch {
            lastNoteFlow.collect { lastNoteJson ->
                lastNote = GsonBuilder().create()
                    .fromJson(
                        lastNoteJson,
                        LastNote::class.java
                    )
            }
        }
    }

    private fun doMidiStuff(context: Context) {
        Log.i(LOG_TAG, "onDeviceAdded: ")

        if (context.packageManager?.hasSystemFeature(PackageManager.FEATURE_MIDI) == true) {
            val m = context.getSystemService(Context.MIDI_SERVICE) as MidiManager

            //to do things when plugging in or unplugging
            m.registerDeviceCallback(object : MidiManager.DeviceCallback() {
                override fun onDeviceAdded(info: MidiDeviceInfo?) {
                    super.onDeviceAdded(info)

                    m.openDevice(
                        info,
                        { device ->
                            class MyReceiver : MidiReceiver() {
                                @Throws(IOException::class)
                                override fun onSend(
                                    data: ByteArray, offset: Int,
                                    count: Int, timestamp: Long
                                ) {
                                    midiFramer?.send(data, offset, count, timestamp)

                                    if (MidiParser.isNoteOn(data, offset, count)) {
                                        CoroutineScope(Dispatchers.Main).launch {
                                            dataStore.edit { lastSavedPrefs ->
                                                lastSavedPrefs[LAST_NOTE] =
                                                    GsonBuilder().create().toJson(
                                                        LastNote(data, offset, count, timestamp)
                                                    )
                                            }
                                        }
                                    }
                                }
                            }

                            val outputPort: MidiOutputPort = device.openOutputPort(/*index*/0)
                            outputPort.connect(MyReceiver())
                        },
                        Handler(Looper.getMainLooper())
                    )
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

    fun resendLastNote() {
        midiFramer.apply {
            Log.i(LOG_TAG, "midiFramer =: ${midiFramer}")
            lastNote?.let {
                Log.i(LOG_TAG, "lastNote =: ${lastNote}")
                midiFramer?.send(
                    it.data,
                    it.offset,
                    it.count,
                    it.timestamp
                )
            }
        }
    }


}