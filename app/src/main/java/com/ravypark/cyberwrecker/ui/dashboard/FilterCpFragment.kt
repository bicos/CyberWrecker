package com.ravypark.cyberwrecker.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ravypark.cyberwrecker.R
import com.ravypark.cyberwrecker.data.ContentProvider
import com.ravypark.cyberwrecker.databinding.ViewholderFilterCpBinding
import kotlinx.android.synthetic.main.fragment_filter_cp.view.*

class FilterCpFragment : BottomSheetDialogFragment() {

    lateinit var adapter: FilterCpAdapter

    private val viewModel: FilterCpViewModel by viewModels { ViewModelProvider.NewInstanceFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_filter_cp, container, false)
        adapter = FilterCpAdapter()
        view.list_filter_cp.adapter = adapter
        adapter.submitList(viewModel.getConfigs())
        return view
    }
}

class FilterCpAdapter : ListAdapter<ContentProvider, FilterCpViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ContentProvider>() {
            override fun areItemsTheSame(
                oldItem: ContentProvider,
                newItem: ContentProvider
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ContentProvider,
                newItem: ContentProvider
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
        holder.bind(getItem(position))
    }
}

class FilterCpViewHolder(private val binding: ViewholderFilterCpBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(cp: ContentProvider) {
        binding.board = cp
        binding.executePendingBindings()
    }
}