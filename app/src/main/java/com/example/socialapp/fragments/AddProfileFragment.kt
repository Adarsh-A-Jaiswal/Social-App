package com.example.socialapp.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.socialapp.ListViewModel
import com.example.socialapp.NavigationListener
import com.example.socialapp.R
import com.example.socialapp.apiservices.PostData
import com.google.android.material.textfield.TextInputLayout


class AddProfileFragment : Fragment() {

    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    private var nameTextField: TextInputLayout? = null
    private var emailTextField: TextInputLayout? = null
    private var progressBar: ProgressBar? = null
    private lateinit var navigationListener: NavigationListener
    private lateinit var viewModel: ListViewModel
    private var gen: String = ""
    private var stat: String = ""
    private var name: EditText? = null
    private var email: EditText? = null

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
        val view = inflater.inflate(R.layout.fragment_add_profile, container, false)
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

        val back = view.findViewById<ImageView>(R.id.back)
        name = view.findViewById(R.id.et_name)
        email = view.findViewById(R.id.et_Email)
        nameTextField = view.findViewById(R.id.nameTextInputLayout)
        emailTextField = view.findViewById(R.id.emailTextInputLayout)
        val gender: Spinner = view.findViewById(R.id.sp_gender)
        val status: Spinner = view.findViewById(R.id.sp_status)


        val good: kotlin.Array<String> = view.resources.getStringArray((R.array.Status))
        status.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                stat = good[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        val genderList: Array<String> = view.resources.getStringArray((R.array.Gender))
        gender.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                gen = genderList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }

        viewModel = ViewModelProvider(this)[ListViewModel::class.java]

        back.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.save_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mi_save -> {
                if (name?.text!!.isNotEmpty() && email?.text!!.isNotEmpty()) {
                    if (email?.text.toString().matches(emailPattern.toRegex())) {
                        emailTextField?.let { setErrorTextField(false, it) }
                        nameTextField?.let { setErrorTextField(false, it) }

                        progressBar = view?.findViewById(R.id.progressBar)
                        progressBar?.isVisible = true
                        viewModel.postData(
                            PostData(
                                102,
                                name?.text.toString(),
                                email?.text.toString(),
                                gen,
                                stat
                            )
                        )
                        viewModel.postData.observe(viewLifecycleOwner) {
                            if (it.isSuccessful) {
                                progressBar?.isVisible = false
                                Log.i(
                                    "TAG",
                                    "Post person clicked name: ${name?.text.toString()} & ${email?.text.toString()} ,$gen ,$stat"
                                )
                                navigationListener.popupFragment()
                                Toast.makeText(
                                    requireContext(),
                                    "Profile Added Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Profile is not Added",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                    } else {
                        setErrorTextField(true, emailTextField!!)
                        emailTextField?.error = "Invalid Email"
                    }
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
}