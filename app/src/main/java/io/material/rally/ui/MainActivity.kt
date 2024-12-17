package io.material.rally.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import io.material.rally.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
  private lateinit var binding: ActivityMainBinding


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    if (savedInstanceState == null) runEnterAnimation()
    setUpViewPager()
  }

  private fun setUpViewPager() {
    val tabs = generateTabs()
    binding.viewPager.adapter = RallyPagerAdapter(supportFragmentManager, tabs)
    binding.viewPager.offscreenPageLimit = 0
    binding.tabLayout.setUpWithViewPager(binding.viewPager, false)

    binding.viewPager.setCurrentItem(0, true)
  }

  fun navigateToTabs(item: TabItem) {
    binding.viewPager.setCurrentItem(item.position, true)
  }

  private fun runEnterAnimation() {
    binding.tabLayout.post {
      binding.tabLayout.translationY -= binding.tabLayout.height.toFloat()
      binding.tabLayout.alpha = 0f
      binding.tabLayout.animate()
          .translationY(0f)
          .setInterpolator(AccelerateDecelerateInterpolator())
          .alpha(1f)
          .setDuration(300)
          .start()
    }
  }

  override fun onBackPressed() {
    if (binding.viewPager.currentItem != 0) {
      binding.viewPager.currentItem = 0
      return
    }
    super.onBackPressed()
  }

  companion object {
    fun start(context: Context) {
      val intent = Intent(context, MainActivity::class.java)
      //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
      context.startActivity(intent)
    }
  }
}

enum class TabItem(val position: Int) {
  ACCOUNT(1),
  BILL(2),
  BUDGET(3)
}
