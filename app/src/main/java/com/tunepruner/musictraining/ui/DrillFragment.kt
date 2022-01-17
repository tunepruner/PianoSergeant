package com.tunepruner.musictraining.ui

import android.content.Context
import android.content.pm.PackageManager
import android.media.midi.MidiDeviceInfo
import android.media.midi.MidiDeviceStatus
import android.media.midi.MidiInputPort
import android.media.midi.MidiManager
import android.media.midi.MidiManager.DeviceCallback
import android.media.midi.MidiOutputPort
import android.media.midi.MidiReceiver
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.example.musictraining.R
import com.example.musictraining.databinding.FragmentMainBinding
import com.tunepruner.musictraining.midi.LoggingReceiver
import com.tunepruner.musictraining.midi.MidiFramer
import com.tunepruner.musictraining.model.PlayState
import com.tunepruner.musictraining.repositories.Settings
import com.tunepruner.musictraining.repositories.SettingsRepository
import com.tunepruner.musictraining.viewmodel.ChordViewModel
import com.tunepruner.musictraining.viewmodel.MetronomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import java.io.IOException
import kotlin.math.roundToInt
import kotlin.time.ExperimentalTime

const val LOG_TAG = "12345"
const val MAX_TEMPO = 270
const val MIN_TEMPO = 50
const val MAX_DISTANCE = 7
const val MIN_DISTANCE = 1
const val MAX_BEATS_PER_CHORD = 13
const val MIN_BEATS_PER_CHORD = 1

