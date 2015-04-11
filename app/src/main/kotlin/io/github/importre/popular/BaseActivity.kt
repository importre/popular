package io.github.importre.popular

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.annotation.AttrRes
import android.support.annotation.IdRes
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarActivity
import android.support.v7.widget.Toolbar
import android.util.TypedValue
import android.view.MenuItem
import android.view.ViewManager
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.anko.__dslAddView
import kotlin.properties.Delegates

abstract class BaseActivity : ActionBarActivity() {

    protected val primaryColor: Int by Delegates.lazy {
        getAttrColor(R.attr.colorPrimary)
    }

    protected val primaryDarkColor: Int by Delegates.lazy {
        getAttrColor(R.attr.colorPrimaryDark)
    }

    protected val toolbarSize: Int by Delegates.lazy {
        getAttrDimen(R.attr.actionBarSize)
    }

    protected val insetStart: Int by Delegates.lazy {
        getAttrDimen(R.attr.actionBarInsetStart)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun ViewManager.toolbar(init: Toolbar.() -> Unit = {}) =
            __dslAddView({ Toolbar(it) }, init, this)

    fun ViewManager.circleImageView(init: CircleImageView.() -> Unit = {}) =
            __dslAddView({ CircleImageView(it) }, init, this)

    fun Activity.drawerLayout(init: DrawerLayout.() -> Unit = {}) =
            __dslAddView({ DrawerLayout(it) }, init, this)

    fun Activity.getAttrColor(AttrRes attr: Int): Int {
        val outValue = TypedValue()
        getTheme().resolveAttribute(attr, outValue, true)
        return getResources().getColor(outValue.resourceId)
    }

    fun Activity.getAttrDimen(AttrRes attr: Int): Int {
        val outValue = TypedValue()
        getTheme().resolveAttribute(attr, outValue, true)
        return getResources().getDimension(outValue.resourceId).toInt()
    }

    fun Activity.getAttrResId(AttrRes attr: Int): Int {
        val outValue = TypedValue()
        getTheme().resolveAttribute(attr, outValue, true)
        return outValue.resourceId
    }

    fun Context.getDimen(IdRes id: Int): Int {
        return getResources().getDimension(id).toInt()
    }

    fun Activity.getDimen(IdRes id: Int): Int {
        return getResources().getDimension(id).toInt()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.getItemId()) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
