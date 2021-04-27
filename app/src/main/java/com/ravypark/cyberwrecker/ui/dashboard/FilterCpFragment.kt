package com.ravypark.cyberwrecker.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Transformations
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ravypark.cyberwrecker.R
import com.ravypark.cyberwrecker.data.ContentProvider
import com.ravypark.cyberwrecker.databinding.ViewholderFilterCpBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_filter_cp.view.*

@AndroidEntryPoint
class FilterCpFragment : BottomSheetDialogFragment() {

    private val viewModel by viewModels<FilterCpViewModel>()

    private var adapter: FilterCpAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_filter_cp, container, false)

        adapter = FilterCpAdapter(viewModel)
        view.list_filter_cp.adapter = adapter

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Transformations.map(viewModel.filterCpChangedEvent) {
            viewModel.mapFilterCps(it.peekContent())
        }.observe(viewLifecycleOwner, {
            adapter?.submitList(it)
        })
    }
}

class FilterCpAdapter(private val filterCpViewModel: FilterCpViewModel) :
    ListAdapter<FilterCpItemViewModel, FilterCpViewHolder>(FilterCpDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterCpViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
            ViewholderFilterCpBinding.inflate(layoutInflater, parent, false)
        return FilterCpViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilterCpViewHolder, position: Int) {
        holder.bind(filterCpViewModel, getItem(position))
    }
}

class FilterCpDiffCallback : DiffUtil.ItemCallback<FilterCpItemViewModel>() {

    override fun areItemsTheSame(
        oldItem: FilterCpItemViewModel,
        newItem: FilterCpItemViewModel
    ): Boolean {
        return oldItem.cp.id == newItem.cp.id
    }

    override fun areContentsTheSame(
        oldItem: FilterCpItemViewModel,
        newItem: FilterCpItemViewModel
    ): Boolean {
        return oldItem == newItem
    }

}

class FilterCpViewHolder(private val binding: ViewholderFilterCpBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(viewModel: FilterCpViewModel, itemViewModel: FilterCpItemViewModel) {
        binding.viewModel = viewModel
        binding.itemViewModel = itemViewModel
        binding.executePendingBindings()
    }
}

data class FilterCpItemViewModel(val cp: ContentProvider, var isChecked: Boolean)