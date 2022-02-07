package com.tunepruner.musictraining.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.musictraining.R
import com.example.musictraining.databinding.FragmentChordDrillSettingsBinding
import com.tunepruner.musictraining.model.music.drill.items.AlgorithmSetting
import com.tunepruner.musictraining.model.music.drill.items.ChordQuality
import com.tunepruner.musictraining.model.music.drill.items.IntervalRequirements
import com.tunepruner.musictraining.model.music.drill.items.Inversion
import com.tunepruner.musictraining.model.music.drill.items.Key
import com.tunepruner.musictraining.model.music.drill.items.MAX_DOUBLING_AMOUNT
import com.tunepruner.musictraining.model.music.drill.items.MIN_DOUBLING_AMOUNT
import com.tunepruner.musictraining.model.music.drill.items.NoteDoublingRequirement
import com.tunepruner.musictraining.model.music.drill.items.PatternSubSetting
import com.tunepruner.musictraining.model.music.drill.items.RegisterRequirement
import com.tunepruner.musictraining.model.music.drill.items.SpacingRequirement
import com.tunepruner.musictraining.model.music.drill.items.TimeConstraint
import com.tunepruner.musictraining.model.music.drill.items.allIntervals
import com.tunepruner.musictraining.repositories.DrillSettingsRepository
import com.tunepruner.musictraining.viewmodel.DrillSettingsViewModel
import kotlinx.android.synthetic.main.add_interval_requirements_layout.view.*
import kotlinx.android.synthetic.main.algorithm_for_prompts_layout.view.*
import kotlinx.android.synthetic.main.choose_mode_layout.*
import kotlinx.android.synthetic.main.constrain_permitted_voicings_layout.*
import kotlinx.android.synthetic.main.fragment_chord_drill_settings.*
import kotlinx.android.synthetic.main.number_selector.view.*
import kotlinx.android.synthetic.main.select_inversions_layout.*
import kotlinx.android.synthetic.main.time_constraint_layout.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.java.KoinJavaComponent

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class ChordDrillSettingsFragment : Fragment() {
    private val args: ChordDrillSettingsFragmentArgs by navArgs()
    private val settingsViewModel: DrillSettingsViewModel by viewModel()
    private val drillSettings: DrillSettingsRepository by KoinJavaComponent.inject(
        DrillSettingsRepository::class.java
    )
    private var _binding: FragmentChordDrillSettingsBinding? = null
    private val binding: FragmentChordDrillSettingsBinding get() = _binding!!

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
        settingsViewModel.loadDrill(args.id)
        initializeViews()
    }

    @InternalCoroutinesApi
    private fun initializeViews() {

        settingsViewModel.isAddingName.observe(viewLifecycleOwner) {
            with(binding.startDrillButton) {
                text = if (it) {
                    setOnClickListener {
                        settingsViewModel.stopAddingName()
                    }
                    context?.getText(R.string.cancel)
                } else {
                    setOnClickListener {
                        findNavController().navigate(R.id.action_chord_drill_settings_to_drill)
                    }
                    context?.getText(R.string.start_drill)
                }
            }

            with(binding.saveDrillButton) {
                text = if (it) {
                    setOnClickListener {
                        settingsViewModel.stopAddingName()
                        settingsViewModel.saveDrill(binding.nameEditText.text.toString())
                    }
                    context?.getText(R.string.save)
                } else {
                    setOnClickListener {
                        settingsViewModel.promptIfNeedsName()
                    }
                    binding.nameEditText.visibility = View.GONE
                    context?.getText(R.string.save_drill)
                }
            }
            binding.nameEditText.visibility = if (it) View.VISIBLE else View.GONE
            binding.nameTextView.visibility = if (it) View.GONE else View.VISIBLE
        }

        drillSettings.current.value.let { drill ->
            drill.chordDrill?.let { chordDrill ->
                binding.nameEditText.setText(chordDrill.id)
                binding.nameTextView.text = chordDrill.id

                with(binding) {
                    with(time_constraint_radio_group) {
                        settingsViewModel.timeConstraint.observe(viewLifecycleOwner) {
                            check(
                                when (it) {
                                    TimeConstraint.RAPID_FIRE -> R.id.rapid_fire_radio_button
                                    else -> R.id.metronome_radio_button
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
                            when (chordDrill.intervalRequirements) {
                                IntervalRequirements.LESS_THAN -> {
                                    current_value.text = chordDrill.intervalLessThanValue.uiName
                                    R.id.less_than_button
                                }
                                IntervalRequirements.GREATER_THAN -> {
                                    current_value.text = chordDrill.intervalGreaterThanValue.uiName
                                    R.id.greater_than_button
                                }
                                else -> {
                                    current_value.text = "-"
                                    R.id.none_button
                                }
                            }
                        )

                        radio_group.setOnCheckedChangeListener { _, button ->
                            when (button) {
                                R.id.none_button -> {
                                    chordDrill.intervalRequirements = IntervalRequirements.NONE
                                    current_value.text = "-"
                                }
                                R.id.less_than_button -> {
                                    chordDrill.intervalRequirements = IntervalRequirements.LESS_THAN
                                    current_value.text = chordDrill.intervalLessThanValue.uiName
                                }
                                R.id.greater_than_button -> {
                                    chordDrill.intervalRequirements =
                                        IntervalRequirements.GREATER_THAN
                                    current_value.text = chordDrill.intervalGreaterThanValue.uiName
                                }
                            }
                            persistSettings()
                        }
                        up_button.setOnClickListener {
                            if (chordDrill.intervalRequirements == IntervalRequirements.LESS_THAN) {
                                val currentIndex: Int =
                                    allIntervals.indexOf(chordDrill.intervalLessThanValue)
                                if (currentIndex < allIntervals.size - 1) {
                                    chordDrill.intervalLessThanValue =
                                        allIntervals[currentIndex + 1]
                                    current_value.text = chordDrill.intervalLessThanValue.uiName
                                }
                            } else if (chordDrill.intervalRequirements == IntervalRequirements.GREATER_THAN) {
                                val currentIndex: Int =
                                    allIntervals.indexOf(chordDrill.intervalGreaterThanValue)
                                if (currentIndex < allIntervals.size - 1) {
                                    chordDrill.intervalGreaterThanValue =
                                        allIntervals[currentIndex + 1]
                                    current_value.text = chordDrill.intervalGreaterThanValue.uiName
                                }
                            }
                            persistSettings()
                        }
                        down_button.setOnClickListener {
                            if (chordDrill.intervalRequirements == IntervalRequirements.LESS_THAN) {
                                val currentIndex: Int =
                                    allIntervals.indexOf(chordDrill.intervalLessThanValue)
                                if (currentIndex > 1) {
                                    chordDrill.intervalLessThanValue =
                                        allIntervals[currentIndex - 1]
                                    current_value.text = chordDrill.intervalLessThanValue.uiName
                                }
                            } else if (chordDrill.intervalRequirements == IntervalRequirements.GREATER_THAN) {
                                val currentIndex: Int =
                                    allIntervals.indexOf(chordDrill.intervalGreaterThanValue)
                                if (currentIndex > 1) {
                                    chordDrill.intervalGreaterThanValue =
                                        allIntervals[(currentIndex - 1)]
                                    current_value.text = chordDrill.intervalGreaterThanValue.uiName
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
                            element.value.isChecked =
                                chordDrill.inversions.contains(element.key) == true
                            element.value.setOnCheckedChangeListener { compoundButton, _ ->
                                if (compoundButton.isChecked) {
                                    chordDrill.inversions.add(element.key)
                                } else {
                                    chordDrill.inversions.remove(element.key)
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
                            element.value.isChecked =
                                chordDrill.chordQualities.contains(element.key) == true
                            element.value.setOnCheckedChangeListener { compoundButton, _ ->
                                if (compoundButton.isChecked) {
                                    chordDrill.chordQualities.add(element.key)
                                } else {
                                    chordDrill.chordQualities.remove(element.key)
                                }
                                persistSettings()
                            }
                        }
                    }

                    with(note_doubling_requirement_layout) {
                        radio_group.apply {
                            check(
                                when (chordDrill.noteDoublingRequirement) {
                                    NoteDoublingRequirement.SPECIFIC_AMOUNT -> R.id.specific_amount_button
                                    else -> R.id.scale_mode_radio_button
                                }
                            )
                            setOnCheckedChangeListener { _, button ->
                                chordDrill.noteDoublingRequirement =
                                    when (button) {
                                        R.id.specific_amount_button -> NoteDoublingRequirement.SPECIFIC_AMOUNT
                                        else -> NoteDoublingRequirement.NONE
                                    }
                                persistSettings()
                            }
                        }

                        current_value.text = chordDrill.noteDoublingAmount.toString()

                        up_button.setOnClickListener {
                            if (chordDrill.noteDoublingRequirement == NoteDoublingRequirement.SPECIFIC_AMOUNT) {
                                if (chordDrill.noteDoublingAmount < MAX_DOUBLING_AMOUNT) {
                                    chordDrill.noteDoublingAmount
                                }
                                current_value.text = chordDrill.noteDoublingAmount.toString()
                                persistSettings()
                            }
                        }
                        down_button.setOnClickListener {
                            if (chordDrill.noteDoublingRequirement == NoteDoublingRequirement.SPECIFIC_AMOUNT) {
                                if (chordDrill.noteDoublingAmount > MIN_DOUBLING_AMOUNT) {
                                    chordDrill.noteDoublingAmount--
                                }
                                current_value.text = chordDrill.noteDoublingAmount.toString()
                                persistSettings()
                            }
                        }
                    }

                    with(voicing_spacing_requirement_layout.radio_group) {
                        check(
                            when (chordDrill.spacingRequirement) {
                                SpacingRequirement.OPEN_VOICING -> R.id.closed_voicing
                                SpacingRequirement.CLOSED_VOICING -> R.id.open_voicing
                                else -> R.id.none
                            }
                        )
                        setOnCheckedChangeListener { _, button ->
                            chordDrill.spacingRequirement = when (button) {
                                R.id.closed_voicing -> SpacingRequirement.CLOSED_VOICING
                                R.id.open_voicing -> SpacingRequirement.OPEN_VOICING
                                else -> SpacingRequirement.NONE
                            }
                            persistSettings()
                        }
                    }

                    with(register_requirement_layout.radio_group) {
                        check(
                            when (chordDrill.registerRequirement) {
                                RegisterRequirement.REQUIRE_VOICE_LEADING -> R.id.voice_leading
                                RegisterRequirement.REQUIRE_COMMON_TOP_NOTE -> R.id.common_top_note
                                RegisterRequirement.REQUIRE_COMMON_BOTTOM_NOTE -> R.id.common_bottom_note
                                RegisterRequirement.REQUIRE_LEAP_GREATER_THAN_5TH -> R.id.leap_greater_than
                                else -> R.id.none
                            }
                        )
                        setOnCheckedChangeListener { _, button ->
                            chordDrill.registerRequirement = when (button) {
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
                            element.value.isChecked =
                                settingsViewModel.currentDrill.value?.keys?.contains(element.key) == true
                            element.value.setOnCheckedChangeListener { compoundButton, _ ->
                                if (compoundButton.isChecked) {
                                    settingsViewModel.currentDrill.value?.keys?.add(element.key)
                                } else {
                                    settingsViewModel.currentDrill.value?.keys?.remove(element.key)
                                }
                                persistSettings()
                            }
                        }
                    }

                    with(algorithm_for_prompts_layout.algorithm_radio_group) {
                        check(
                            when (drill.algorithmForPrompts) {
                                AlgorithmSetting.RANDOM -> R.id.random_button
                                AlgorithmSetting.PATTERN -> R.id.pattern_button
                            }
                        )
                        setOnCheckedChangeListener { _, button ->
                            drill.algorithmForPrompts = when (button) {
                                R.id.pattern_button -> AlgorithmSetting.PATTERN
                                else -> AlgorithmSetting.RANDOM
                            }
                            persistSettings()
                        }
                    }

                    with(algorithm_for_prompts_layout.pattern_radio_group) {
                        check(
                            when (drill.patternSubSetting) {
                                PatternSubSetting.CHROMATIC -> R.id.chromatically_button
                                PatternSubSetting.IN_FIFTHS -> R.id.fifths_button
                                PatternSubSetting.IN_FOURTHS -> R.id.fourths_button
                            }
                        )
                        setOnCheckedChangeListener { _, button ->
                            drill.patternSubSetting = when (button) {
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
    }

    private fun persistSettings() {
        this@ChordDrillSettingsFragment.drillSettings.persist()
    }
}