@ExperimentalCoroutinesApi
class DrillFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val metronomeViewModel: MetronomeViewModel by inject(MetronomeViewModel::class.java)
    private val chordViewModel: ChordViewModel by inject(ChordViewModel::class.java)
    private val settings: SettingsRepository by inject(SettingsRepository::class.java)


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun doMidiStuff() {
        Log.i(LOG_TAG, "onDeviceAdded: ")

        if (this.context?.packageManager?.hasSystemFeature(PackageManager.FEATURE_MIDI) == true) {


            val m =
                this@DrillFragment.context?.getSystemService(Context.MIDI_SERVICE) as MidiManager

            //to do things when plugging in or unplugging
            m.registerDeviceCallback(object : DeviceCallback() {
                override fun onDeviceAdded(info: MidiDeviceInfo?) {
                    super.onDeviceAdded(info)
                    Log.i(LOG_TAG, "onDeviceAdded: ")
                    binding.chord.text = "on device added"

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

                    //To access a MIDI device you need to open it first. The open is asynchronous so you need to provide a callback for completion. You can specify an optional Handler if you want the callback to occur on a specific Thread.
                    m.openDevice(
                        info,
                        { device ->
                            //If you want to send a message to a MIDI Device then you need to open an “input” port with exclusive access.
                            val inputPort: MidiInputPort = device.openInputPort(/*index*/0)
                            binding.chord.text = "open device"

                            class MyReceiver : MidiReceiver() {
                                @Throws(IOException::class)
                                override fun onSend(
                                    data: ByteArray, offset: Int,
                                    count: Int, timestamp: Long
                                ) {
                                    val loggingReceiver = LoggingReceiver {
                                        CoroutineScope(Dispatchers.Main).launch {
                                            binding.chord.text = it
                                        }
                                    }
                                    val framer = MidiFramer(loggingReceiver)
                                    framer.send(data, offset, count, timestamp)
                                }

                                init {
                                    binding.chord.text = "my receiver created"
                                }
                            }

                            val outputPort: MidiOutputPort = device.openOutputPort(/*index*/0)
                            outputPort.connect(MyReceiver())
                            Log.i(LOG_TAG, "openDevice: ")
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doMidiStuff()
        setUpHandlers()

        metronomeViewModel.updateLevelReading(
            binding.tempo,
            binding.tempoSelector,
            MAX_TEMPO,
            MIN_TEMPO
        )
        metronomeViewModel.updateLevelReading(
            binding.chordDistanceView,
            binding.chordDistanceSelector,
            MAX_DISTANCE,
            MIN_DISTANCE
        )
        metronomeViewModel.updateLevelReading(
            binding.beatsPerChordView,
            binding.beatsPerChordSelector,
            MAX_BEATS_PER_CHORD,
            MIN_BEATS_PER_CHORD
        )

        metronomeViewModel.publishedBeatNumber.observe(viewLifecycleOwner) {
            binding.currentBeat.text = it.toString()
            if (it == 1) {
                chordViewModel.triggerNextChord()
            }
        }

        with(binding.playButton) {
            setImageResource(R.drawable.play_button);
            metronomeViewModel.playState.observe(viewLifecycleOwner) {
                when (it) {
                    PlayState.STOPPED -> setImageResource(R.drawable.play_button);
                    PlayState.PLAYING -> setImageResource(R.drawable.stop_button);
                }
            }
        }

        metronomeViewModel.currentSettings.observe(viewLifecycleOwner) {
            updateSettingsDisplay(it)
        }

        metronomeViewModel.barPercentage.observe(viewLifecycleOwner) {
            binding.cueLine.progress = metronomeViewModel.barPercentage.value ?: 0
        }

        chordViewModel.currentChord.observe(viewLifecycleOwner) {
            binding.chord.text = it
        }
    }

    private fun setUpHandlers() {
        binding.playButton.setOnClickListener {
            metronomeViewModel.onPlayStopButtonPressed()
        }

        val map: Map<CheckBox, View> = mapOf(
            binding.chordCheckBox to binding.chord,
            binding.modeCheckBox to binding.mode,
            binding.keySigCheckBox to binding.keySig,
            binding.cueLineCheckBox to binding.cueLine,
            binding.currentBeatCheckbox to binding.currentBeat
        )

        for (element in map) {
            element.key.setOnClickListener {
                it as CheckBox
                element.value.visibility =
                    if (it.isChecked) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
            }
        }

        with(binding.soundCheckBox) {
            setOnClickListener {
                settings.current.value.soundOn = (it as CheckBox).isChecked
            }
        }


        binding.beatsPerChordSelector.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                metronomeViewModel.updateLevelReading(
                    binding.beatsPerChordView,
                    binding.beatsPerChordSelector,
                    MAX_BEATS_PER_CHORD,
                    MIN_BEATS_PER_CHORD
                )
                settings.current.value.setBeatsPerChordFromPercentage(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
        binding.chordDistanceSelector.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                metronomeViewModel.updateLevelReading(
                    binding.chordDistanceView,
                    binding.chordDistanceSelector,
                    MAX_DISTANCE,
                    MIN_DISTANCE
                )
                settings.current.value.setChordDistanceFromPercentage(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
        binding.tempoSelector.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                metronomeViewModel.updateLevelReading(
                    binding.tempo,
                    binding.tempoSelector,
                    MAX_TEMPO,
                    MIN_TEMPO
                )
                settings.current.value.setTempoFromPercentage(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
    }

    override fun onPause() {
        super.onPause()
        metronomeViewModel.forceStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateSettingsDisplay(settings: Settings) {
        binding.tempoSelector.progress =
            ((settings.tempo.toDouble() / MAX_TEMPO) * 100).roundToInt()
        binding.chordDistanceSelector.progress =
            ((settings.chordDistance.toDouble() / MAX_DISTANCE) * 100).roundToInt()
        binding.beatsPerChordSelector.progress =
            ((settings.beatsPerChord.toDouble() / MAX_BEATS_PER_CHORD) * 100).roundToInt()
        binding.chordCheckBox.isChecked = settings.chordVisible
        binding.modeCheckBox.isChecked = settings.modeVisible
        binding.keySigCheckBox.isChecked = settings.keySigVisible
        binding.cueLineCheckBox.isChecked = settings.cueLineVisible
        binding.currentBeatCheckbox.isChecked = settings.currentBeatVisible
        binding.soundCheckBox.isChecked = settings.soundOn
    }
}