package com.example.myapplication.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentChildAddEditBinding
import com.example.myapplication.ui.BaseFragment
import com.example.myapplication.ui.notifyObserver
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.imageview.ShapeableImageView
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ChildAddEditFragment : BaseFragment<ChildAddEditViewModel>(ChildAddEditViewModel::class.java) {

    private var _binding: FragmentChildAddEditBinding? = null

    private val binding get() = _binding!!

    private val circleLinearLayoutMap = mutableMapOf<String, LinearLayout>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChildAddEditBinding.inflate(inflater, container, false)
        createAvatarsCardToGridLayout()

        return binding.root
    }

    private fun createAvatarsCardToGridLayout() {
        var drawableList = listOf(
            Pair(R.drawable.avatar_cat, "avatar_cat"),
            Pair(R.drawable.avatar_dog, "avatar_dog"),
            Pair(R.drawable.avatar_monster, "avatar_monster"),
            Pair(R.drawable.avatar_pikachu, "avatar_pikachu"),
            Pair(R.drawable.avatar_horse, "avatar_horse")
        )

        circleLinearLayoutMap.clear()

        for (i in drawableList.indices) {
            val cardView: CardView = LayoutInflater.from(context)
                .inflate(R.layout.item_avatar, binding.gridLayoutAvatarList, false) as CardView

            cardView.findViewWithTag<ShapeableImageView>("ShapeableImageView")?.setImageResource(drawableList[i].first)
            val myGLP = androidx.gridlayout.widget.GridLayout.LayoutParams()
            val rowSpec: androidx.gridlayout.widget.GridLayout.Spec = androidx.gridlayout.widget.GridLayout.spec(i / 4, 1, 0.25f)
            val colSpec: androidx.gridlayout.widget.GridLayout.Spec = androidx.gridlayout.widget.GridLayout.spec(i % 4, 1, 0.25f)

            circleLinearLayoutMap[drawableList[i].second] = findViewWithTagRecursively(cardView, "layoutWithCircle")[0] as LinearLayout

            myGLP.rowSpec = rowSpec
            myGLP.columnSpec = colSpec
            binding.gridLayoutAvatarList.addView(cardView, myGLP)
            cardView.setOnClickListener {
                viewModel.child.value!!.drawableName = drawableList[i].second
                viewModel.child.notifyObserver() //notify about changed value
            }
        }
        }



    fun findViewWithTagRecursively(root: ViewGroup, tag: Any): List<View> {
        val allViews: MutableList<View> = ArrayList()
        val childCount = root.childCount
        for (i in 0 until childCount) {
            val childView = root.getChildAt(i)
            if (childView is ViewGroup) {
                allViews.addAll(findViewWithTagRecursively(childView, tag)!!)
            }
    //            else {
            val tagView = childView.tag
            if (tagView != null && tagView == tag) allViews.add(childView)
    //            }
        }
        return allViews
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = this.viewModel
        binding.lifecycleOwner = this
        binding.viewModel!!.child.observe(viewLifecycleOwner) {
            refreshAvatarSelection()
        }
        observerShowDataPickerRequest()
    }

    private fun observerShowDataPickerRequest() {
        viewModel.showDatePickerRequest.observe(this.viewLifecycleOwner) {
            showBirthdayDataPicker()
        }
    }

    private fun showBirthdayDataPicker() {
        val title = resources.getString(R.string.select_birthday)
        val datePicker = MaterialDatePicker.Builder.datePicker().setTitleText(title).build()
        datePicker.addOnPositiveButtonClickListener { value ->
            viewModel.child.value?.let { child ->
                child.birthday = Date(value)
                viewModel.child.notifyObserver()
            }
        }

        datePicker.show(this.parentFragmentManager, "MY_DATE_PICKER_TAG")
    }

    fun refreshAvatarSelection() {
        val selectedAvatarName = viewModel.child.value?.drawableName
        for (entry in circleLinearLayoutMap.entries) {
            if (entry.key == selectedAvatarName)
                entry.value.setBackgroundResource(R.drawable.circle)
            else
                entry.value.setBackgroundResource(R.drawable.circle_gray)
        }
    }
}