package tw.holidaybear.graphql.android.binding

import android.annotation.SuppressLint
import android.databinding.BindingAdapter
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.support.v4.content.ContextCompat
import android.widget.ImageView
import android.widget.TextView

object BindingAdapters {

    @BindingAdapter("solidColor")
    @JvmStatic fun bindSolidColor(view: ImageView, color: String?) {
        val bgShape = view.background as GradientDrawable
        if (color != null) {
            bgShape.setColor(Color.parseColor(color))
        } else {
            bgShape.setColor(ContextCompat.getColor(view.context, android.R.color.transparent))
        }
    }

    @SuppressLint("SetTextI18n")
    @BindingAdapter("starCount")
    @JvmStatic fun bindStarCount(view: TextView, count: Int) {
        if (count >= 1000) {
            view.text = "★ %.1fk".format(count/1000.toDouble())
        } else {
            view.text = "★ $count"
        }
    }
}