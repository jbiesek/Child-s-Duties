package com.example.myapplication.ui.reward
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentRewardAddEditBinding
import com.example.myapplication.ui.BaseFragment
import com.example.myapplication.ui.notifyObserver
import com.example.myapplication.utils.ResourceUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RewardAddEditFragment : BaseFragment<RewardAddEditViewModel>(RewardAddEditViewModel::class.java) {

    private var _binding: FragmentRewardAddEditBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRewardAddEditBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = this.viewModel;
        binding.lifecycleOwner = this;
        viewModel.reward.observe(this.viewLifecycleOwner, Observer {
            binding.sliderBehaviour.value = it.reward.behaviourPoints.toFloat()
            binding.sliderDuty.value = it.reward.dutyPoints.toFloat()
        })
        binding.resourceUtils = ResourceUtils

        binding.sliderBehaviour.addOnChangeListener { slider, value, fromUser ->
            viewModel.reward.value?.reward?.behaviourPoints = value.toInt()
            viewModel.reward.notifyObserver()
        }
        binding.sliderDuty.addOnChangeListener { slider, value, fromUser ->
            viewModel.reward.value?.reward?.dutyPoints = value.toInt()
            viewModel.reward.notifyObserver()
        }

        observeDeleteDialogRequest()
    }


    private fun observeDeleteDialogRequest() {
        viewModel.deleteRewardDialogRequest.observe(this.viewLifecycleOwner) {
            MaterialAlertDialogBuilder(requireContext())
                .setMessage(getString(R.string.are_you_sure_delete_reward).format(viewModel.deleteRewardDialogRequest.value!!.reward.name))
                .setTitle(getString(R.string.are_you_sure))
                .setNegativeButton(R.string.no) { dialog, which ->
                    //
                }
                .setPositiveButton(R.string.yes) { dialog, which ->
                    viewModel.deleteReward(viewModel.deleteRewardDialogRequest.value!!)
                }
                .show()
        }

    }


}