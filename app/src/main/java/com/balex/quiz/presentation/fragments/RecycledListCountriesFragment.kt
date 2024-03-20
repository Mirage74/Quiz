package com.balex.quiz.presentation.fragments

import android.os.Bundle
import android.util.Log
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
import com.balex.quiz.presentation.SORT_MODE_BY_CAPITAL
import com.balex.quiz.presentation.SORT_MODE_BY_COUNTRY
import com.balex.quiz.presentation.SYMBOL_NOT_APPLY_FILTER

class RecycledListCountriesFragment : Fragment() {
    val TAG = "RecycledListCountriesFragment"
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
        countriesListAdapter.submitList(
            viewModel.getCountriesListSortedAndFiltered(
                SORT_MODE_BY_COUNTRY, SYMBOL_NOT_APPLY_FILTER
            )
        )
        setRadioListener()
        binding.btFilter.setOnClickListener {
            popupMenu.show()
        }
        binding.radioGroupSort.setOnCheckedChangeListener { _, checkedId ->
            //Log.d(TAG, "checkedId: ${checkedId == R.id.rbSortByCountry}")
            if (checkedId == R.id.rbSortByCountry) {

            } else {
                if (checkedId == R.id.rbSortByCapital) {

                }
            }


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

    private fun setRadioListener() {
        binding.radioGroupSort.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rbSortByCountry) {
                countriesListAdapter.submitList(
                    viewModel.getCountriesListSortedAndFiltered(
                        SORT_MODE_BY_COUNTRY, binding.tvSelectedLetter.text.first()
                    )
                ) {
                    binding.rvCountriesList.scrollToPosition(0)
                }
            } else if (checkedId == R.id.rbSortByCapital) {
                countriesListAdapter.submitList(
                    viewModel.getCountriesListSortedAndFiltered(
                        SORT_MODE_BY_CAPITAL, binding.tvSelectedLetter.text.first()
                    )
                ) {
                    binding.rvCountriesList.scrollToPosition(0)
                }

            }

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
            filterListByLetter(s)
            false
        }
    }

    private fun filterListByLetter(s: String) {
        if (binding.radioGroupSort.checkedRadioButtonId == R.id.rbSortByCountry) {
            countriesListAdapter.submitList(
                viewModel.getCountriesListSortedAndFiltered(
                    SORT_MODE_BY_COUNTRY, s.first()
                )
            ) {
                binding.rvCountriesList.scrollToPosition(0)
            }
        } else if (binding.radioGroupSort.checkedRadioButtonId == R.id.rbSortByCapital) {
            countriesListAdapter.submitList(
                viewModel.getCountriesListSortedAndFiltered(
                    SORT_MODE_BY_CAPITAL, s.first()
                )
            ) {
                binding.rvCountriesList.scrollToPosition(0)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}