package com.aldikitta.retrofitexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import com.aldikitta.retrofitexample.databinding.ActivityMainBinding
import retrofit2.Response
import retrofit2.create

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var retService: AlbumService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        retService = RetrofitInstance.getRetrofitInstance().create(AlbumService::class.java)
//        getRequesWithQueryParameters()
//        getRequesWithPathParameters()
        uploadAlbum()

    }

    private fun getRequesWithQueryParameters() {
        val responseLiveData: LiveData<Response<Albums>> = liveData {
            val response = retService.getSortedAlbums(3)

            emit(response)
        }
        responseLiveData.observe(this, Observer {
            val albumsList = it.body()?.listIterator()
            if (albumsList != null) {
                while (albumsList.hasNext()) {
                    val albumsItem = albumsList.next()
                    val result = " " + "Album Tittle : ${albumsItem.title}" + "\n" +
                            " " + "Album id : ${albumsItem.id}" + "\n" +
                            " " + "User id : ${albumsItem.userId}" + "\n\n\n"
                    binding.textView.append(result)
                }
            }
        })
    }

    private fun getRequesWithPathParameters() {
        val pathResponse: LiveData<Response<AlbumsItem>> = liveData {
            val response = retService.getAlbum(3)
            emit(response)
        }

        pathResponse.observe(this, Observer {
            val title = it.body()?.title
            Toast.makeText(applicationContext, title, Toast.LENGTH_SHORT).show()
        })
    }

    private fun uploadAlbum() {
        val album = AlbumsItem(0, "My Title", 3)
        val postResponse: LiveData<Response<AlbumsItem>> = liveData {
            val response = retService.uploadAlbum(album)
            emit(response)
        }
        postResponse.observe(this, Observer {
            val receivedAlbumsItem = it.body()
            val result = " " + "Album Tittle : ${receivedAlbumsItem?.title}" + "\n" +
                    " " + "Album id : ${receivedAlbumsItem?.id}" + "\n" +
                    " " + "User id : ${receivedAlbumsItem?.userId}" + "\n\n\n"
            binding.textView.text = result
        })
    }
}

