package com.sukdeb.weatherApp.ui.dashBoard.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.sukdeb.weatherApp.databinding.FragmentHomeBinding
import com.sukdeb.weatherApp.ui.dashBoard.MainViewModel
import com.sukdeb.weatherApp.utils.ext.toSimpleJson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel by activityViewModels<MainViewModel>()
    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()
    }

    private fun initObserver() {
        homeViewModel.weatherState.observe(viewLifecycleOwner) { state ->
            when(state) {
                HomeState.Loading -> {}
                is HomeState.Error -> {}
                is HomeState.WeatherSuccess -> {
                    Log.d("TAG", "initObserver: ${state.weatherData.toSimpleJson()}")
                }
            }
        }

        lifecycleScope.launch {
            homeViewModel.onWeatherCall(
                lat = mainViewModel.latitude.value.toString(),
                lon = mainViewModel.longitude.value.toString(),
                appId = "2c50325f427689340a03ff16215d8fc4",
                unit = "metric"
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}