package com.ravypark.cyberwrecker.ui.dashboard

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.firebase.ui.firestore.paging.LoadingState
import com.google.firebase.firestore.Query
import com.ravypark.cyberwrecker.R
import com.ravypark.cyberwrecker.data.Feed
import com.ravypark.cyberwrecker.databinding.FragmentDashboardBinding
import com.ravypark.cyberwrecker.utils.EventObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private val dashboardViewModel: DashboardViewModel by viewModels()

    private val filterCpViewModel: FilterCpViewModel by viewModels()

    private lateinit var adapter: FirestorePagingAdapter<Feed, FeedListViewHolder>

    private var binding: FragmentDashboardBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPrefetchDistance(5)
            .setPageSize(30)
            .build()

        val options = FirestorePagingOptions.Builder<Feed>()
            .setLifecycleOwner(this)
            .setQuery(
                dashboardViewModel.collection.orderBy(
                    "createdAt",
                    Query.Direction.DESCENDING
                ), config, Feed::class.java
            )
            .build()

        adapter = FeedListAdapter(options, dashboardViewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater)
        binding?.feedList?.let {
            setupRecyclerView(it)
        }

        binding?.refresh?.setOnRefreshListener {
            adapter.refresh()
        }

        binding?.filter?.setOnClickListener {
            FilterCpFragment().show(childFragmentManager, "filter_cp")
        }

        return binding!!.root
    }

    private fun setupRecyclerView(feedList: RecyclerView) {
        feedList.setHasFixedSize(true)

        val deco = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL).apply {
            ContextCompat.getDrawable(requireContext(), R.drawable.divider_feed_list)?.let {
                setDrawable(it)
            }
        }
        feedList.addItemDecoration(deco)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        filterCpViewModel.filterCpChangedEvent.observe(viewLifecycleOwner, EventObserver {
            val config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(5)
                .setPageSize(30)
                .build()

            var query: Query = dashboardViewModel.collection

            if (it.isNotEmpty()) {
                Log.i("test", "cps : ${it.toList()}")
                query = query.whereIn("cp", it.toList())
            }

            query = query.orderBy("createdAt", Query.Direction.DESCENDING)

            val options = FirestorePagingOptions.Builder<Feed>()
                .setLifecycleOwner(this)
                .setQuery(query, config, Feed::class.java)
                .build()

            adapter.updateOptions(options)
        })

        dashboardViewModel.clickEvent.observe(viewLifecycleOwner, EventObserver {
            CustomTabsIntent.Builder()
                .build().launchUrl(requireContext(), Uri.parse(it.url))
        })

        dashboardViewModel.loadingState.observe(viewLifecycleOwner, {
            if (it == LoadingState.LOADED || it == LoadingState.ERROR) {
                binding?.refresh?.isRefreshing = false
                if (it == LoadingState.ERROR) {
                    Toast.makeText(requireContext(), "일시적으로 서비스를 이용할 수 없습니다.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })

        dashboardViewModel.start {
            binding?.feedList?.adapter = this.adapter
        }
    }
}