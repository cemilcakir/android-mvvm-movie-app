package com.example.anroidmovieappmvvm.ui.details

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import com.example.anroidmovieappmvvm.R
import com.example.anroidmovieappmvvm.data.repository.MovieRepository
import com.example.anroidmovieappmvvm.databinding.DetailFragmentBinding
import com.example.anroidmovieappmvvm.internal.NetworkStatus
import com.google.android.material.snackbar.Snackbar
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class DetailFragment : Fragment(), KodeinAware {


    override val kodein by closestKodein()
    private val repository: MovieRepository by instance()

    private lateinit var viewModel: DetailViewModel
    private lateinit var binding: DetailFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.let {
            val safeArgs = DetailFragmentArgs.fromBundle(it)
            val movieId = safeArgs.movieId

            val viewModelFactory = DetailViewModel.Factory(movieId, repository)
            viewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailViewModel::class.java)

            viewModel.details.observe(viewLifecycleOwner, Observer { detailModel ->
                detailModel?.let { details ->
                    binding.details = details
                    binding.lifecycleOwner = this
                }
            })

            viewModel.networkStatus.observe(viewLifecycleOwner, Observer {
                if (it == NetworkStatus.NO_CONNECTIVITY)
                    Snackbar.make(binding.root, resources.getString(R.string.no_connectivity), Snackbar.LENGTH_SHORT).show()
            })
        }
    }

}
