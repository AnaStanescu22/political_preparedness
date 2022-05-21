package com.example.android.politicalpreparedness.utils

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.network.models.Election
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("listData")
fun RecyclerView.setElectionData(data: List<Election>?) {
    val adapter = adapter as ElectionListAdapter
    adapter.submitList(data)
}

@BindingAdapter("civicsApiStatus")
fun bindApiStatus(statusImageView: ImageView, status: CivicsApiStatus?) {
    when (status) {
        CivicsApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        CivicsApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        CivicsApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}

@BindingAdapter("day")
fun TextView.setElectionDay(date: Date?) {
    val calendar = Calendar.getInstance()
    date?.let {
        calendar.time = it
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        text = dateFormat.format(calendar.time)
    }
}

@BindingAdapter("followOrUnfollow")
fun Button.followOrUnfollow(hasSaved: Boolean?) {
    hasSaved?.let {
        text =
            if (it) resources.getString(R.string.unfollow_btn) else resources.getString(R.string.follow_btn)
    }
}