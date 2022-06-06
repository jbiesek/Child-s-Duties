package com.example.myapplication.ui.reward


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.databinding.FragmentRewardsBinding
import com.example.myapplication.ui.BaseFragment

import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RewardsFragment : BaseFragment<RewardsViewModel>(RewardsViewModel::class.java) {

    private var _binding: FragmentRewardsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRewardsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var adapter = RewardsListAddEditAdapter(this.viewModel)

        binding.viewModel = this.viewModel
        binding.lifecycleOwner = this
        binding.recyclerViewRewards.adapter = adapter
        binding.viewModel?.rewardList?.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

    }
}