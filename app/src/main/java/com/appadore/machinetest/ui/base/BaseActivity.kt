package com.appadore.machinetest.ui.base

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.appadore.machinetest.utils.AppUtils
import com.google.android.material.snackbar.Snackbar


abstract class BaseActivity<VM : BaseViewModel, B : ViewDataBinding> : AppCompatActivity() {

    protected var TAG: String = this@BaseActivity.javaClass.simpleName
    protected lateinit var binding: B
    protected lateinit var viewModel: VM
    private var progressDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, getLayout())
        viewModel = ViewModelProvider(this)[getViewModelType()]
        bindViews()
    }

    abstract fun getLayout(): Int

    abstract fun getViewModelType(): Class<VM>

    abstract fun bindViews()

    /*override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStackImmediate()
        } else {
            super.onBackPressedDispatcher.onBackPressed()
        }
    }*/

    fun replaceFragment(fragment: Fragment, container: Int) {
        supportFragmentManager.beginTransaction()
            .replace(container, fragment, fragment::class.java.simpleName).commit()
    }

    fun replaceFragmentWithBackAction(fragment: Fragment, container: Int) {
        supportFragmentManager.beginTransaction()
            .replace(container, fragment, fragment::class.java.simpleName)
            .addToBackStack(fragment::class.java.simpleName).commit()
    }

    open fun requestFocus(view: View) {
        if (view.requestFocus()) {
            window.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
            )
        }
    }

    open fun showToast(value: String?) {
        Toast.makeText(this, value, Toast.LENGTH_LONG).show()
    }

    open fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(
            Activity.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    open fun showSnackBar(value: String?) {
        value?.let {
            binding.root.let {
                hideKeyboard(it)
                val snackBar = Snackbar.make(it, value, Snackbar.LENGTH_SHORT)
                snackBar.show()
            }
        }
    }

    open fun showBlockingProgress() {
        hideBlockingProgress()
        progressDialog = AppUtils.showProgressDialog(this)
        progressDialog?.window?.setBackgroundDrawable(
            ColorDrawable(
                Color.TRANSPARENT
            )
        )
        progressDialog?.show()
    }

    open fun hideBlockingProgress() {
        if (progressDialog != null && progressDialog?.isShowing == true) {
            progressDialog?.dismiss()
        }
    }
}