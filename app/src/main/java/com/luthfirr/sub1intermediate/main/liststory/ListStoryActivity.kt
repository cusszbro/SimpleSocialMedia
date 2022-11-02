package com.luthfirr.sub1intermediate.main.liststory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.luthfirr.sub1intermediate.R
import com.luthfirr.sub1intermediate.UserPreference
import com.luthfirr.sub1intermediate.ViewModelFactory
import com.luthfirr.sub1intermediate.adapter.LoadingStateAdapter
import com.luthfirr.sub1intermediate.adapter.StoryListAdapter
import com.luthfirr.sub1intermediate.addstory.UploadActivity
import com.luthfirr.sub1intermediate.api.response.ListStoryItem
import com.luthfirr.sub1intermediate.databinding.ActivityListStoryBinding
import com.luthfirr.sub1intermediate.detailstory.DetailStoryActivity
import com.luthfirr.sub1intermediate.login.LoginActivity
import com.luthfirr.sub1intermediate.login.dataStore
import com.luthfirr.sub1intermediate.main.mapstory.MapsActivity

class ListStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListStoryBinding
    private lateinit var listStoryViewModel: ListStoryViewModel
    private lateinit var adapterPaging : StoryListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.story_page)

        setupViewModel()
        setupAction()
    }

    private fun setupViewModel() {
        listStoryViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore), this)
        )[ListStoryViewModel::class.java]
    }

    private fun setupAction() {
        adapterPaging = StoryListAdapter()

        listStoryViewModel.getToken().observe(this) { user ->
            val session = user.state
            if (session) {
                showLoading(false)
                val name = user.name
                val token = user.token
                binding.tvName.text = resources.getString(R.string.greeting, name)
                listStoryViewModel.getStories(token).observe(this) {
                    adapterPaging.submitData(lifecycle, it)
                }
            } else {
                showLoading(false)
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        adapterPaging.setOnItemClick(object : StoryListAdapter.OnItemClick {
            override fun onItemClicked(data: ListStoryItem) {
                showLoading(true)
                Intent(this@ListStoryActivity, DetailStoryActivity::class.java).also {
                    it.putExtra(DetailStoryActivity.EXTRA_USERNAME, data.name)
                    it.putExtra(DetailStoryActivity.EXTRA_PHOTO, data.photoUrl)
                    it.putExtra(DetailStoryActivity.EXTRA_DESCRIPTION, data.description)
                    showLoading(false)
                    startActivity(it)
                }
            }
        })

        binding.apply {
            rvStories.layoutManager = LinearLayoutManager(this@ListStoryActivity)
            rvStories.setHasFixedSize(true)
            rvStories.adapter = adapterPaging.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    adapterPaging.retry()
                }
            )

            fabAddstory.setOnClickListener {
                val intentAdd = Intent(this@ListStoryActivity, UploadActivity::class.java)
                startActivity(intentAdd)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu1 -> {
                val intentMaps = Intent(this@ListStoryActivity, MapsActivity::class.java)
                startActivity(intentMaps)
                true
            }
            R.id.menu2 -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            R.id.menu3 -> {
                showLoading(true)
                showLoading(false)
                val intent = Intent(this@ListStoryActivity, LoginActivity::class.java)
                listStoryViewModel.clear()
                startActivity(intent)
                true
            }
            else -> true
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}