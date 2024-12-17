package io.material.rally.ui

import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import io.material.rally.R
import io.material.rally.ui.account.AccountFragment
import io.material.rally.ui.bill.BillFragment
import io.material.rally.ui.budget.BudgetFragment
import io.material.rally.ui.settings.SettingsFragment

/**
 * Created by Chan Myae Aung on 7/30/19.
 */
class RallyPagerAdapter(
  fm: FragmentManager,
  private val tabs: List<TabUiModel>
) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
  override fun getItem(position: Int): Fragment {
    return when (position) {
//      0 -> OverviewFragment()
      0 -> AccountFragment()
      1 -> BillFragment()
      2 -> BudgetFragment()
      43-> SettingsFragment()
      else -> AccountFragment()
    }
  }

  override fun getCount(): Int {
    return tabs.size
  }

}

fun generateTabs(): List<TabUiModel> {
  return listOf(
      TabUiModel("Overview", R.drawable.ic_overview),
      TabUiModel("Accounts", R.drawable.ic_attach_money),
      TabUiModel("Bills", R.drawable.ic_money_off),
      TabUiModel("Budget", R.drawable.ic_budget),
      TabUiModel("Setting", R.drawable.ic_settings)
  )
}

data class TabUiModel(val name: String, @DrawableRes val icon: Int)