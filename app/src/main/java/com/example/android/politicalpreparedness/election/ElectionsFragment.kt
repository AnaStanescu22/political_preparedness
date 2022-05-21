package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election

class ElectionsFragment : Fragment() {

    private val viewModel by viewModels<ElectionsViewModel> {
        ElectionsViewModelFactory(
            ElectionDatabase.getInstance(requireContext()).electionDao,
            CivicsApi.retrofitService
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentElectionBinding.inflate(inflater)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner

            electionsViewModel = viewModel

            recyclerUpcoming.adapter = ElectionListAdapter(ElectionListAdapter.ElectionListener {
                viewModel.displayVoterInfo(it)
            })

            recyclerSaved.adapter = ElectionListAdapter(ElectionListAdapter.ElectionListener {
                viewModel.displayVoterInfo(it)
            })
        }

        viewModel.navigateToVoterInfo.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                navigateToDetailFragment(it)
                viewModel.displayVoterInfoComplete()
            }
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
    }

    private fun navigateToDetailFragment(election: Election) {
        this.findNavController().navigate(
            ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(
                election.id,
                election.division
            )
        )
    }
}