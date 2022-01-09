package com.example.musictraining

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.example.musictraining.databinding.FragmentFirstBinding
import org.koin.java.KoinJavaComponent.inject
import kotlin.math.roundToInt

const val maxTempo = 270
const val maxDistance = 7
const val maxBeatsPerChord = 13

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val firstViewModel: FirstViewModel by inject(FirstViewModel::class.java)
    private val chordViewModel: ChordViewModel by inject(ChordViewModel::class.java)
    private val settings: SettingsRepository by inject(SettingsRepository::class.java)

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpHandlers()

        firstViewModel.updateLevelReading(binding.tempo, binding.tempoSelector, maxTempo)
        firstViewModel.updateLevelReading(
            binding.chordDistanceView,
            binding.chordDistanceSelector,
            maxDistance
        )
        firstViewModel.updateLevelReading(
            binding.beatsPerChordView,
            binding.beatsPerChordSelector,
            maxBeatsPerChord
        )

        firstViewModel.beatLiveData.observe(viewLifecycleOwner) {
            binding.visualMetronome.text = it.toString()
            if (it == 1) {
                chordViewModel.triggerNextChord()
            }
        }

        with(binding.playButton) {
            setImageResource(R.drawable.play_button);
            firstViewModel.playState.observe(viewLifecycleOwner) {
                when (it) {
                    PlayState.STOPPED -> setImageResource(R.drawable.play_button);
                    PlayState.PLAYING -> setImageResource(R.drawable.stop_button);
                }
            }
        }

        firstViewModel.currentSettings.observe(viewLifecycleOwner) {
            updateSettingsDisplay(it)
        }

        firstViewModel.barPercentage.observe(viewLifecycleOwner) {
            binding.cueLine.progress = firstViewModel.barPercentage.value ?: 0
        }

        chordViewModel.currentChord.observe(viewLifecycleOwner) {
            binding.chord.text = it
        }
    }

    private fun setUpHandlers() {
        binding.playButton.setOnClickListener {
            firstViewModel.onPlayStopButtonPressed()
        }

        binding.chordCheckBox.setOnClickListener {
            firstViewModel.onChordCheckBoxClicked()
        }
        binding.modeCheckBox.setOnClickListener {
            firstViewModel.onModeCheckBoxClicked()
        }
        binding.keySigCheckBox.setOnClickListener {
            firstViewModel.onKeySigCheckBoxClicked()
        }
        binding.cueLineCheckBox.setOnClickListener {
            firstViewModel.onCueLineCheckBoxClicked()
        }
        binding.currentBeatCheckbox.setOnClickListener {
            firstViewModel.onMetronomeCheckBoxClicked()
        }
        binding.soundCheckBox.setOnClickListener {
            firstViewModel.onSoundCheckBoxClicked()
        }

        binding.beatsPerChordSelector.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                firstViewModel.updateLevelReading(
                    binding.beatsPerChordView,
                    binding.beatsPerChordSelector,
                    maxBeatsPerChord
                )
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
        binding.chordDistanceSelector.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                firstViewModel.updateLevelReading(
                    binding.chordDistanceView,
                    binding.chordDistanceSelector,
                    maxDistance
                )
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
        binding.tempoSelector.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                firstViewModel.updateLevelReading(binding.tempo, binding.tempoSelector, maxTempo)
                settings.current.value.tempo = p1
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
    }

    override fun onPause() {
        super.onPause()
        firstViewModel.forceStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun updateSettingsDisplay(settings: Settings) {
        binding.tempoSelector.progress = ((settings.tempo.toDouble() / maxTempo) * 100).roundToInt()
        binding.chordDistanceSelector.progress =
            ((settings.chordDistance.toDouble() / maxDistance) * 100).roundToInt()
        binding.beatsPerChordSelector.progress =
            ((settings.beatsPerChord.toDouble() / maxBeatsPerChord) * 100).roundToInt()
        binding.chordCheckBox.isChecked = settings.chordVisible
        binding.modeCheckBox.isChecked = settings.modeVisible
        binding.keySigCheckBox.isChecked = settings.keySigVisible
        binding.cueLineCheckBox.isChecked = settings.cueLineVisible
        binding.currentBeatCheckbox.isChecked = settings.currentBeatVisible
        binding.soundCheckBox.isChecked = settings.soundOn
    }
}