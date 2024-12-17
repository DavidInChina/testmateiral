package io.material.rally.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import io.material.rally.data.DataProvider
import io.material.rally.databinding.FragmentMonthlyBinding
import io.material.rally.extension.getRallyItemDecoration

class MonthlyFragment : Fragment() {

  private var _binding: FragmentMonthlyBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentMonthlyBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setUpRecyclerView()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null // 防止内存泄漏
  }

  private fun setUpRecyclerView() {
    binding.rvMonthly.apply {
      layoutManager = LinearLayoutManager(requireContext())
      setHasFixedSize(true)
      addItemDecoration(getRallyItemDecoration())
      adapter = MonthlyAdapter(DataProvider.monthlyItems(arguments?.getInt(KEY_MONTH) ?: 1))
    }
  }

  companion object {
    private const val KEY_MONTH = "key-month"
    fun newInstance(month: Int): MonthlyFragment {
      return MonthlyFragment().apply {
        arguments = Bundle().apply { putInt(KEY_MONTH, month) }
      }
    }
  }
}