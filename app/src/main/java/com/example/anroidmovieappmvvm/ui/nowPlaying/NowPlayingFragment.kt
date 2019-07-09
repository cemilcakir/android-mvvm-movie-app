package com.example.anroidmovieappmvvm.ui.nowPlaying

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.anroidmovieappmvvm.data.models.MovieModel
import com.example.anroidmovieappmvvm.databinding.NowPlayingFragmentBinding
import com.example.anroidmovieappmvvm.ui.MovieAdapter
import com.example.anroidmovieappmvvm.ui.MovieListener
import com.example.anroidmovieappmvvm.ui.MovieViewModel
import com.example.anroidmovieappmvvm.ui.ViewModelType

class NowPlayingFragment : Fragment() {

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

        val viewModelFactory = MovieViewModel.Factory(ViewModelType.NOW_PLAYING)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MovieViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val adapter = MovieAdapter(MovieListener { movieId ->
            viewModel.navigateToDetails(movieId)
        })
        binding.nowPlayingMovieList.adapter = adapter

        viewModel.movies.observe(viewLifecycleOwner, Observer {
            it?.let {movieList ->
                val list = arrayListOf<MovieModel>()
                movieList.forEach {
                    list.add(it)
                }

                adapter.submitList(list)
            }
        })

        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                view?.findNavController()?.navigate(NowPlayingFragmentDirections.actionNowPlayingFragmentToDetailFragment(it))

                viewModel.doneNavigatingToDetail()
            }
        })

    }

}
