package com.example.musictraining

import OnClickListener
import SavedDrillsAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musictraining.databinding.FragmentSavedDrillsBinding
import com.tunepruner.musictraining.model.music.drill.Drill
import com.tunepruner.musictraining.model.music.drill.DrillType
import com.tunepruner.musictraining.viewmodel.DrillListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SavedDrillsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SavedDrillsFragment : Fragment() {
    private val viewModel: DrillListViewModel by viewModel()
    private var _binding: FragmentSavedDrillsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedDrillsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = null
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerView.adapter = adapter

        viewModel.allDrills.observe(viewLifecycleOwner) {
            binding.recyclerView.adapter =
                SavedDrillsAdapter(ArrayList(it), object : OnClickListener {
                    override fun onClick(view: View, drill: Drill) {
                        if (drill.drillType() == DrillType.CHORD) {
                            val action =
                                SavedDrillsFragmentDirections
                                    .actionSavedDrillsFragmentToChordDrillSettingsFragment(
                                        drill.id
                                    )
                            findNavController().navigate(action)
                        } else {
                            val action =
                                SavedDrillsFragmentDirections
                                    .actionSavedDrillsFragmentToScaleDrillSettingsFragment(
                                        drill.id
                                    )
                            findNavController().navigate(action)
                        }
                    }
                })
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllChordDrills()
    }
}