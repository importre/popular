package io.github.importre.popular

import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.text.format.DateUtils
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.VideoView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import io.github.importre.popular.api.Item
import kotlinx.android.anko.*
import kotlin.properties.Delegates

public class DetailsActivity : BaseActivity(), AnkoLogger {

    private var item: Item = Item()
    private var imageSize: Int = matchParent

    val toolbar: Toolbar by Delegates.lazy {
        find<Toolbar>(R.id.toolbar)
    }

    val image: ImageView by Delegates.lazy {
        find<ImageView>(R.id.image)
    }

    val video: VideoView by Delegates.lazy {
        find<VideoView>(R.id.video)
    }

    val commentList: LinearLayout by Delegates.lazy {
        find<LinearLayout>(R.id.comment_list)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super<BaseActivity>.onCreate(savedInstanceState)
        initData()

        verticalLayout {
            toolbar {
                id = R.id.toolbar
                backgroundColor = primaryColor
                setTitleTextColor(Color.WHITE)
                setSubtitleTextColor(Color.WHITE)
                setSubtitleTextAppearance(ctx, R.style.TextAppearance_AppCompat_Small)
                setContentInsetsAbsolute(insetStart, 0)
                setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
                if (Build.VERSION.SDK_INT >= 21) {
                    setElevation(dip(4).toFloat())
                }
            }.layoutParams(width = matchParent, height = toolbarSize.toInt())

            scrollView {
                setFillViewport(true)

                verticalLayout {
                    gravity = Gravity.CENTER

                    imageView {
                        id = R.id.image
                    }.layoutParams(width = matchParent, height = dip(imageSize))

                    relativeLayout {
                        videoView {
                            id = R.id.video
                        }.layoutParams(width = matchParent, height = dip(imageSize))
                    }

                    verticalLayout {
                        id = R.id.comment_list
                    }
                }
            }
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super<BaseActivity>.onPostCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        updateUi()
    }

    override fun onPause() {
        super<BaseActivity>.onPause()
        video.stopPlayback()
    }

    private fun initData() {
        imageSize = configuration.smallestScreenWidthDp
        val data = getIntent()?.getStringExtra("data")
        if (data != null) {
            item = Gson().fromJson(data, javaClass<Item>())
        } else {
            longToast(R.string.error_failure)
            finish()
        }
    }

    private fun updateUi() {
        toolbar.setTitle(item.user.username)
        var time = DateUtils.getRelativeTimeSpanString(
                item.created_time * 1000, System.currentTimeMillis(), 0,
                DateUtils.FORMAT_ABBREV_RELATIVE).toString();
        toolbar.setSubtitle(time)

        if ("video".equals(item.type)) {
            video.setVideoURI(Uri.parse(item.videos.low_resolution.url))
            video.requestFocus()
            video.setOnPreparedListener {
                video.start()
            }
            image.visibility = View.GONE
        } else {
            Glide.with(this)
                    .load(item.images.standard_resolution.url)
                    .fitCenter()
                    .crossFade()
                    .into(image);
            video.visibility = View.GONE
        }

        for (comment in item.comments.data) {
            val v = layoutInflater.inflate(
                    R.layout.layout_comment, commentList, false)

            Glide.with(this)
                    .load(comment.from.profile_picture)
                    .fitCenter()
                    .crossFade()
                    .into(v.find<ImageView>(R.id.profile))

            v.find<TextView>(R.id.name).setText(comment.from.username)
            v.find<TextView>(R.id.comment).setText(comment.text)

            time = DateUtils.getRelativeTimeSpanString(
                    comment.created_time * 1000, System.currentTimeMillis(), 0,
                    DateUtils.FORMAT_ABBREV_RELATIVE).toString();
            v.find<TextView>(R.id.time).setText(time)
            commentList.addView(v)
        }
    }
}
