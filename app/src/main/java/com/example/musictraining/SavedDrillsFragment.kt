package com.example.musictraining

import SavedDrillsAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.musictraining.databinding.FragmentSavedDrillsBinding
import com.tunepruner.musictraining.model.music.drill.ChordDrill
import com.tunepruner.musictraining.repositories.LOG_TAG
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
        val adapter = SavedDrillsAdapter(arrayOf(ChordDrill("testing")))
        binding.recyclerView.adapter = adapter
        viewModel.allDrills.observe(viewLifecycleOwner) {
            adapter.updateData(ArrayList(it))
        }
    }
}