package com.balex.quiz.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.balex.quiz.databinding.RecycledListCountriesBinding
import com.balex.quiz.presentation.CountriesAdapter
import com.balex.quiz.presentation.MainViewModel

class RecycledListCountriesFragment : Fragment() {
    private var _binding: RecycledListCountriesBinding? = null
    private val binding: RecycledListCountriesBinding
        get() = _binding ?: throw RuntimeException("RecycledListCountriesFragment == null")

    private lateinit var viewModel: MainViewModel
    private lateinit var countriesListAdapter: CountriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RecycledListCountriesBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        countriesListAdapter.submitList(viewModel.countriesFullList)
    }




    private fun setupRecyclerView() {
        val rvCountriesList = binding.rvCountriesList
        with(rvCountriesList) {
            countriesListAdapter = CountriesAdapter()
            adapter = countriesListAdapter

        }
        countriesListAdapter.onCountryItemClickListener = {
            findNavController().navigate(
                RecycledListCountriesFragmentDirections.actionRecycledListCountriesFragmentToViewCountryItemFragment(it)
            )
        }

    }
}