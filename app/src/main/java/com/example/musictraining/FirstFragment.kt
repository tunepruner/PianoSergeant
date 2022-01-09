package com.example.musictraining

import android.os.Bundle
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
//    private val viewModel: FirstViewModel by inject(FirstViewModel::class.java)
    private val viewModel = FirstViewModel(SettingsRepository())

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

        viewModel.updateLevelReading(binding.tempo, binding.tempoSelector, maxTempo)
        viewModel.updateLevelReading(
            binding.chordDistanceView,
            binding.chordDistanceSelector,
            maxDistance
        )
        viewModel.updateLevelReading(
            binding.beatsPerChordView,
            binding.beatsPerChordSelector,
            maxBeatsPerChord
        )

        viewModel.beatLiveData.observe(viewLifecycleOwner) {
            binding.visualMetronome.text = it.toString()
        }

        with(binding.playButton) {
            setImageResource(R.drawable.play_button);
            viewModel.playState.observe(viewLifecycleOwner) {
                when (it) {
                    PlayState.STOPPED -> setImageResource(R.drawable.play_button);
                    PlayState.PLAYING -> setImageResource(R.drawable.stop_button);
                }
            }
        }

        viewModel.currentSettings.observe(viewLifecycleOwner) {
            updateSettingsDisplay(it)
        }

        viewModel.barPercentage.observe(viewLifecycleOwner) {
            binding.cueLine.progress = viewModel.barPercentage.value ?: 0
        }
    }

    private fun setUpHandlers() {
        binding.playButton.setOnClickListener {
            viewModel.onPlayStopButtonPressed()
        }

        binding.chordCheckBox.setOnClickListener {
            viewModel.onChordCheckBoxClicked()
        }
        binding.modeCheckBox.setOnClickListener {
            viewModel.onModeCheckBoxClicked()
        }
        binding.keySigCheckBox.setOnClickListener {
            viewModel.onKeySigCheckBoxClicked()
        }
        binding.cueLineCheckBox.setOnClickListener {
            viewModel.onCueLineCheckBoxClicked()
        }
        binding.currentBeatCheckbox.setOnClickListener {
            viewModel.onMetronomeCheckBoxClicked()
        }
        binding.soundCheckBox.setOnClickListener {
            viewModel.onSoundCheckBoxClicked()
        }

        binding.beatsPerChordSelector.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                viewModel.updateLevelReading(
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
                viewModel.updateLevelReading(
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
                viewModel.updateLevelReading(binding.tempo, binding.tempoSelector, maxTempo)
                viewModel.updateSettings()
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
    }

    override fun onPause() {
        super.onPause()
        viewModel.forceStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun updateSettingsDisplay(settings: Settings) {
        binding.tempoSelector.progress = ((settings.tempo.toDouble() / maxTempo) * 100).roundToInt()
        binding.chordDistanceSelector.progress = ((settings.chordDistance.toDouble() / maxDistance) * 100).roundToInt()
        binding.beatsPerChordSelector.progress = ((settings.beatsPerChord.toDouble() / maxBeatsPerChord) * 100).roundToInt()
        binding.chordCheckBox.isChecked = settings.chordVisible
        binding.modeCheckBox.isChecked = settings.modeVisible
        binding.keySigCheckBox.isChecked = settings.keySigVisible
        binding.cueLineCheckBox.isChecked = settings.cueLineVisible
        binding.currentBeatCheckbox.isChecked = settings.currentBeatVisible
        binding.soundCheckBox.isChecked = settings.soundOn
    }
}