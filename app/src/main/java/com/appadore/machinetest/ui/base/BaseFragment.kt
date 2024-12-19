package com.appadore.machinetest.ui.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VM : ViewModel, B : ViewBinding> : Fragment() {

    protected var binding: B? = null
    protected lateinit var viewModel: VM
    protected var baseActivity: BaseActivity<*, *>? = null
    private var isSharedViewModel: Boolean = false
    private var isFirstTime: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = if (isSharedViewModel) {
            ViewModelProvider(requireActivity())[getViewModelType()]
        } else {
            ViewModelProvider(this)[getViewModelType()]
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getFragmentBinding(inflater, container)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        isFirstTime = false
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        baseActivity = context as BaseActivity<*, *>
    }

    override fun onDetach() {
        super.onDetach()
        baseActivity = null
    }

    open fun requestFocus(view: View) {
        baseActivity?.requestFocus(view = view)
    }

    fun replaceFragment(fragment: Fragment, container: Int) {
        baseActivity?.replaceFragment(fragment, container)
    }

    fun replaceFragmentWithBackAction(fragment: Fragment, container: Int) {
        baseActivity?.replaceFragmentWithBackAction(fragment, container)
    }

    open fun isFirstTimeDisplayed() = isFirstTime

    open fun showToast(value: String?) {
        Toast.makeText(requireContext(), value, Toast.LENGTH_LONG).show()
    }

    open fun hideKeyboard(view: View) {
        val inputMethodManager =
            requireActivity().getSystemService(
                Activity.INPUT_METHOD_SERVICE
            ) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun setIsSharedViewModel() {
        isSharedViewModel = true
    }

    fun showBlockingProgress() {
        baseActivity?.showBlockingProgress()
    }

    fun hideBlockingProgress() {
        baseActivity?.hideBlockingProgress()
    }

    fun showSnackBar(value: String?){
        baseActivity?.showSnackBar(value)
    }

    abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): B

    abstract fun getViewModelType(): Class<VM>

    fun <T> LiveData<T>.reObserve(owner: LifecycleOwner, observer: Observer<T>) {
        removeObserver(observer)
        observe(owner, observer)
    }
}