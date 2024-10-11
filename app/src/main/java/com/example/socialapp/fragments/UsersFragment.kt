package com.example.socialapp.fragments

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.socialapp.ListViewModel
import com.example.socialapp.NavigationListener
import com.example.socialapp.R
import com.example.socialapp.R.color.*
import com.example.socialapp.adapter.ListAdapter


class UsersFragment : Fragment() {

    private var progressBar: ProgressBar? = null
    private var recyclerView: RecyclerView? = null
    private lateinit var viewModel: ListViewModel
    private lateinit var navigationListener: NavigationListener
    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigationListener = context as NavigationListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_users, container, false)
        recyclerView = view.findViewById(R.id.users_recycle_view)
        recyclerView?.layoutManager = LinearLayoutManager(context)

        val toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.detail_toolbar)
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        (activity as? AppCompatActivity)?.supportActionBar?.show()
        toolbar.title = ""
        (activity as AppCompatActivity).supportActionBar?.apply {
            title = ""
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[ListViewModel::class.java]
        val swipe = view.findViewById<SwipeRefreshLayout>(R.id.swipe)
        swipe.setProgressBackgroundColorSchemeColor(resources.getColor(gray,resources.newTheme()))
        swipe.setColorSchemeResources(white)

        progressBar = view.findViewById(R.id.progressBar)
        progressBar?.isVisible = true

        viewModel.getDetails()
        viewModel.listData.observe(viewLifecycleOwner) { list ->
            if (list.isNotEmpty()) {
                swipe.setOnRefreshListener{
                    viewModel.getDetails()
                    if (list.isNotEmpty()) {
                        swipe.isRefreshing = false
                    }
                }
                progressBar?.isVisible = false
                val adapter = ListAdapter(list) {
                    navigationListener.navigateToDetailFragment(it)
                }
                recyclerView?.adapter = adapter
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.app_bar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mi_add_person -> navigationListener.navigateToAddProfileFragment()
        }
        return true
    }
}