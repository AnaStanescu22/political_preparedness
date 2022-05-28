package com.example.android.politicalpreparedness.voter_info

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.utils.CivicsApiStatus
import timber.log.Timber

class VoterInfoFragment : Fragment() {

    private val viewModel by viewModels<VoterInfoViewModel> {
        VoterInfoViewModelFactory(ElectionDatabase.getInstance(requireContext()).electionDao, CivicsApi.retrofitService)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val voterInfoFragmentArgs = VoterInfoFragmentArgs.fromBundle(requireArguments())
        val electionId = voterInfoFragmentArgs.argElectionId
        val division = voterInfoFragmentArgs.argDivision

        Timber.d("electionId: %s", electionId)
        Timber.d("division: %s", division)

        val binding = FragmentVoterInfoBinding.inflate(layoutInflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            voterInfoViewModel = viewModel
        }

        viewModel.getVoterInformation(electionId, division)

        viewModel.apiStatus.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it == CivicsApiStatus.ERROR) showRequestErrorDialog()
            }
        })

        viewModel.url.observe(viewLifecycleOwner, Observer { it ->
            it?.let {
                loadUrl(it)
                viewModel.navigateToUrlCompleted()
            }
        })
        return binding.root
    }

    private fun showRequestErrorDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Error")
            .setMessage("The voter information can't be retrieved. Click OK to go back.")
            .setCancelable(false)
            .setPositiveButton("Ok") { dialog, _ ->
                dialog.dismiss()
                this.findNavController().popBackStack()
            }.show()
    }

    private fun loadUrl(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
//        viewModel.navigateToUrlCompleted()
    }
}