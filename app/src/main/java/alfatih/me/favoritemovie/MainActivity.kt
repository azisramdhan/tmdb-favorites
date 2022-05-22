package alfatih.me.favoritemovie

import alfatih.me.favoritemovie.MovieContract.MovieEntry.Companion.CONTENT_URI
import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.database.getDoubleOrNull
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: ListMovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupView()
        fetchLocalDataMovie()
    }

    private fun setupView() {
        adapter = ListMovieAdapter()
        rv_movies.adapter = adapter
        rv_movies.layoutManager = LinearLayoutManager(this)
    }


    private fun fetchLocalDataMovie() {
        val movieList = mutableListOf<MovieItem>()
        val sortOrder = MovieContract.MovieEntry.COLUMN_NAME_TITLE + " ASC"
        Log.d("CONTENT_URI", CONTENT_URI.toString())

        val cursor = contentResolver?.query(CONTENT_URI, null, null, null, sortOrder) as Cursor
//        Toast.makeText(this, CONTENT_URI.toString(), Toast.LENGTH_LONG).show()
        if (cursor.count>0) {
            cursor.moveToFirst()
            do {
                val adult = cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_ADULT)) > 0
                val backdropPath = cursor.getStringOrNull(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_BACKDROP_PATH))
                val genreIds = cursor.getStringOrNull(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_GENRE_IDS))
                val id = cursor.getIntOrNull(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_ID))
                val originalLanguage = cursor.getStringOrNull(cursor.getColumnIndex(
                    MovieContract.MovieEntry.COLUMN_NAME_ORIGINAL_LANGUAGE))
                val originalTitle = cursor.getStringOrNull(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_ORIGINAL_ORIGINAL_TITLE))
                val overview = cursor.getStringOrNull(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_OVERVIEW))
                val popularity = cursor.getDoubleOrNull(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_POPULARITY))
                val posterPath = cursor.getStringOrNull(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_POSTER_PATH))
                val releaseDate = cursor.getStringOrNull(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_RELEASE_DATE))
                val title = cursor.getStringOrNull(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_TITLE))
                val video = cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_VIDEO)) > 0
                val voteAverage = cursor.getDoubleOrNull(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_VOTE_AVERAGE))
                val voteCount = cursor.getIntOrNull(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_VOTE_COUNT))

                val result = genreIds?.map { it.toInt() }
                val item = MovieItem(adult, backdropPath, result, id, originalLanguage, originalTitle,overview, popularity, posterPath,releaseDate, title,video, voteAverage, voteCount)
                movieList.add(item)
                cursor.moveToNext()
            } while (!cursor.isAfterLast)
            adapter.onReplace(movieList)
        }else{
            Toast.makeText(this, "Tidak ditemukan film favorit!",Toast.LENGTH_LONG).show()
        }
    }
}
