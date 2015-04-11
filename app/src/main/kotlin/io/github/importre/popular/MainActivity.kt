package io.github.importre.popular

import android.annotation.TargetApi
import android.content.res.Configuration
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ListView
import com.afollestad.materialdialogs.MaterialDialog
import kotlinx.android.anko.*
import kotlin.properties.Delegates
import android.content.Intent as INTENT


public class MainActivity : BaseActivity() {

    val toolbar: Toolbar by Delegates.lazy {
        find<Toolbar>(R.id.toolbar)
    }

    val drawerLayout: DrawerLayout by Delegates.lazy {
        find<DrawerLayout>(R.id.drawer_layout)
    }

    val drawerToggle: ActionBarDrawerToggle by Delegates.lazy {
        ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.app_name, R.string.app_name)
    }

    val navDrawer: LinearLayout by Delegates.lazy {
        find<LinearLayout>(R.id.navdrawer)
    }

    val listView: ListView by Delegates.lazy {
        find<ListView>(R.id.listview)
    }

    private var mainFragment: MainFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super<BaseActivity>.onCreate(savedInstanceState)

        drawerLayout {
            id = R.id.drawer_layout
            setFitsSystemWindows(true)
            setStatusBarBackgroundColor(primaryDarkColor);

            verticalLayout {
                toolbar {
                    id = R.id.toolbar
                    backgroundColor = primaryColor
                    setTitleTextColor(Color.WHITE)
                    setSubtitleTextAppearance(ctx, R.style.TextAppearance_AppCompat_Small)
                    setContentInsetsAbsolute(insetStart, 0)
                    setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
                    if (Build.VERSION.SDK_INT >= 21) {
                        setElevation(dip(4).toFloat())
                    }
                }.layoutParams(width = matchParent, height = toolbarSize.toInt())

                frameLayout {
                    id = R.id.container
                }
            }

            verticalLayout {
                id = R.id.navdrawer
                clickable = true
                layoutParams = DrawerLayout.LayoutParams(
                        getDimen(R.dimen.navdrawer_width),
                        matchParent, Gravity.START)

                listView {
                    id = R.id.listview
                    backgroundResource = android.R.color.white
                    setSelector(getAttrResId(R.attr.selectableItemBackground))
                }.layoutParams(width = matchParent, height = matchParent)
            }
        }

        drawerLayout.setDrawerListener(drawerToggle);
        initContainer(savedInstanceState)
    }

    private fun initContainer(bundle: Bundle?) {
        val fm = getSupportFragmentManager()
        val tag = "container"

        if (bundle == null) {
            if (mainFragment == null) {
                mainFragment = MainFragment.Companion.newInstance()
            }

            fm.beginTransaction()
                    .replace(R.id.container, mainFragment, tag)
                    .commit()
        } else {
            mainFragment = fm.findFragmentByTag(tag) as MainFragment
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super<BaseActivity>.onPostCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        populateNavDrawer()
        removeTopMarginOfNavDrawer()
        drawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super<BaseActivity>.onConfigurationChanged(newConfig)
        drawerToggle.onConfigurationChanged(newConfig)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true
        }

        when (item!!.getItemId()) {
            R.id.action_refresh -> {
                mainFragment?.refresh()
                return true
            }
        }

        return super<BaseActivity>.onOptionsItemSelected(item)
    }

    private fun populateNavDrawer() {
        val header = layoutInflater.inflate(
                R.layout.nav_drawer_header, listView, false)
        listView.addHeaderView(header)

        val adapter = ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1)

        adapter.add(getString(R.string.help))
        adapter.add("${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})")

        listView.setAdapter(adapter)
        listView.setOnItemClickListener { adapterView, view, position, id ->
            val count = listView.getHeaderViewsCount()
            if (position >= count) {
                val pos = position - count

                when (pos) {
                    0 -> openHelpDialog()
                }

                closeDrawersDelayed(300)
            }
        }
    }

    private fun openHelpDialog() {
        MaterialDialog.Builder(this)
                .title(R.string.help_title)
                .content(R.string.help_desc)
                .positiveText(android.R.string.ok)
                .neutralText(R.string.fork_on_github)
                .callback(object : MaterialDialog.ButtonCallback() {
                    override fun onNeutral(dialog: MaterialDialog?) {
                        openGithubRepo()
                    }
                })
                .show()
    }

    private fun openGithubRepo() {
        try {
            val url = getString(R.string.repo_addr)
            val intent = INTENT(INTENT.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            longToast(e.getMessage() ?: "error")
        }
    }

    private fun closeDrawersDelayed(delayMillis: Long) {
        drawerLayout.postDelayed(object : Runnable {
            override fun run() {
                drawerLayout.closeDrawers()
            }
        }, delayMillis)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(navDrawer)) {
            drawerLayout.closeDrawers()
            return
        }

        super<BaseActivity>.onBackPressed()
    }

    TargetApi(21)
    private fun removeTopMarginOfNavDrawer() {
        if (Build.VERSION.SDK_INT < 21) {
            return
        }

        navDrawer.setOnApplyWindowInsetsListener { v, insets: WindowInsets ->
            val lp = listView.getLayoutParams() as ViewGroup.MarginLayoutParams
            lp.topMargin = 0
            listView.setLayoutParams(lp)
            insets
        }
    }
}
