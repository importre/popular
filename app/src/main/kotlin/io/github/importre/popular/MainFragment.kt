package io.github.importre.popular

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import io.github.importre.popular.api.Popular
import io.github.importre.popular.api.PopularResult
import kotlinx.android.anko.*
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import kotlin.properties.Delegates


public class MainFragment : BaseFragment(), AnkoLogger {

    companion object {
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }

    var recyclerView: RecyclerView? = null
    var refresh: SwipeRefreshLayout? = null
    var loading: Boolean = true

    val adapter: ItemAdapter by Delegates.lazy {
        ItemAdapter(this)
    }

    var pendingObservable: Observable<PopularResult>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super<BaseFragment>.onCreate(savedInstanceState)
        setRetainInstance(true)
    }

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var p = getResources().getDimension(R.dimen.keyline_1_minus_8dp)
        val view = with(ctx) {
            verticalLayout {
                refreshLayout {
                    id = R.id.refresh
                    enabled = false
                    setColorSchemeResources(R.color.main, R.color.main_accent)

                    setOnRefreshListener {
                        refresh()
                    }

                    recyclerView {
                        id = R.id.recycler
                        scrollBarStyle = View.SCROLLBARS_OUTSIDE_OVERLAY
                        setHasFixedSize(true)
                        padding = p.toInt()
                        setClipToPadding(false)
                    }
                }
            }
        }

        initUi(view)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super<BaseFragment>.onViewCreated(view, savedInstanceState)
        loadPopular()
    }

    private fun initUi(view: LinearLayout) {
        val tolerance = 10
        val landscape = getResources().getBoolean(R.bool.landscape)
        val layoutManager = StaggeredGridLayoutManager(
                if (landscape) 2 else 1,
                StaggeredGridLayoutManager.VERTICAL)
        recyclerView = view.find<RecyclerView>(R.id.recycler)
        recyclerView!!.setLayoutManager(layoutManager)
        recyclerView!!.setAdapter(adapter)

        val scrollListener = object : RecyclerView.OnScrollListener() {
            var pastVisibleItems: Int = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                var visibleItemCount = recyclerView.getChildCount()
                var totalItemCount = layoutManager.getItemCount()

                var firstVisibleItems: IntArray? = null
                firstVisibleItems = layoutManager.findFirstVisibleItemPositions(firstVisibleItems);
                if (firstVisibleItems != null && firstVisibleItems!!.size() > 0) {
                    pastVisibleItems = firstVisibleItems!!.get(0)
                }

                if (loading) {
                    val count = visibleItemCount + pastVisibleItems
                    if (count + tolerance >= totalItemCount) {
                        loadPopular()
                        loading = false;
                    }
                }
            }
        }

        recyclerView!!.setOnScrollListener(scrollListener)
        refresh = view.find<SwipeRefreshLayout>(R.id.refresh)
    }

    private synchronized fun loadPopular() {
        refresh?.setRefreshing(true)

        var o = Popular.service.getPopular()

        if (pendingObservable == null) {
            pendingObservable = o
            o.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ result ->
                        updateUi(result)
                        pendingObservable = null
                    }, { throwable ->
                        longToast(throwable.getMessage() ?: "error")
                        refresh?.setRefreshing(false)
                    }, {
                        refresh?.setRefreshing(false)
                    })
        } else {
            pendingObservable!!.concatWith(o)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ result ->
                        updateUi(result)
                    })
        }
    }

    private fun updateUi(result: PopularResult) {
        val code = result.meta.code
        if (code == 200) {
            adapter.items.addAll(result.data)
            adapter.notifyDataSetChanged()
        } else {
            longToast("code: ${code}")
        }

        loading = true
    }

    public fun refresh() {
        adapter.clear()
        loadPopular()
    }
}