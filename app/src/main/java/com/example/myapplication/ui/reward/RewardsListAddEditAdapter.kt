package com.example.myapplication.ui.reward

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.RewardWithChildren
import com.example.myapplication.databinding.RewardsRowItemBinding


class RewardsListAddEditAdapter(val rewardsViewModel: RewardsViewModel) : ListAdapter<RewardWithChildren, RewardsListAddEditAdapter.RewardViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RewardViewHolder {
        val binding = RewardsRowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RewardViewHolder(binding, rewardsViewModel)

    }

    override fun onBindViewHolder(holder: RewardsListAddEditAdapter.RewardViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class RewardViewHolder(private val binding: RewardsRowItemBinding, val rewardsViewModel: RewardsViewModel) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RewardWithChildren) = with(itemView) {
            binding.itemViewModel = item
            binding.viewModel = rewardsViewModel
        }
    }
}

class DiffCallback : DiffUtil.ItemCallback<RewardWithChildren>() {
    override fun areItemsTheSame(oldItem: RewardWithChildren, newItem: RewardWithChildren): Boolean {
        return oldItem?.reward?.rewardId == newItem?.reward?.rewardId
    }

    override fun areContentsTheSame(oldItem: RewardWithChildren, newItem: RewardWithChildren): Boolean {
        return oldItem == newItem
    }
}