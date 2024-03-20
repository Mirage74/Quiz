package com.balex.quiz.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.balex.quiz.data.api.ApiFactory
import com.balex.quiz.databinding.CountryItemLinearBinding
import com.bumptech.glide.Glide

class ViewCountryItemFragment : Fragment() {
    private val args by navArgs<ViewCountryItemFragmentArgs>()

    private var _binding: CountryItemLinearBinding? = null
    private val binding: com.balex.quiz.databinding.CountryItemLinearBinding
        get() = _binding ?: throw RuntimeException("ViewCountryItemFragment == null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CountryItemLinearBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            var s = "Country name: ${args.country.countryName}"
            tvCountryName.text = s
            s = "Capital name: ${args.country.capitalName}"
            tvCapitalName.text = s
            Glide.with(view)
                .load("${ApiFactory.BASE_URL_STATIC_IMAGES}/${args.country.imageName}")
                .into(ivImageCapital)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}