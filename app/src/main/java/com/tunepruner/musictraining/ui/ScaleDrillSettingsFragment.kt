package com.tunepruner.musictraining.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.musictraining.R
import com.example.musictraining.databinding.FragmentScaleDrillSettingsBinding
import com.tunepruner.musictraining.model.music.drill.items.*
import com.tunepruner.musictraining.viewmodel.ScaleDrillSettingsViewModel
import kotlinx.android.synthetic.main.add_interval_requirements_layout.view.*
import kotlinx.android.synthetic.main.algorithm_for_prompts_layout.view.*
import kotlinx.android.synthetic.main.fragment_chord_drill_settings.*
import kotlinx.android.synthetic.main.number_selector.view.*
import kotlinx.android.synthetic.main.time_constraint_layout.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ScaleDrillSettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class ScaleDrillSettingsFragment : Fragment() {
    private val settingsViewModel: ScaleDrillSettingsViewModel by viewModel()
    private var _binding: FragmentScaleDrillSettingsBinding? = null
    private val binding: FragmentScaleDrillSettingsBinding get() = _binding!!

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
        _binding = FragmentScaleDrillSettingsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
    }

    private fun initializeViews() {
        binding.startDrillButton.setOnClickListener {
            findNavController().navigate(R.id.action_scale_drill_settings_to_drill)
        }
        settingsViewModel.settings.observe(viewLifecycleOwner) { drill ->
            drill?.scaleDrill?.let { scaleDrill ->
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
                            when (scaleDrill.intervalRequirements) {
                                IntervalRequirements.NONE -> {
                                    current_value.text = "-"
                                    R.id.none_button
                                }
                                IntervalRequirements.LESS_THAN -> {
                                    current_value.text = scaleDrill.intervalLessThanValue.uiName
                                    R.id.less_than_button
                                }
                                IntervalRequirements.GREATER_THAN -> {
                                    current_value.text = scaleDrill.intervalGreaterThanValue.uiName
                                    R.id.greater_than_button
                                }
                            }
                        )

                        radio_group.setOnCheckedChangeListener { _, button ->
                            when (button) {
                                R.id.none_button -> {
                                    scaleDrill.intervalRequirements = IntervalRequirements.NONE
                                    current_value.text = "-"
                                }
                                R.id.less_than_button -> {
                                    scaleDrill.intervalRequirements = IntervalRequirements.LESS_THAN
                                    current_value.text = scaleDrill.intervalLessThanValue.uiName
                                }
                                R.id.greater_than_button -> {
                                    scaleDrill.intervalRequirements =
                                        IntervalRequirements.GREATER_THAN
                                    current_value.text = scaleDrill.intervalGreaterThanValue.uiName
                                }
                            }
                        }
                        up_button.setOnClickListener {
                            if (scaleDrill.intervalRequirements == IntervalRequirements.LESS_THAN) {
                                val currentIndex: Int =
                                    allIntervals.indexOf(scaleDrill.intervalLessThanValue)
                                if (currentIndex < allIntervals.size - 1) {
                                    scaleDrill.intervalLessThanValue =
                                        allIntervals[currentIndex + 1]
                                    current_value.text = scaleDrill.intervalLessThanValue.uiName
                                }
                            } else if (scaleDrill.intervalRequirements == IntervalRequirements.GREATER_THAN) {
                                val currentIndex: Int =
                                    allIntervals.indexOf(scaleDrill.intervalGreaterThanValue)
                                if (currentIndex < allIntervals.size - 1) {
                                    scaleDrill.intervalGreaterThanValue =
                                        allIntervals[currentIndex + 1]
                                    current_value.text = scaleDrill.intervalGreaterThanValue.uiName
                                }
                            }
                        }
                        down_button.setOnClickListener {
                            if (scaleDrill.intervalRequirements == IntervalRequirements.LESS_THAN) {
                                val currentIndex: Int =
                                    allIntervals.indexOf(scaleDrill.intervalLessThanValue)
                                if (currentIndex > 1) {
                                    scaleDrill.intervalLessThanValue =
                                        allIntervals[currentIndex - 1]
                                    current_value.text = scaleDrill.intervalLessThanValue.uiName
                                }
                            } else if (scaleDrill.intervalRequirements == IntervalRequirements.GREATER_THAN) {
                                val currentIndex: Int =
                                    allIntervals.indexOf(scaleDrill.intervalGreaterThanValue)
                                if (currentIndex > 1) {
                                    scaleDrill.intervalGreaterThanValue =
                                        allIntervals[(currentIndex - 1)]
                                    current_value.text = scaleDrill.intervalGreaterThanValue.uiName
                                }
                            }
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
                            element.value.isChecked = drill.keys.contains(element.key)
                            element.value.setOnCheckedChangeListener { compoundButton, _ ->
                                if (compoundButton.isChecked) {
                                    drill.keys.add(element.key)
                                } else {
                                    drill.keys.remove(element.key)
                                }
                            }
                        }
                    }

                    with(algorithm_for_prompts_layout.algorithm_radio_group) {
                        check(
                            when (drill.algorithmForPrompts) {
                                AlgorithmSetting.PATTERN -> R.id.pattern_button
                                else -> R.id.random_button
                            }
                        )
                        setOnCheckedChangeListener { _, button ->
                            drill.algorithmForPrompts = when (button) {
                                R.id.pattern_button -> AlgorithmSetting.PATTERN
                                else -> AlgorithmSetting.RANDOM
                            }
                        }
                    }

                    with(algorithm_for_prompts_layout.pattern_radio_group) {
                        check(
                            when (drill.patternSubSetting) {
                                PatternSubSetting.IN_FIFTHS -> R.id.fifths_button
                                PatternSubSetting.IN_FOURTHS -> R.id.fourths_button
                                else -> R.id.chromatically_button
                            }
                        )
                        setOnCheckedChangeListener { _, button ->
                            drill.patternSubSetting = when (button) {
                                R.id.fifths_button -> PatternSubSetting.IN_FIFTHS
                                R.id.fourths_button -> PatternSubSetting.IN_FOURTHS
                                else -> PatternSubSetting.CHROMATIC
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ScaleDrillSettingsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ScaleDrillSettingsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}