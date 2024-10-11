package com.example.socialapp.fragments

import android.app.ActionBar
import android.app.ActionBar.DISPLAY_HOME_AS_UP
import android.app.ActionBar.DISPLAY_SHOW_HOME
import android.app.AlertDialog
import android.app.Notification
import android.content.Context
import android.net.wifi.hotspot2.pps.HomeSp
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.Fragment
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBar.DISPLAY_HOME_AS_UP
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import com.example.socialapp.ListViewModel
import com.example.socialapp.NavigationListener
import com.example.socialapp.R
import com.example.socialapp.apiservices.UserDetails
import javax.security.auth.login.LoginException


class DetailFragment : Fragment() {
    private var details: UserDetails? = null
    private lateinit var viewModel: ListViewModel
    private lateinit var navigationListener: NavigationListener
    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigationListener = context as NavigationListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)


        details = arguments?.getSerializable("detail") as UserDetails
        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        val toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.detail_toolbar)
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        (activity as? AppCompatActivity)?.supportActionBar?.show()
        toolbar.title = ""
        Log.i(
            "TAG",
            "name of user is : ${details?.name} and gender is :${details?.gender} from DetailFragment"
        )
        (activity as AppCompatActivity).supportActionBar?.apply {
            title = ""
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[ListViewModel::class.java]
        val name = view.findViewById<TextView>(R.id.tvd_name)
        val id = view.findViewById<TextView>(R.id.tvd_id)
        val email = view.findViewById<TextView>(R.id.tvd_mail)
        val gender = view.findViewById<TextView>(R.id.tvd_gender)
        val status = view.findViewById<TextView>(R.id.tvd_status)
        val back = view.findViewById<ImageView>(R.id.back)

        name.text = details?.name
        id.text = details?.id.toString()
        email.text = details?.email
        gender.text = details?.gender
        status.text = details?.status
        back.setOnClickListener {
            activity?.onBackPressed()
            Log.i("TAG", "wefwefwfwefwefefcwef ")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.detail_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.mi_edit -> {
                navigationListener.navigateToEditProfileFragment(details)
            }

            R.id.mi_Delete -> deleteDialog().show()

        }
        return true
    }

    private fun deleteDialog() =
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Profile")
            .setMessage("Do you want to delete this profile?")
            .setIcon(R.drawable.ic_baseline_delete_24)
            .setPositiveButton("Yes") { _, _ ->
                viewModel.deleteUser(details?.id as Int)
                viewModel.data.observe(viewLifecycleOwner) {
                    if ((it > 199) && (it < 300)) {
                        navigationListener.popupFragment()

                        //navigationListener.navigateToUserFragment()
                        Toast.makeText(
                            requireContext(),
                            "${details?.name.toString()} is deleted successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(requireContext(), "$it is the response ", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
            .setNegativeButton("No") { _, _ ->
            }.create()

    companion object {
        fun newInstance(param1: UserDetails): DetailFragment {
            return DetailFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("detail", param1)
                }
            }
        }
    }
}