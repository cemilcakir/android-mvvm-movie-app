package com.example.anroidmovieappmvvm.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.anroidmovieappmvvm.data.models.MovieModel
import com.example.anroidmovieappmvvm.databinding.MovieListItemBinding

class MovieAdapter(val clickListener: MovieListener) : ListAdapter<MovieModel, MovieAdapter.ViewHolder>(MovieDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    class MovieDiffCallback : DiffUtil.ItemCallback<MovieModel>() {
        override fun areItemsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
            return oldItem == newItem
        }

    }
    class ViewHolder private constructor(val binding: MovieListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MovieModel, clickListener: MovieListener) {
            binding.movie = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup) : ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val item = MovieListItemBinding.inflate(layoutInflater, parent , false)

                return ViewHolder(item)
            }
        }
    }
}

class MovieListener(val clickListener: (movieId: Int) -> Unit) {
    fun onClick(movie: MovieModel) = clickListener(movie.id)
}