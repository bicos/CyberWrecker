package com.ravypark.cyberwrecker.ui.dashboard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.arch.core.util.Function
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ravypark.cyberwrecker.R
import com.ravypark.cyberwrecker.data.ContentProvider
import com.ravypark.cyberwrecker.databinding.ViewholderFilterCpBinding
import com.ravypark.cyberwrecker.utils.EventObserver
import kotlinx.android.synthetic.main.fragment_filter_cp.view.*

class FilterCpFragment : BottomSheetDialogFragment() {

    lateinit var adapter: FilterCpAdapter

    private lateinit var viewModel: FilterCpViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)

        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(
            requireActivity().application
        ).create(FilterCpViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_filter_cp, container, false)
        adapter = FilterCpAdapter(viewModel)
        view.list_filter_cp.adapter = adapter

        Transformations.map(viewModel.filterCpChangedEvent) {
            viewModel.mapFilterCps(it.peekContent())
        }.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        return view
    }
}

class FilterCpAdapter(private val filterCpViewModel: FilterCpViewModel) :
    ListAdapter<FilterCpItemViewModel, FilterCpViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FilterCpItemViewModel>() {
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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterCpViewHolder {
        val binding =
            ViewholderFilterCpBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilterCpViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilterCpViewHolder, position: Int) {
        holder.bind(filterCpViewModel, getItem(position))
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