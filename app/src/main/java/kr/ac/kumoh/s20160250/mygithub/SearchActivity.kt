package kr.ac.kumoh.s20160250.mygithub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.*
import kr.ac.kumoh.s20160250.mygithub.RepositoryActivity.Companion.REPOSITORY_NAME_KEY
import kr.ac.kumoh.s20160250.mygithub.RepositoryActivity.Companion.REPOSITORY_OWNER_KEY
import kr.ac.kumoh.s20160250.mygithub.data.entity.GithubRepoEntity
import kr.ac.kumoh.s20160250.mygithub.databinding.ActivitySearchBinding
import kr.ac.kumoh.s20160250.mygithub.utillity.RetrofitUtil
import kr.ac.kumoh.s20160250.mygithub.view.RepositoryRecyclerAdapter
import kotlin.coroutines.CoroutineContext

class SearchActivity : AppCompatActivity(), CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var binding: ActivitySearchBinding
    private lateinit var adapter: RepositoryRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAdapter()
        initViews()
        bindViews()
    }

    private fun initAdapter() {
        adapter = RepositoryRecyclerAdapter()
    }

    private fun initViews() = with(binding) {
        emptyResultTextView.isGone = true
        recyclerView.adapter = adapter
        recyclerView.layoutManager =LinearLayoutManager(this@SearchActivity)
    }

    private fun bindViews() = with(binding) {
        searchButton.setOnClickListener {
            //Log.d("검색",searchBarInputView.text.toString())
            searchKeyword(searchBarInputView.text.toString())
        }
    }

    private fun searchKeyword(keywordString: String) {
        showLoading(true)
        launch(coroutineContext) {
            try {
                withContext(Dispatchers.IO) {
                    val response = RetrofitUtil.githubApiService.searchRepositories(
                        query = keywordString
                    )
                    if (response.isSuccessful) {
                        val body = response.body()
                        withContext(Dispatchers.Main) {
                            body?.let { searchResponse ->
                                Log.e("검색결과",response.body().toString())
                                setData(searchResponse.Items)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@SearchActivity, "검색하는 과정에서 에러가 발생했습니다. : ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setData(githubRepoList: List<GithubRepoEntity>) = with(binding) {
        showLoading(false)
        if (githubRepoList.isEmpty()) {
            emptyResultTextView.isGone = false
            recyclerView.isGone = true
        } else {
            emptyResultTextView.isGone = true
            recyclerView.isGone = false
            adapter.setSearchResultList(githubRepoList) {
                startActivity(
                    Intent(this@SearchActivity, RepositoryActivity::class.java).apply {
                        putExtra(REPOSITORY_OWNER_KEY, it.owner.login)
                        putExtra(REPOSITORY_NAME_KEY, it.name)
                    }
                )
            }
        }
    }
    private fun showLoading(isShown: Boolean) = with(binding) {
        progressBar.isGone = isShown.not()
    }
}