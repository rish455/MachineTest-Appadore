package com.appadore.machinetest.utils

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.appadore.machinetest.R
import com.appadore.machinetest.data.model.Country
import com.appadore.machinetest.data.model.Question
import com.bumptech.glide.Glide

object BindingAdapters {

    @BindingAdapter("app:loadImageUrl")
    @JvmStatic
    fun loadImage(view: ImageView, url: String?) {
        if (url != null && url != "") {
            Glide.with(view.context)
                .load(url)
                /*.placeholder(R.drawable.img_default_profile)
                .error(R.drawable.img_default_profile)*/
                .into(view)
        }
    }

    @BindingAdapter("app:loadImageResId")
    @JvmStatic
    fun loadImage(view: ImageView, resId: Int?) {
        if (resId != null && resId != 0) {
            Glide.with(view.context)
                .load(resId)
                /*.placeholder(R.drawable.img_default_profile)
                .error(R.drawable.img_default_profile)*/
                .into(view)
        }
    }

    @BindingAdapter("app:loadImageDrawable")
    @JvmStatic
    fun loadImage(view: ImageView, drawable: Drawable?) {
        if (drawable != null) {
            Glide.with(view.context)
                .load(drawable)
                /*.placeholder(R.drawable.img_default_profile)
                .error(R.drawable.img_default_profile)*/
                .into(view)
        }
    }

    @BindingAdapter("app:visibility")
    @JvmStatic
    fun setVisibility(view: View, boolean: Boolean?) {
        view.visibility = if (boolean == true) View.VISIBLE else View.INVISIBLE
    }


    @BindingAdapter("app:optionUI")
    @JvmStatic
    fun setOptionUI(view: ConstraintLayout, data: Country?) {
        val btnOption = view.findViewById<AppCompatButton>(R.id.btnOption)
        val tvStatus = view.findViewById<TextView>(R.id.tvStatus)
        // Default state
        btnOption.setBackgroundResource(R.drawable.bg_option_button)
        btnOption.setTextColor(ContextCompat.getColorStateList(view.context, R.color.black))
        tvStatus.visibility = View.GONE

        when {
            // Correct option selected
            data?.ansStatus == AppConstants.AnswerStatus.CORRECT -> {
                btnOption.setBackgroundResource(R.drawable.bg_option_button_correct)
                btnOption.setTextColor(ContextCompat.getColorStateList(view.context, R.color.white))
                tvStatus.text = view.context.getString(R.string.correct)
                tvStatus.visibility = View.VISIBLE
            }

            // Wrong option selected
            data?.ansStatus == AppConstants.AnswerStatus.WRONG -> {
                btnOption.setBackgroundResource(R.drawable.bg_option_button_wrong)
                btnOption.setTextColor(ContextCompat.getColorStateList(view.context, R.color.white))
                tvStatus.text = view.context.getString(R.string.wrong)
                tvStatus.visibility = View.VISIBLE
            }

            // Correct option to highlight when user selects the wrong option
            data?.isCorrect == true -> {
                btnOption.setBackgroundResource(R.drawable.bg_option_button_actual_correct)
                btnOption.setTextColor(ContextCompat.getColorStateList(view.context, R.color.black))
                tvStatus.text = view.context.getString(R.string.correct)
                tvStatus.visibility = View.VISIBLE
            }

            // Selected but not correct or wrong
            data?.isSelected == true -> {
                btnOption.setBackgroundResource(R.drawable.bg_option_button_selected)
                btnOption.setTextColor(ContextCompat.getColorStateList(view.context, R.color.black))
                tvStatus.visibility = View.GONE
            }
        }
    }


    @JvmStatic
    @BindingAdapter("app:countryFlag")
    fun setCountryFlag(view: AppCompatImageView, item: Question?) {
        if (item == null)
            return
        var icon = 0
        when (item.countryCode) {
            "NZ" -> icon = R.drawable.ic_nz
            "AW" -> icon = R.drawable.ic_aw
            "EC" -> icon = R.drawable.ic_ec
            "PY" -> icon = R.drawable.ic_py
            "KG" -> icon = R.drawable.ic_kg
            "PM" -> icon = R.drawable.ic_pm
            "JP" -> icon = R.drawable.ic_jp
            "TM" -> icon = R.drawable.ic_tm
            "GA" -> icon = R.drawable.ic_ga
            "MQ" -> icon = R.drawable.ic_mq
            "BZ" -> icon = R.drawable.ic_bz
            "CZ" -> icon = R.drawable.ic_cz
            "AE" -> icon = R.drawable.ic_ae
            "JE" -> icon = R.drawable.ic_je
            "LS" -> icon = R.drawable.ic_ls
        }

        Glide.with(view.context)
            .load(icon)
            .centerCrop()
            .into(view)
    }
}