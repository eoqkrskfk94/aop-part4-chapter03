package com.mj.aop_part4_chapter03

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.mj.aop_part4_chapter03.databinding.ActivityMainBinding
import com.mj.aop_part4_chapter03.model.LocationLatLngEntity
import com.mj.aop_part4_chapter03.model.SearchResultEntity
import com.mj.aop_part4_chapter03.utility.RetrofitUtil
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: SearchRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        job = Job()

        initAdapter()
        initView()
        bindViews()
        initData()
        setData()
    }

    private fun initAdapter() {
        adapter = SearchRecyclerAdapter()
    }

    private fun bindViews() = with(binding) {
        searchButton.setOnClickListener {
            searchKeyword(searchBarInputView.text.toString())
        }
    }

    private fun initView() = with(binding) {
        emptyResultTextView.isVisible = false
        recyclerView.adapter = adapter
    }

    private fun initData() {
        adapter.notifyDataSetChanged()
    }

    private fun setData() {
        val dataList = (0..10).map {
            SearchResultEntity(
                name = "$it",
                fullAddress = "address $it",
                locationLatLng = LocationLatLngEntity(
                    it.toFloat(),
                    it.toFloat()
                )
            )
        }
        adapter.setSearchResultList(dataList) {
            Toast.makeText(this, "test", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun searchKeyword(keywordString : String) {
        launch(coroutineContext) {
            try {
                withContext(Dispatchers.IO) {
                    val response = RetrofitUtil.apiService.getSearchLocation(keyword = keywordString)

                    if(response.isSuccessful) {
                        val body = response.body()
                        withContext(Dispatchers.Main) {

                        }
                    }
                }
            }catch (e : Exception) {
                e.printStackTrace()
            }
        }
    }
}