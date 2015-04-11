package io.github.importre.popular

import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.text.format.DateUtils
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import io.github.importre.popular.api.Item
import kotlinx.android.anko.AnkoLogger
import kotlinx.android.anko.find
import kotlinx.android.anko.layoutInflater
import java.util.ArrayList

public class ItemAdapter(val fragment: Fragment) :
        RecyclerView.Adapter<ItemAdapter.ViewHolder>(), AnkoLogger {

    val items = ArrayList<Item>()

    override fun getItemCount(): Int {
        return items.size()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
        val context = parent!!.getContext()
        val v = context.layoutInflater.inflate(
                R.layout.layout_item, parent, false)
        val header = v.find<View>(R.id.header)
        val image = v.find<ImageView>(R.id.image)
        val profile = v.find<ImageView>(R.id.profile)
        val title = v.find<TextView>(R.id.title)
        val subtitle = v.find<TextView>(R.id.subtitle)
        val caption = v.find<TextView>(R.id.caption)
        val likes = v.find<TextView>(R.id.likes)
        val comments = v.find<TextView>(R.id.comments)
        val vh = ViewHolder(v, header, image, profile, title,
                subtitle, caption, likes, comments)
        return vh
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        if (holder != null) {
            val item = items.get(position)

            holder.view.setOnClickListener {
                val activity = fragment.getActivity()
                if (activity != null) {
                    val intent = Intent(activity, javaClass<DetailsActivity>())
                    intent.putExtra("data", Gson().toJson(item))
                    activity.startActivity(intent)
                }
            }

            Glide.with(fragment)
                    .load(item.images.standard_resolution.url)
                    .fitCenter()
                    .crossFade()
                    .into(holder.image)

            Glide.with(fragment)
                    .load(item.user.profile_picture)
                    .fitCenter()
                    .crossFade()
                    .into(holder.profile)

            holder.title.setText(item.user.username)
            val time = DateUtils.getRelativeTimeSpanString(
                    item.created_time * 1000, System.currentTimeMillis(), 0,
                    DateUtils.FORMAT_ABBREV_RELATIVE).toString()
            holder.subtitle.setText(time)
            holder.caption.setText(item.caption?.text)
            holder.likes.setText(item.likes.count.toString())
            holder.comments.setText(item.comments.count.toString())

            val isVideo = "video".equals(item.type)
            holder.header.setBackgroundResource(if (isVideo) {
                R.color.header_video
            } else {
                R.color.header_image
            })
        }
    }

    inner class ViewHolder(val view: View, val header: View,
                           val image: ImageView, val profile: ImageView,
                           val title: TextView, val subtitle: TextView,
                           val caption: TextView, val likes: TextView,
                           val comments: TextView) :
            RecyclerView.ViewHolder(view)

    fun clear() {
        items.clear()
    }
}
