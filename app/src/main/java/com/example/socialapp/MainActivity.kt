package com.example.socialapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.socialapp.apiservices.UserDetails
import com.example.socialapp.fragments.AddProfileFragment
import com.example.socialapp.fragments.DetailFragment
import com.example.socialapp.fragments.EditProfileFragment
import com.example.socialapp.fragments.UsersFragment

class MainActivity : AppCompatActivity(), NavigationListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, UsersFragment())
            .commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Log.i("TAG", "onBackPressed: from mainActivity ")
        //supportFragmentManager.popBackStack()
    }

    override fun navigateToUserFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, UsersFragment())
            .commit()
    }

    override fun navigateToDetailFragment(details: UserDetails) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, DetailFragment.newInstance(details))
            .addToBackStack("Back")
            .commit()
    }

    override fun navigateToDetailFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, DetailFragment())
            .addToBackStack(null)
            .commit()
    }

    override fun navigateToAddProfileFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container,AddProfileFragment())
            .addToBackStack("return")
            .commit()
    }

    override fun popupFragment() {
        supportFragmentManager.popBackStack()
    }

    override fun navigateToEditProfileFragment(details: UserDetails?) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container,EditProfileFragment.newInstance(details))
            .addToBackStack("back")
            .commit()
    }
}

interface NavigationListener {
    fun navigateToUserFragment()
    fun navigateToDetailFragment(details: UserDetails)
    fun navigateToAddProfileFragment()
    fun navigateToDetailFragment()
    fun popupFragment()
    fun navigateToEditProfileFragment(details: UserDetails?)
}