package com.example.anroidmovieappmvvm.ui.nowPlaying

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.anroidmovieappmvvm.R
import com.example.anroidmovieappmvvm.data.models.MovieModel
import com.example.anroidmovieappmvvm.data.repository.MovieRepository
import com.example.anroidmovieappmvvm.databinding.NowPlayingFragmentBinding
import com.example.anroidmovieappmvvm.internal.NetworkStatus
import com.example.anroidmovieappmvvm.ui.MovieAdapter
import com.example.anroidmovieappmvvm.ui.MovieListener
import com.example.anroidmovieappmvvm.ui.MovieViewModel
import com.example.anroidmovieappmvvm.ui.ViewModelType
import com.google.android.material.snackbar.Snackbar
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class NowPlayingFragment : Fragment(), KodeinAware {

    override val kodein by closestKodein()
    private val repository: MovieRepository by instance()

    private lateinit var viewModel: MovieViewModel
    private lateinit var binding: NowPlayingFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = NowPlayingFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val viewModelFactory = MovieViewModel.Factory(ViewModelType.NOW_PLAYING, repository)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MovieViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val adapter = MovieAdapter(MovieListener { movieId ->
            viewModel.navigateToDetails(movieId)
        })
        binding.nowPlayingMovieList.adapter = adapter

        viewModel.movies.observe(viewLifecycleOwner, Observer {
            it?.let { movieList ->
                val list = arrayListOf<MovieModel>()
                movieList.forEach {
                    list.add(it)
                }

                adapter.submitList(list)
            }
        })

        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                view?.findNavController()
                    ?.navigate(NowPlayingFragmentDirections.actionNowPlayingFragmentToDetailFragment(it))

                viewModel.doneNavigatingToDetail()
            }
        })

        viewModel.networkStatus.observe(viewLifecycleOwner, Observer {
            if (it == NetworkStatus.NO_CONNECTIVITY)
                Snackbar.make(binding.root, resources.getString(R.string.no_connectivity), Snackbar.LENGTH_SHORT).show()
        })

    }

}
