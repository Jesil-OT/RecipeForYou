package com.jesil.example.custom.recipeforyou.ui.favorites

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jesil.example.custom.recipeforyou.R

class FavoritesListFragment : Fragment() {

    companion object {
        fun newInstance() = FavoritesListFragment()
    }

    private lateinit var viewModel: FavoritesListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.favorites_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FavoritesListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}