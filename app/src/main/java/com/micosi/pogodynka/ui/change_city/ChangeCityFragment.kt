package com.micosi.pogodynka.ui.change_city

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.micosi.pogodynka.R
import com.micosi.pogodynka.databinding.FragmentChangeCityBinding
import com.micosi.pogodynka.models.AppView
import com.micosi.pogodynka.providers.SnackBarProvider

class ChangeCityFragment : Fragment() {

    private val args: ChangeCityFragmentArgs by navArgs()
    private lateinit var appView: AppView
    private lateinit var binding: FragmentChangeCityBinding
    private val viewModel = ChangeCityViewModel()
    private val snackBarProvider = SnackBarProvider()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_change_city,
            container,
            false
        )

        appView = args.appView

        return binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@ChangeCityFragment.viewModel
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.changeSuccess.observe(viewLifecycleOwner) { city ->
            if (appView == AppView.NORMAL) {
                val action =
                    ChangeCityFragmentDirections.actionChangeCityFragmentToNormalViewFragment(
                        city
                    )
                findNavController().navigate(action)
            } else {
                val action =
                    ChangeCityFragmentDirections.actionChangeCityFragmentToOldWeatherFragment(
                        city
                    )
                findNavController().navigate(action)
            }
        }

        viewModel.changeError.observe(viewLifecycleOwner) { message ->
            hideKeyboard()
            snackBarProvider.errorSnackBar(message, this.requireActivity())
        }
    }

    private fun hideKeyboard() {
        val imm: InputMethodManager =
            this.requireActivity()
                .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = this.requireActivity().currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}