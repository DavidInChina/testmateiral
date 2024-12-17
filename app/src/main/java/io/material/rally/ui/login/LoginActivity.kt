package io.material.rally.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import io.material.rally.data.Authenticator
import io.material.rally.databinding.ActivityLoginBinding
import io.material.rally.ui.MainActivity

class LoginActivity : AppCompatActivity() {

  private lateinit var binding: ActivityLoginBinding
  private lateinit var authenticator: Authenticator

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityLoginBinding.inflate(layoutInflater)
    setContentView(binding.root)

    authenticator = Authenticator(applicationContext)
    if (authenticator.isLoggedIn()) {
      navigateToMain()
    }

    if (savedInstanceState == null) runEnterAnimation()

    setUpInputField()

    // Set click listeners
    with(binding) {
      constraintLayout.setOnClickListener { login() }
      ivFingerprint.setOnClickListener { login() }
      labelLoginId.setOnClickListener { login() }
      imgLogo.setOnClickListener { login() }
    }
  }

  private fun setUpInputField() {
    with(binding) {
      inputEmail.isEndIconVisible = false
      etEmail.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
          inputEmail.isEndIconVisible = editable?.toString()?.trim() == "user@rally"
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
      })
      etEmail.setText("user@rally")

      etPwd.setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
          login()
          true
        } else {
          false
        }
      }
    }
  }

  private fun login() {
    val email = binding.etEmail.text?.toString()?.trim()
    val password = binding.etPwd.text?.toString()?.trim()
    if (authenticator.login(email, password)) {
      navigateToMain()
    }
  }

  private fun navigateToMain() {
    MainActivity.start(this)
    finish()
  }

  private fun runEnterAnimation() {
    with(binding) {
      inputEmail.alpha = 0f
      inputEmail.scaleX = 1.05f
      inputPwd.alpha = 0f
      inputPwd.scaleX = 1.05f
      ivFingerprint.alpha = 0f
      labelLoginId.alpha = 0f

      root.post {
        val startY = resources.displayMetrics.heightPixels / 2 - imgLogo.height / 2f
        val endY = imgLogo.y

        imgLogo.y = startY
        inputEmail.translationY += inputEmail.height
        inputPwd.translationY += inputPwd.height
        ivFingerprint.translationY += ivFingerprint.height
        labelLoginId.translationY += inputPwd.height

        val logoAnimator = ObjectAnimator.ofFloat(imgLogo, View.Y, startY, endY).apply {
          duration = DURATION
        }

        val emailInputAnimator = ObjectAnimator.ofPropertyValuesHolder(
          inputEmail,
          PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0f),
          PropertyValuesHolder.ofFloat(View.ALPHA, 1f),
          PropertyValuesHolder.ofFloat(View.SCALE_X, 1f)
        ).apply {
          startDelay = 250
          duration = 250
        }

        val pwdInputAnimator = ObjectAnimator.ofPropertyValuesHolder(
          inputPwd,
          PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0f),
          PropertyValuesHolder.ofFloat(View.ALPHA, 1f),
          PropertyValuesHolder.ofFloat(View.SCALE_X, 1f)
        ).apply {
          startDelay = 250
          duration = 250
        }

        val ivFingerPrintAnimator = ObjectAnimator.ofPropertyValuesHolder(
          ivFingerprint,
          PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0f),
          PropertyValuesHolder.ofFloat(View.ALPHA, 1f)
        ).apply {
          startDelay = 300
          duration = 200
        }

        val labelAnimator = ObjectAnimator.ofPropertyValuesHolder(
          labelLoginId,
          PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0f),
          PropertyValuesHolder.ofFloat(View.ALPHA, 1f)
        ).apply {
          startDelay = 400
          duration = 100
        }

        AnimatorSet().apply {
          interpolator = FastOutSlowInInterpolator()
          playTogether(
            logoAnimator, emailInputAnimator, pwdInputAnimator,
            ivFingerPrintAnimator, labelAnimator
          )
          start()
        }
      }
    }
  }

  companion object {
    private const val DURATION = 500L
  }
}