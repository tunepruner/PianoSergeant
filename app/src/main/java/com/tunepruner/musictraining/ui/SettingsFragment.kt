package com.tunepruner.musictraining.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.musictraining.R
import com.example.musictraining.databinding.FragmentSettingsBinding
import com.tunepruner.musictraining.repositories.ChordQuality
import com.tunepruner.musictraining.repositories.IntervalRequirements
import com.tunepruner.musictraining.repositories.Inversion
import com.tunepruner.musictraining.repositories.Mode
import com.tunepruner.musictraining.repositories.SettingsRepository
import com.tunepruner.musictraining.repositories.TimeConstraint
import kotlinx.android.synthetic.main.add_interval_requirements_layout.view.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.java.KoinJavaComponent

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@ExperimentalCoroutinesApi
class SettingsFragment : Fragment() {
    private val settings: SettingsRepository by KoinJavaComponent.inject(SettingsRepository::class.java)
    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding get() = _binding!!

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.timeConstraintLayout.rapidFireRadioButton.setOnClickListener {

            if (it.isEnabled) {
                settings.current.value.timeConstraint = TimeConstraint.RapidFire
            }
        }
        initializeViews()
    }

    private fun initializeViews() {
        settings.current.value.let { settings ->
            with(binding) {
                with(timeConstraintLayout) {
                    rapidFireRadioButton.isChecked =
                        settings.timeConstraint is TimeConstraint.RapidFire
                    metronomeRadioButton.isChecked =
                        settings.timeConstraint is TimeConstraint.Metronome
                }
                with(chooseModeLayout) {
                    scaleModeRadioButton.isChecked =
                        settings.mode is Mode.ScaleMode
                    chordModeRadioButton.isChecked =
                        settings.mode is Mode.ChordMode
                }
                with(add_interval_requirements_layout) {
                    none_button.isChecked =
                        settings.intervalRequirements is IntervalRequirements.None
                    less_than_button.isChecked =
                        settings.intervalRequirements is IntervalRequirements.LessThan
                    greater_than_button.isChecked =
                        settings.intervalRequirements is IntervalRequirements.GreaterThan
                }
                activity?.findViewById<TextView>(R.id.current_value)?.text =
                    settings.notesPerBeat.toString()
                with(selectInversionsLayout) {
                    rootPositionChip.isChecked =
                        settings.inversions.contains(Inversion.RootPosition)
                    firstInverisonChip.isChecked =
                        settings.inversions.contains(Inversion.FirstInversion)
                    secondInversionChip.isChecked =
                        settings.inversions.contains(Inversion.SecondInversion)
                    thirdInversionChip.isChecked =
                        settings.inversions.contains(Inversion.ThirdInversion)
                }
                with(selectChordQualitiesLayout) {
                    majorTriad.isChecked =
                        settings.chordQualities.contains(ChordQuality.MajorTriad)
                    minorTriad.isChecked =
                        settings.chordQualities.contains(ChordQuality.MinorTriad)
                    diminishedTriad.isChecked =
                        settings.chordQualities.contains(ChordQuality.DiminishedTriad)
                    augmentedTriad.isChecked =
                        settings.chordQualities.contains(ChordQuality.AugmentedTriad)
                    sus2Triad.isChecked =
                        settings.chordQualities.contains(ChordQuality.Sus2Triad)
                    sus4Triad.isChecked =
                        settings.chordQualities.contains(ChordQuality.Sus4Triad)
                    dominantSeventh.isChecked =
                        settings.chordQualities.contains(ChordQuality.DominantSeventh)
                    minorSeventh.isChecked =
                        settings.chordQualities.contains(ChordQuality.MinorSeventh)
                    minorMajorSeventh.isChecked =
                        settings.chordQualities.contains(ChordQuality.MinorMajorSeventh)
                    halfDiminishedSeventh.isChecked =
                        settings.chordQualities.contains(ChordQuality.HalfDiminishedSeventh)
                    fullDiminishedSeventh.isChecked =
                        settings.chordQualities.contains(ChordQuality.FullDiminishedSeventh)
                    augmentedSeventh.isChecked =
                        settings.chordQualities.contains(ChordQuality.AugmentedSeventh)
                    augmentedMajorSeventh.isChecked =
                        settings.chordQualities.contains(ChordQuality.AugmentedMajorSeventh)
                    dominantSeventhSus4.isChecked =
                        settings.chordQualities.contains(ChordQuality.DominantSeventhSus4)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        settings.persist()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SettingsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}