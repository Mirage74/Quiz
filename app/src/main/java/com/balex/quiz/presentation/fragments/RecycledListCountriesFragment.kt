package com.balex.quiz.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.balex.quiz.R
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
        initView()
        val popupMenu = PopupMenu(requireActivity(), binding.btFilter)
        popupMenu.menuInflater.inflate(R.menu.menu_popup, popupMenu.menu)
        popupMenuSetListener(popupMenu)
        countriesListAdapter.submitList(viewModel.countriesFullList)
        binding.btFilter.setOnClickListener{
            popupMenu.show()
        }

    }


    private fun initView() {
        with(binding) {
            rbSortByCountry.isChecked = true
        }
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val rvCountriesList = binding.rvCountriesList
        with(rvCountriesList) {
            countriesListAdapter = CountriesAdapter()
            adapter = countriesListAdapter

        }
        countriesListAdapter.onCountryItemClickListener = {
            findNavController().navigate(
                RecycledListCountriesFragmentDirections.actionRecycledListCountriesFragmentToViewCountryItemFragment(
                    it
                )
            )
        }

    }

    private fun popupMenuSetListener(popupMenu: PopupMenu) {
        popupMenu.setOnMenuItemClickListener {
            val id = it.itemId
            val s = when (id) {
                R.id.letterA -> "A"
                R.id.letterB -> "B"
                R.id.letterC -> "C"
                R.id.letterD -> "D"
                R.id.letterE -> "E"
                R.id.letterF -> "F"
                R.id.letterG -> "G"
                R.id.letterH -> "H"
                R.id.letterI -> "I"
                R.id.letterJ -> "J"
                R.id.letterK -> "K"
                R.id.letterL -> "L"
                R.id.letterM -> "M"
                R.id.letterN -> "N"
                R.id.letterO -> "O"
                R.id.letterP -> "P"
                R.id.letterQ -> "Q"
                R.id.letterR -> "R"
                R.id.letterS -> "S"
                R.id.letterT -> "T"
                R.id.letterU -> "U"
                R.id.letterV -> "V"
                R.id.letterW -> "W"
                R.id.letterX -> "X"
                R.id.letterY -> "Y"
                R.id.letterZ -> "Z"
                else -> "*"
            }
            binding.tvSelectedLetter.text = s
            false
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}