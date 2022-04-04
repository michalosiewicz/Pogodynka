package com.micosi.pogodynka.ui.weather_old

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.micosi.pogodynka.R
import com.micosi.pogodynka.constants.Constants.ICON_URL
import com.micosi.pogodynka.databinding.FragmentOldWeatherBinding
import com.micosi.pogodynka.models.AppView
import com.micosi.pogodynka.providers.SnackBarProvider
import com.squareup.picasso.Picasso


class OldWeatherFragment : Fragment() {

    private val args: OldWeatherFragmentArgs by navArgs()
    private lateinit var binding: FragmentOldWeatherBinding
    private lateinit var viewModel: OldWeatherViewModel
    private val snackBarProvider = SnackBarProvider()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_old_weather,
            container,
            false
        )

        viewModel = OldWeatherViewModel(args.city)

        return binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@OldWeatherFragment.viewModel
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.changeError.observe(viewLifecycleOwner) { message ->
            snackBarProvider.errorSnackBar(message, this.requireActivity())
        }

        viewModel.icon.observe(viewLifecycleOwner) { icon ->
            val imageUrl = "$ICON_URL$icon"
            Picasso.get().load(imageUrl).into(binding.imageView)
        }

        binding.mainView.setOnClickListener {
            val action =
                viewModel.city.value?.let { city ->
                    OldWeatherFragmentDirections.actionOldWeatherFragmentToNormalViewFragment(
                        city
                    )
                }
            if (action != null) {
                findNavController().navigate(action)
            }
        }

        binding.changeCity.setOnClickListener {
            val action =
                OldWeatherFragmentDirections.actionOldWeatherFragmentToChangeCityFragment(
                    AppView.OLD
                )
            findNavController().navigate(action)
        }

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.isFragmentOpen.value = false
    }
}