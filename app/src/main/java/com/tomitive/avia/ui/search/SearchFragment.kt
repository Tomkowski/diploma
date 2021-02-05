package com.tomitive.avia.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tomitive.avia.R
import com.tomitive.avia.model.classrooms
import com.tomitive.avia.utils.MarginItemDecoration
import com.tomitive.avia.utils.afterTextChanged
import kotlinx.android.synthetic.main.fragment_search.view.*

class SearchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_search, container, false)
        val searchList = rootView.findViewById<RecyclerView>(R.id.search_list)

        initRecyclerView(searchList)

        rootView.navigation_search_edit_text.afterTextChanged { searchText ->
            (searchList.adapter as SearchViewAdapter).updateSearchResults(
                classrooms.filter { it.contains(searchText.trim()) }
            )

            Log.d("searchview", "listener set")
        }
        return rootView
    }

    private fun initRecyclerView(searchList: RecyclerView) {
        searchList.adapter = SearchViewAdapter(requireContext())
        searchList.addItemDecoration(
            MarginItemDecoration(resources.getDimension(R.dimen.recyclerview_item_padding).toInt())
        )
        searchList.layoutManager = LinearLayoutManager(requireContext())
        searchList.setHasFixedSize(true)
        Log.d("searchview", "all set")
    }
}