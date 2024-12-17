package io.material.rally.ui.bill

import android.os.Bundle
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
import io.material.rally.ui.overview.adapter.BillAdapter
import io.material.rally_pie.RallyPieAnimation
import io.material.rally_pie.RallyPieData
import io.material.rally_pie.RallyPiePortion
import io.material.rally.databinding.FragmentBillBinding // 自动生成的绑定类

class BillFragment : Fragment() {

  private var _binding: FragmentBillBinding? = null
  private val binding get() = _binding!!

  private val billAdapter by lazy { BillAdapter() }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    _binding = FragmentBillBinding.inflate(inflater, container, false)
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
      infoFragment.show(childFragmentManager, "BillInfo")
    }
  }

  private fun setUpPieView() {
    binding.tvAmount.text = DataProvider.billOverView.total.toUSDFormatted()
    val rallyPiePortions = DataProvider.billOverView.bills.map {
      RallyPiePortion(
        it.name, it.amount, ContextCompat.getColor(requireContext(), it.color)
      )
    }
    val rallyPieData = RallyPieData(portions = rallyPiePortions)
    val rallyPieAnimation = RallyPieAnimation(binding.rallyPie).apply {
      duration = 600
    }

    binding.rallyPie.setPieData(pieData = rallyPieData, animation = rallyPieAnimation)
  }

  private fun setUpRecyclerView() {
    binding.rvBill.apply {
      layoutManager = LinearLayoutManager(requireContext())
      setHasFixedSize(true)
      addItemDecoration(getRallyItemDecoration())
      adapter = billAdapter
    }
    billAdapter.submitList(DataProvider.billOverView.bills)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null // 防止内存泄漏
  }
}