package com.project.recipee.ui.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.recipee.R


class LoaderAdapter: LoadStateAdapter<LoaderAdapter.LoaderViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, LoadState: LoadState): LoaderViewHolder {
        val view = LayoutInflater.from (parent.context).inflate(R.layout.custom_loader, parent, false)
        return LoaderViewHolder(view)
    }

    override fun onBindViewHolder(holder: LoaderViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    class LoaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val progressBar = itemView.findViewById <ProgressBar>(R.id.progress_bar)
        fun bind(loadState: LoadState) {
            progressBar.isVisible = loadState is LoadState.Loading
        }
    }
}