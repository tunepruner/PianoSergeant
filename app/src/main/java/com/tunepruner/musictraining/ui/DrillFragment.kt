package com.tunepruner.musictraining.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.musictraining.R
import com.example.musictraining.databinding.FragmentDrillBinding
import com.tunepruner.musictraining.model.PlayState
import com.tunepruner.musictraining.repositories.Settings
import com.tunepruner.musictraining.repositories.SettingsRepository
import com.tunepruner.musictraining.viewmodel.ChordViewModel
import com.tunepruner.musictraining.viewmodel.MetronomeViewModel
import com.tunepruner.musictraining.viewmodel.NoteInputViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.java.KoinJavaComponent.inject
import kotlin.math.roundToInt

const val LOG_TAG = "12345"
const val MAX_TEMPO = 270
const val MIN_TEMPO = 50
const val MAX_DISTANCE = 7
const val MIN_DISTANCE = 1
const val MAX_BEATS_PER_CHORD = 13
const val MIN_BEATS_PER_CHORD = 1

@ExperimentalCoroutinesApi
class DrillFragment : Fragment() {
    private var _binding: FragmentDrillBinding? = null
    private val metronomeViewModel: MetronomeViewModel by inject(MetronomeViewModel::class.java)
    private val chordViewModel: ChordViewModel by inject(ChordViewModel::class.java)
    private val inputViewModel: NoteInputViewModel by inject(NoteInputViewModel::class.java)
    private val settings: SettingsRepository by inject(SettingsRepository::class.java)


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDrillBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

        inputViewModel.latestNote.observe(viewLifecycleOwner) {
            binding.noteInput.text = it ?: "-"
        }
    }

    private fun setUpHandlers() {
        binding.settingsButton.setOnClickListener {
            findNavController().navigate(R.id.action_drill_to_chord_drill_settings)
        }

        binding.playButton.setOnClickListener {
            metronomeViewModel.onPlayStopButtonPressed()
        }

        binding.resendButton.setOnClickListener {
            inputViewModel.resendLastNote()
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