package com.jxlopez.movieapp.util.components

import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

open class BaseFragment : Fragment() {

    fun showError(message: String) {
        MovieStyleAlertDialog(
            MovieStyleAlertDialog.Builder()
                .context(requireContext())
                .activity(requireActivity())
                .message(message)
        ).showAlert()
    }

}