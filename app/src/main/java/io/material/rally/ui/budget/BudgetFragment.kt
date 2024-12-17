package io.material.rally.ui.budget

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import io.material.rally.data.DataProvider
import io.material.rally.extension.getRallyItemDecoration
import io.material.rally.extension.toUSDFormatted
import io.material.rally.ui.info.InfoFragment
import io.material.rally.ui.overview.adapter.BudgetAdapter
import io.material.rally_pie.RallyPieAnimation
import io.material.rally_pie.RallyPieData
import io.material.rally_pie.RallyPiePortion
import io.material.rally_pie.pxToDp
import io.material.rally.databinding.FragmentBudgetBinding // 自动生成的绑定类

class BudgetFragment : Fragment() {

  private var _binding: FragmentBudgetBinding? = null
  private val binding get() = _binding!!

  private lateinit var budgetAdapter: BudgetAdapter

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    _binding = FragmentBudgetBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)

    setUpPieView()
    setUpRecyclerView()

    binding.btnInfo.setOnClickListener {
      val infoFragment = InfoFragment()
      infoFragment.show(childFragmentManager, "AccountInfo")
    }
  }

  private fun setUpPieView() {
    binding.tvAmount.text = DataProvider.budgetOverView.budgets.sumByDouble { it.spend.toDouble() }
      .toFloat()
      .toUSDFormatted()

    val rallyPiePortions = DataProvider.budgetOverView.budgets.map {
      RallyPiePortion(it.name, it.spend, ContextCompat.getColor(requireContext(), it.color))
    }

    val rallyPieData =
      RallyPieData(portions = rallyPiePortions, maxValue = DataProvider.budgetOverView.total)

    val rallyPieAnimation = RallyPieAnimation(binding.rallyPie).apply {
      duration = 600
    }

    binding.rallyPie.setPieData(pieData = rallyPieData, animation = rallyPieAnimation)
  }

  private fun setUpRecyclerView() {
    val isTwoLine = requireContext().pxToDp(binding.rvBudget.measuredWidth) >= 400
    Log.i("Width", requireContext().pxToDp(requireView().measuredWidth).toString())

    budgetAdapter = BudgetAdapter(isTwoLine)

    binding.rvBudget.apply {
      layoutManager = LinearLayoutManager(requireContext())
      setHasFixedSize(true)
      addItemDecoration(getRallyItemDecoration())
      adapter = budgetAdapter
    }
    budgetAdapter.submitList(DataProvider.budgetOverView.budgets)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null // 防止内存泄漏
  }
}