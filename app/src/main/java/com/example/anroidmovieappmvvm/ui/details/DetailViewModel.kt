package com.example.anroidmovieappmvvm.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.anroidmovieappmvvm.data.models.DetailModel
import com.example.anroidmovieappmvvm.data.repository.MovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DetailViewModel(val movieId: Int) : ViewModel() {

    val viewModelJob = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val repository = MovieRepository()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private var _details = MutableLiveData<DetailModel>()
    val details: LiveData<DetailModel>
        get() = _details

    init {
        uiScope.launch {
            _details.value = repository.getMovieDetails(movieId)
        }
    }

    class Factory(val movieId: Int) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DetailViewModel(movieId) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
