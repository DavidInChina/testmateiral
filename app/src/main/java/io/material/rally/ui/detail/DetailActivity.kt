package io.material.rally.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import io.material.rally.R
import io.material.rally.databinding.ActivityDetailBinding // 自动生成的绑定类
import io.material.rally_line_chart.DataPoint

class DetailActivity : AppCompatActivity() {

  private lateinit var binding: ActivityDetailBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityDetailBinding.inflate(layoutInflater)
    setContentView(binding.root)

    binding.rallyLine.setCurveBorderColor(intent.getIntExtra(KEY_COLOR, R.color.rally_dark_green))

    setUpToolbar()
    setUpTab()
    binding.rallyLine.addDataPoints(getRandomPoints())
  }

  private fun setUpToolbar() {
    setSupportActionBar(binding.toolbar)
    supportActionBar?.apply {
      setDisplayHomeAsUpEnabled(true)
      title = ""
    }
  }

  private fun setUpTab() {
    val months = listOf(
      "Jan 2018", "Feb 2018", "Mar 2018", "Apr 2018", "May 2018", "Jun 2018", "July 2018",
      "Aug 2018", "Sep 2018", "Oct 2018", "Nov 2018", "Dec 2018"
    )
    binding.tab.addTabs(months)
    binding.tab.addOnPageChangeListener {
      binding.rallyLine.addDataPoints(getRandomPoints())
    }

    binding.viewPager.adapter = MonthlyPagerAdapter(supportFragmentManager)
    binding.tab.setUpWithViewPager(binding.viewPager)
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_detail, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == android.R.id.home) {
      super.onBackPressed()
      return true
    }
    return super.onOptionsItemSelected(item)
  }

  companion object {
    private const val KEY_COLOR = "key-color"
    fun start(
      context: Context,
      shareView: View, @ColorRes color: Int
    ) {
      val intent = Intent(context, DetailActivity::class.java)
      intent.putExtra(KEY_COLOR, color)

      val pair = Pair(shareView.findViewById<View>(R.id.shareView), "DetailView")
      val options =
        ActivityOptionsCompat.makeSceneTransitionAnimation(context as AppCompatActivity, pair)

      val transition = context.window.exitTransition
      transition.excludeTarget(shareView, true)
      context.window.exitTransition = transition

      context.startActivity(intent, options.toBundle())
    }
  }
}

fun getRandomPoints(): MutableList<DataPoint> {
  val list = mutableListOf<DataPoint>()
  val range = (0..10)

  (1..15).forEach { _ ->
    list.add(DataPoint(range.random() * 100f))
  }
  return list
}