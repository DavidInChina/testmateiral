package io.material.rally.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import androidx.constraintlayout.helper.widget.Flow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.transition.AutoTransition
import androidx.transition.Slide
import androidx.transition.TransitionManager
import androidx.viewpager.widget.ViewPager
import io.material.rally.R
import io.material.rally.databinding.LayoutRallyTabBinding
import io.material.rally.ui.viewpager.SwipeControllableViewPager

class RallyTab @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    // 使用 View Binding
    private val binding: LayoutRallyTabBinding = LayoutRallyTabBinding.inflate(
        LayoutInflater.from(context), this, true
    )
    var viewPager: SwipeControllableViewPager? = null
    var previousClickedPosition = 0
    var lastClickedPosition = 0

    private val transition by lazy {
        AutoTransition().apply {
            excludeTarget(binding.textView, true)
        }.apply {
            interpolator = FastOutSlowInInterpolator()
            duration = 300
        }
    }

    private val titleAlphaAnimator by lazy {
        ObjectAnimator.ofFloat(binding.textView, "alpha", 0f, 1f).apply {
            startDelay = duration * 1 / 3
            duration = 300
        }
    }

    private val titleSlideAnimator by lazy {
        val tvWidth = resources.displayMetrics.widthPixels - (binding.image1.width * 5)
        ObjectAnimator.ofFloat(
            binding.textView, "translationX", tvWidth.toFloat(), 0f
        ).apply {
            interpolator = FastOutSlowInInterpolator()
            duration = 300
        }
    }

    init {
        addView(binding.root)

        val slideInRight = Slide(Gravity.RIGHT)
        slideInRight.duration = 1000
        slideInRight.startDelay = 0

        binding.image1.setOnClickListener {
            viewPager?.setCurrentItem(0, true)
        }

        binding.image2.setOnClickListener {
            viewPager?.setCurrentItem(1, true)
        }

        binding.image3.setOnClickListener {
            viewPager?.setCurrentItem(2, true)
        }

        binding.image4.setOnClickListener {
            viewPager?.setCurrentItem(3, true)
        }

        binding.image5.setOnClickListener {
            viewPager?.setCurrentItem(4, true)
        }
    }

    val tabNames = listOf("Overview", "Accounts", "Bills", "Budgets", "Settings")

    fun clickedItem(position: Int) {
        val flow = binding.root.findViewById<Flow>(R.id.flow)
        val refs = flow.referencedIds.toMutableList()

        // Bug 修复：Flow 在配置更改后没有正确引用 ID
        if (refs.size < position) {
            viewPager?.currentItem = 0
            return
        }

        TransitionManager.beginDelayedTransition(binding.cl, transition)
        previousClickedPosition = lastClickedPosition
        lastClickedPosition = position

        if (lastClickedPosition != previousClickedPosition) {
            binding.textView.alpha = 0f

            refs.remove(R.id.textView)
            refs.filterIndexed { index, _ -> index != position }
                .forEach {
//                    binding.root.findViewById<MaterialButton>(it).iconTint =
//                        ColorStateList.valueOf(fetchColor(R.attr.colorPrimaryVariant))
                }

//            binding.root.findViewById<MaterialButton>(refs[position]).iconTint =
//                ColorStateList.valueOf(fetchColor(R.attr.colorPrimary))
//            binding.textView.setTextColor(
//                fetchColor(R.attr.colorPrimary)
//            )

            binding.textView.text = tabNames[position]

            if (previousClickedPosition < lastClickedPosition) {
                val set = AnimatorSet()
                set.playTogether(titleAlphaAnimator, titleSlideAnimator)
                set.start()
            } else {
                titleAlphaAnimator.start()
            }

            refs.add(position + 1, R.id.textView)
            flow.referencedIds = refs.toIntArray()
        }
    }

    private fun fetchColor(colorAttr: Int): Int {
        val typedValue = TypedValue()
        val a = context.obtainStyledAttributes(typedValue.data, intArrayOf(colorAttr))
        val color = a.getColor(0, 0)
        a.recycle()
        return color
    }

    fun setUpWithViewPager(
        viewPager: SwipeControllableViewPager,
        allowSwipe: Boolean
    ) {
        this.viewPager = viewPager
        this.viewPager?.swipeEnabled = allowSwipe

        this.viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                clickedItem(position)
            }
        })
    }
}