package com.tunepruner.musictraining.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.musictraining.R
import com.example.musictraining.databinding.FragmentChordDrillSettingsBinding
import com.tunepruner.musictraining.repositories.AlgorithmSetting
import com.tunepruner.musictraining.repositories.ChordQuality
import com.tunepruner.musictraining.repositories.IntervalRequirements
import com.tunepruner.musictraining.repositories.Inversion
import com.tunepruner.musictraining.repositories.Key
import com.tunepruner.musictraining.repositories.MAX_DOUBLING_AMOUNT
import com.tunepruner.musictraining.repositories.MIN_DOUBLING_AMOUNT
import com.tunepruner.musictraining.repositories.NoteDoublingRequirement
import com.tunepruner.musictraining.repositories.PatternSubSetting
import com.tunepruner.musictraining.repositories.RegisterRequirement
import com.tunepruner.musictraining.repositories.SettingsRepository
import com.tunepruner.musictraining.repositories.SpacingRequirement
import com.tunepruner.musictraining.repositories.TimeConstraint
import com.tunepruner.musictraining.repositories.allIntervals
import com.tunepruner.musictraining.viewmodel.SettingsViewModel
import kotlinx.android.synthetic.main.add_interval_requirements_layout.view.*
import kotlinx.android.synthetic.main.algorithm_for_prompts_layout.view.*
import kotlinx.android.synthetic.main.choose_mode_layout.*
import kotlinx.android.synthetic.main.constrain_permitted_voicings_layout.*
import kotlinx.android.synthetic.main.fragment_chord_drill_settings.*
import kotlinx.android.synthetic.main.number_selector.view.*
import kotlinx.android.synthetic.main.select_inversions_layout.*
import kotlinx.android.synthetic.main.time_constraint_layout.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.java.KoinJavaComponent

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChordDrillSettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@ExperimentalCoroutinesApi
class ChordDrillSettingsFragment : Fragment() {
    private val settingsViewModel: SettingsViewModel by viewModel()
    private val settings: SettingsRepository by KoinJavaComponent.inject(SettingsRepository::class.java)
    private var _binding: FragmentChordDrillSettingsBinding? = null
    private val binding: FragmentChordDrillSettingsBinding get() = _binding!!

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
        _binding = FragmentChordDrillSettingsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
    }

    private fun initializeViews() {
        binding.startDrillButton.setOnClickListener {
            findNavController().navigate(R.id.action_chord_drill_settings_to_drill)
        }
        settings.current.value.let { settings ->
            with(binding) {
                with(time_constraint_radio_group) {
                    settingsViewModel.timeConstraint.observe(viewLifecycleOwner) {
                        check(
                            when (it) {
                                TimeConstraint.RAPID_FIRE -> R.id.rapid_fire_radio_button
                                TimeConstraint.METRONOME -> R.id.metronome_radio_button
                            }
                        )
                    }
                    setOnCheckedChangeListener { _, button ->
                        when (button) {
                            R.id.metronome_radio_button -> settingsViewModel.enableMetronome()
                            R.id.rapid_fire_radio_button -> settingsViewModel.enableRapidFire()
                        }
                    }
                }
                with(add_interval_requirements_layout) {
                    //Initialize the radio button state and related ui states
                    radio_group.check(
                        when (settings.intervalRequirements) {
                            IntervalRequirements.NONE -> {
                                current_value.text = "-"
                                R.id.none_button
                            }
                            IntervalRequirements.LESS_THAN -> {
                                current_value.text = settings.intervalLessThanValue.uiName
                                R.id.less_than_button
                            }
                            IntervalRequirements.GREATER_THAN -> {
                                current_value.text = settings.intervalGreaterThanValue.uiName
                                R.id.greater_than_button
                            }
                        }
                    )

                    radio_group.setOnCheckedChangeListener { _, button ->
                        when (button) {
                            R.id.none_button -> {
                                settings.intervalRequirements = IntervalRequirements.NONE
                                current_value.text = "-"
                            }
                            R.id.less_than_button -> {
                                settings.intervalRequirements = IntervalRequirements.LESS_THAN
                                current_value.text = settings.intervalLessThanValue.uiName
                            }
                            R.id.greater_than_button -> {
                                settings.intervalRequirements =
                                    IntervalRequirements.GREATER_THAN
                                current_value.text = settings.intervalGreaterThanValue.uiName
                            }
                        }
                        persistSettings()
                    }
                    up_button.setOnClickListener {
                        if (settings.intervalRequirements == IntervalRequirements.LESS_THAN) {
                            val currentIndex: Int =
                                allIntervals.indexOf(settings.intervalLessThanValue)
                            if (currentIndex < allIntervals.size - 1) {
                                settings.intervalLessThanValue = allIntervals[currentIndex + 1]
                                current_value.text = settings.intervalLessThanValue.uiName
                            }
                        } else if (settings.intervalRequirements == IntervalRequirements.GREATER_THAN) {
                            val currentIndex: Int =
                                allIntervals.indexOf(settings.intervalGreaterThanValue)
                            if (currentIndex < allIntervals.size - 1) {
                                settings.intervalGreaterThanValue = allIntervals[currentIndex + 1]
                                current_value.text = settings.intervalGreaterThanValue.uiName
                            }
                        }
                        persistSettings()
                    }
                    down_button.setOnClickListener {
                        if (settings.intervalRequirements == IntervalRequirements.LESS_THAN) {
                            val currentIndex: Int =
                                allIntervals.indexOf(settings.intervalLessThanValue)
                            if (currentIndex > 1) {
                                settings.intervalLessThanValue = allIntervals[currentIndex - 1]
                                current_value.text = settings.intervalLessThanValue.uiName
                            }
                        } else if (settings.intervalRequirements == IntervalRequirements.GREATER_THAN) {
                            Log.i(LOG_TAG, "index of: ${settings.intervalGreaterThanValue}")
                            val currentIndex: Int =
                                allIntervals.indexOf(settings.intervalGreaterThanValue)
                            if (currentIndex > 1) {
                                settings.intervalGreaterThanValue = allIntervals[(currentIndex - 1)]
                                current_value.text = settings.intervalGreaterThanValue.uiName
                            }
                        }
                        persistSettings()
                    }
                }
                with(selectInversionsLayout) {
                    val inversionsMap = mapOf(
                        Inversion.ROOT_POSITION to rootPositionChip,
                        Inversion.FIRST_INVERSION to firstInverisonChip,
                        Inversion.SECOND_INVERSION to secondInversionChip,
                        Inversion.THIRD_INVERSION to thirdInversionChip,
                    )
                    for (element in inversionsMap) {
                        element.value.isChecked = settings.inversions.contains(element.key)
                        element.value.setOnCheckedChangeListener { compoundButton, b ->
                            if (compoundButton.isChecked) {
                                settings.inversions.add(element.key)
                            } else {
                                settings.inversions.remove(element.key)
                            }
                            persistSettings()
                        }
                    }
                }


                with(selectChordQualitiesLayout) {
                    val chordQualitiesMap = mapOf(
                        ChordQuality.MAJOR_TRIAD to majorTriad,
                        ChordQuality.MINOR_TRIAD to minorTriad,
                        ChordQuality.DIMINISHED_TRIAD to diminishedTriad,
                        ChordQuality.AUGMENTED_TRIAD to augmentedTriad,
                        ChordQuality.SUS_2_TRIAD to sus2Triad,
                        ChordQuality.SUS_4_TRIAD to sus4Triad,
                        ChordQuality.MAJOR_SEVENTH to majorSeventh,
                        ChordQuality.DOMINANT_SEVENTH to dominantSeventh,
                        ChordQuality.MINOR_SEVENTH to minorSeventh,
                        ChordQuality.MINOR_MAJOR_SEVENTH to minorMajorSeventh,
                        ChordQuality.HALF_DIMINISHED_SEVENTH to halfDiminishedSeventh,
                        ChordQuality.FULL_DIMINISHED_SEVENTH to fullDiminishedSeventh,
                        ChordQuality.AUGMENTED_SEVENTH to augmentedSeventh,
                        ChordQuality.AUGMENTED_MAJOR_SEVENTH to augmentedMajorSeventh,
                        ChordQuality.DOMINANT_SEVENTH_SUS_4 to dominantSeventhSus4,
                    )

                    for (element in chordQualitiesMap) {
                        element.value.isChecked = settings.chordQualities.contains(element.key)
                        element.value.setOnCheckedChangeListener { compoundButton, b ->
                            if (compoundButton.isChecked) {
                                settings.chordQualities.add(element.key)
                            } else {
                                settings.chordQualities.remove(element.key)
                            }
                            persistSettings()
                        }
                    }
                }

                with(note_doubling_requirement_layout) {
                    radio_group.apply {
                        check(
                            when (settings.noteDoublingRequirement) {
                                NoteDoublingRequirement.NONE -> R.id.scale_mode_radio_button
                                NoteDoublingRequirement.SPECIFIC_AMOUNT -> R.id.specific_amount_button
                            }
                        )
                        setOnCheckedChangeListener { _, button ->
                            Log.i(LOG_TAG, "clicked: ")
                            settings.noteDoublingRequirement =
                                when (button) {
                                    R.id.specific_amount_button -> NoteDoublingRequirement.SPECIFIC_AMOUNT
                                    else -> NoteDoublingRequirement.NONE
                                }
                            persistSettings()
                        }
                    }

                    current_value.text = settings.noteDoublingAmount.toString()

                    up_button.setOnClickListener {
                        if (settings.noteDoublingRequirement == NoteDoublingRequirement.SPECIFIC_AMOUNT) {
                            if (settings.noteDoublingAmount < MAX_DOUBLING_AMOUNT) {
                                settings.noteDoublingAmount++
                            }
                            current_value.text = settings.noteDoublingAmount.toString()
                            persistSettings()
                        }
                    }
                    down_button.setOnClickListener {
                        if (settings.noteDoublingRequirement == NoteDoublingRequirement.SPECIFIC_AMOUNT) {
                            if (settings.noteDoublingAmount > MIN_DOUBLING_AMOUNT) {
                                settings.noteDoublingAmount--
                            }
                            current_value.text = settings.noteDoublingAmount.toString()
                            persistSettings()
                        }
                    }
                }

                with(voicing_spacing_requirement_layout.radio_group) {
                    check(
                        when (settings.spacingRequirement) {
                            SpacingRequirement.NONE -> R.id.none
                            SpacingRequirement.OPEN_VOICING -> R.id.closed_voicing
                            SpacingRequirement.CLOSED_VOICING -> R.id.open_voicing
                        }
                    )
                    setOnCheckedChangeListener { _, button ->
                        settings.spacingRequirement = when (button) {
                            R.id.closed_voicing -> SpacingRequirement.CLOSED_VOICING
                            R.id.open_voicing -> SpacingRequirement.OPEN_VOICING
                            else -> SpacingRequirement.NONE
                        }
                        persistSettings()
                    }
                }

                with(register_requirement_layout.radio_group) {
                    check(
                        when (settings.registerRequirement) {
                            RegisterRequirement.NONE -> R.id.none
                            RegisterRequirement.REQUIRE_VOICE_LEADING -> R.id.voice_leading
                            RegisterRequirement.REQUIRE_COMMON_TOP_NOTE -> R.id.common_top_note
                            RegisterRequirement.REQUIRE_COMMON_BOTTOM_NOTE -> R.id.common_bottom_note
                            RegisterRequirement.REQUIRE_LEAP_GREATER_THAN_5TH -> R.id.leap_greater_than
                        }
                    )
                    setOnCheckedChangeListener { _, button ->
                        settings.registerRequirement = when (button) {
                            R.id.voice_leading -> RegisterRequirement.REQUIRE_VOICE_LEADING
                            R.id.common_top_note -> RegisterRequirement.REQUIRE_COMMON_TOP_NOTE
                            R.id.common_bottom_note -> RegisterRequirement.REQUIRE_COMMON_BOTTOM_NOTE
                            R.id.leap_greater_than -> RegisterRequirement.REQUIRE_LEAP_GREATER_THAN_5TH
                            else -> RegisterRequirement.NONE
                        }
                        persistSettings()
                    }
                }

                with(selectKeysLayout) {
                    val keysMap = mapOf(
                        Key.A_MAJOR to aMajor,
                        Key.Bb_MAJOR to bbMajor,
                        Key.B_MAJOR to bMajor,
                        Key.C_MAJOR to cMajor,
                        Key.Db_MAJOR to dbMajor,
                        Key.D_MAJOR to dMajor,
                        Key.Eb_MAJOR to ebMajor,
                        Key.E_MAJOR to eMajor,
                        Key.F_MAJOR to fMajor,
                        Key.Fsharp_MAJOR to fsMajor,
                        Key.G_MAJOR to gMajor,
                        Key.Ab_MAJOR to abMajor,
                        Key.A_MINOR to aMinor,
                        Key.Bb_MINOR to bbMinor,
                        Key.B_MINOR to bMinor,
                        Key.C_MINOR to cMinor,
                        Key.Db_MINOR to dbMinor,
                        Key.D_MINOR to dMinor,
                        Key.Eb_MINOR to ebMinor,
                        Key.E_MINOR to eMinor,
                        Key.F_MINOR to fMinor,
                        Key.Fsharp_MINOR to fsMinor,
                        Key.G_MINOR to gMinor,
                        Key.Ab_MINOR to abMinor,
                    )

                    for (element in keysMap) {
                        element.value.isChecked = settings.keys.contains(element.key)
                        element.value.setOnCheckedChangeListener { compoundButton, b ->
                            if (compoundButton.isChecked) {
                                settings.keys.add(element.key)
                            } else {
                                settings.keys.remove(element.key)
                            }
                            persistSettings()
                        }
                    }
                }

                with(algorithm_for_prompts_layout.algorithm_radio_group) {
                    check(
                        when (settings.algorithmForPrompts) {
                            AlgorithmSetting.RANDOM -> R.id.random_button
                            AlgorithmSetting.PATTERN -> R.id.pattern_button
                        }
                    )
                    setOnCheckedChangeListener { _, button ->
                        settings.algorithmForPrompts = when (button) {
                            R.id.pattern_button -> AlgorithmSetting.PATTERN
                            else -> AlgorithmSetting.RANDOM
                        }
                        persistSettings()
                    }
                }

                with(algorithm_for_prompts_layout.pattern_radio_group) {
                    check(
                        when (settings.patternSubSetting) {
                            PatternSubSetting.CHROMATIC -> R.id.chromatically_button
                            PatternSubSetting.IN_FIFTHS -> R.id.fifths_button
                            PatternSubSetting.IN_FOURTHS -> R.id.fourths_button
                        }
                    )
                    setOnCheckedChangeListener { _, button ->
                        settings.patternSubSetting = when (button) {
                            R.id.fifths_button -> PatternSubSetting.IN_FIFTHS
                            R.id.fourths_button -> PatternSubSetting.IN_FOURTHS
                            else -> PatternSubSetting.CHROMATIC
                        }
                        persistSettings()
                    }
                }
            }
        }
    }

    private fun persistSettings() {
        this@ChordDrillSettingsFragment.settings.persist()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChordDrillSettingsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChordDrillSettingsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}