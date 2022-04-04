package com.micosi.pogodynka.ui.weather_norm

import android.os.Bundle
import android.util.Log
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
import com.micosi.pogodynka.databinding.FragmentNormWeatherBinding
import com.micosi.pogodynka.models.AppView
import com.micosi.pogodynka.providers.SnackBarProvider
import com.squareup.picasso.Picasso

class NormWeatherFragment : Fragment() {

    private val args: NormWeatherFragmentArgs by navArgs()
    private lateinit var binding: FragmentNormWeatherBinding
    private lateinit var viewModel: NormWeatherViewModel
    private val snackBarProvider = SnackBarProvider()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_norm_weather,
            container,
            false
        )

        viewModel = NormWeatherViewModel(args.city)

        return binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@NormWeatherFragment.viewModel
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

        binding.changeView.setOnClickListener {
            val action =
                viewModel.city.value?.let { city ->
                    NormWeatherFragmentDirections.actionNormalViewFragmentToOldWeatherFragment(
                        city
                    )
                }
            if (action != null) {
                findNavController().navigate(action)
            }
        }

        binding.city.setOnClickListener {
            val action =
                NormWeatherFragmentDirections.actionNormalViewFragmentToChangeCityFragment(
                    AppView.NORMAL
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
        Log.d("Weather", "Destroy")
    }
}