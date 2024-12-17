package io.material.rally.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.material.rally.databinding.FragmentSettingsBinding

/**
 * Created by lin min phyo on 2019-08-01.
 */
class SettingsFragment : Fragment() {

  private var _binding: FragmentSettingsBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    _binding = FragmentSettingsBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)

    Linkify.addLinks(binding.tvAboutRallyProject, Linkify.WEB_URLS)
    Linkify.addLinks(binding.tvAboutOpenSource, Linkify.WEB_URLS)

    binding.btnCma.setOnClickListener {
      startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/Thechanmyaeaung")))
    }
    binding.btnLmp.setOnClickListener {
      startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/Linminphyoe1")))
    }

  }
}