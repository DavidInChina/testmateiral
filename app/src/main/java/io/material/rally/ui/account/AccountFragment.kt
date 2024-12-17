package io.material.rally.ui.account

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
import io.material.rally.ui.overview.adapter.AccountOverviewAdapter
import io.material.rally_pie.RallyPieAnimation
import io.material.rally_pie.RallyPieData
import io.material.rally_pie.RallyPiePortion
import io.material.rally.databinding.FragmentAccountBinding // 自动生成的绑定类

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    private val accountAdapter by lazy { AccountOverviewAdapter(isSingleLine = false) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
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
        binding.tvAmount.text = DataProvider.accountOverView.total.toUSDFormatted()

        val rallyPiePortions = DataProvider.accountOverView.accounts.map {
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
        binding.rvAccount.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(getRallyItemDecoration())
            adapter = accountAdapter
        }
        accountAdapter.submitList(DataProvider.accountOverView.accounts)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // 防止内存泄漏
    }
}