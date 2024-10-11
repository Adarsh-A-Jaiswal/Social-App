package com.example.socialapp.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuItemImpl
import androidx.lifecycle.ViewModelProvider
import com.example.socialapp.ListViewModel
import com.example.socialapp.NavigationListener
import com.example.socialapp.R
import com.example.socialapp.apiservices.PostData
import com.example.socialapp.apiservices.UserDetails
import com.google.android.material.textfield.TextInputLayout

class EditProfileFragment : Fragment() {

    private var item: MenuItem? = null
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z-]+\\.+[a-z]+"
    private var nameTextField: TextInputLayout? = null
    private var emailTextField: TextInputLayout? = null
    private var name: EditText? = null
    private var email: EditText? = null
    private var gender: Spinner? = null
    private var status: Spinner? = null
    private lateinit var viewModel: ListViewModel
    private var detail: UserDetails? = null
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
        detail = arguments?.getSerializable("detail") as UserDetails
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_profile, container, false)
        val toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.detail_toolbar)
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        (activity as? AppCompatActivity)?.supportActionBar?.show()
        toolbar.title = ""
        Log.i(
            "TAG",
            "user name =${detail?.name}, gender=${detail?.gender}, email=${detail?.email}, id=${detail?.id}"
        )
        (activity as AppCompatActivity).supportActionBar?.apply {
            title = ""
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[ListViewModel::class.java]

        val back = view.findViewById<ImageView>(R.id.back)
        name = view.findViewById(R.id.et_name)
        email = view.findViewById(R.id.et_Email)
        nameTextField = view.findViewById(R.id.nameTextInputLayout)
        emailTextField = view.findViewById(R.id.emailTextInputLayout)
        name?.setText(detail?.name)

        email?.setText(detail?.email)

        gender = view.findViewById(R.id.sp_gender)
        val genderList: Array<String> = view.resources.getStringArray((R.array.Gender))
        val position = genderList.indexOf(detail?.gender)
        gender?.setSelection(position)

        status = view.findViewById(R.id.sp_status)
        val statusList: Array<String> = view.resources.getStringArray((R.array.Status))
        val indexOfStatus = statusList.indexOf(detail?.status)
        status?.setSelection(indexOfStatus)

        back.setOnClickListener {
            Log.i("TAG", "back is clicked from edit fragment")
            backDialog().show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.save_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mi_save -> {
                Log.i("TAG", "${status?.selectedItem}, ${gender?.selectedItem}")
                if (name?.text!!.isNotEmpty() && email?.text!!.isNotEmpty()) {
                    if (email?.text.toString().matches(emailPattern.toRegex())) {

                        emailTextField?.let { setErrorTextField(false, it) }
                        nameTextField?.let { setErrorTextField(false, it) }
                        viewModel.putData(
                            detail?.id.toString().toInt(),
                            PostData(
                                detail?.id.toString().toInt(),
                                name?.text.toString(),
                                email?.text.toString(),
                                gender?.selectedItem as String,
                                status?.selectedItem as String
                            )
                        )
                        viewModel.putData.observe(viewLifecycleOwner) {
                            if (it.isSuccessful) {
                                navigationListener.popupFragment()
                                Toast.makeText(
                                    requireContext(),
                                    "Profile is Updated",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else {
                        setErrorTextField(true, emailTextField!!)
                        emailTextField?.error = "Invalid Email"
                    }
                    //setting error according to empty textField
                } else if (name?.text!!.isEmpty()) {
                    nameTextField?.let { setErrorTextField(true, it) }
                    emailTextField?.let { setErrorTextField(false, it) }
                } else if (email?.text!!.isEmpty()) {
                    emailTextField?.let { setErrorTextField(true, it) }
                    nameTextField?.let { setErrorTextField(false, it) }
                }
            }
        }
        return true
    }

    private fun setErrorTextField(error: Boolean, emptyTextInputLayout: TextInputLayout) {
        if (error) {
            emptyTextInputLayout.isErrorEnabled = true
            emptyTextInputLayout.error = "Field Can't be empty"
        } else {
            emptyTextInputLayout.isErrorEnabled = false
            emptyTextInputLayout.error = null
        }
    }

    private fun backDialog() =
        AlertDialog.Builder(requireContext())
            .setTitle("Exit")
            .setMessage("Do you want to go back?")
            .setPositiveButton("YES") { _, _ ->
                activity?.onBackPressed()
            }
            .setNegativeButton("NO") { _, _ ->
            }.create()

    companion object {
        @JvmStatic
        fun newInstance(param1: UserDetails?): EditProfileFragment {
            return EditProfileFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("detail", param1)
                }
            }
        }
    }
}