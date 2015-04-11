package io.github.importre.popular

import android.app.Activity
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.ViewManager
import android.widget.Toast
import kotlinx.android.anko.__dslAddView

abstract class BaseFragment : Fragment() {

    public val Fragment.act: Activity
        get() = getActivity()

    public val Fragment.ctx: Context
        get() = getActivity()

    public fun Fragment.toast(textResource: Int): Unit = ctx.toast(textResource)
    public fun Context.toast(textResource: Int) {
        Toast.makeText(this, textResource, Toast.LENGTH_SHORT).show()
    }

    public fun Fragment.toast(text: CharSequence): Unit = ctx.toast(text)
    public fun Context.toast(text: CharSequence) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    public fun Fragment.longToast(textResource: Int): Unit = ctx.longToast(textResource)
    public fun Context.longToast(textResource: Int) {
        Toast.makeText(this, textResource, Toast.LENGTH_LONG).show()
    }

    public fun Fragment.longToast(text: CharSequence): Unit = ctx.longToast(text)
    public fun Context.longToast(text: CharSequence) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    fun ViewManager.recyclerView(init: RecyclerView.() -> Unit = {}) =
            __dslAddView({ RecyclerView(it) }, init, this)

    fun ViewManager.refreshLayout(init: SwipeRefreshLayout.() -> Unit = {}) =
            __dslAddView({ SwipeRefreshLayout(it) }, init, this)
}

