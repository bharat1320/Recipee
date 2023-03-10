package com.project.recipee.util

import android.content.Context
import android.graphics.Color
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.project.recipee.R
import com.project.recipee.data.Dish

class CustomArrayAdapter(context: Context,
                         var items: ArrayList<Dish>,
                         val listener: SearchViewItemClicked
) : ArrayAdapter<Dish>(context, 0, items) {

    // ViewHolder class to hold references to views
    private class ViewHolder {
        lateinit var layout: ConstraintLayout
        lateinit var name: TextView
        lateinit var image: ImageView
    }

    var searchQuery = ""

    fun updateData(dishes: ArrayList<Dish>,query :String) {
        searchQuery = query
        items.clear()
        items.addAll(dishes)
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val viewHolder: ViewHolder
        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.rv_search_view, parent, false)
            viewHolder = ViewHolder()
            viewHolder.name = view.findViewById(R.id.item_name)
            viewHolder.image = view.findViewById(R.id.item_image)
            viewHolder.layout = view.findViewById(R.id.layout)
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }

        val item = getItem(position)
        viewHolder.name.text = highlightSearchQuery(item?.title ?:"",searchQuery)
        Glide.with(context).load(item?.image).into(viewHolder.image)

        viewHolder.image.setOnClickListener {
            if (item != null) {
                listener.itemClicked(item)
            }
        }
        viewHolder.name.setOnClickListener {
            if (item != null) {
                listener.itemClicked(item)
            }
        }
        viewHolder.layout.setOnClickListener {
            if (item != null) {
                listener.itemClicked(item)
            }
        }

        return view!!
    }

    override fun getFilter(): Filter {
        // Override the getFilter() method to prevent the adapter from filtering the data
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                results.values = items
                results.count = items.size
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                notifyDataSetChanged()
            }
        }
    }

    fun highlightSearchQuery(input: String, searchQuery: String): Spannable {
        val spannable = SpannableStringBuilder(input)
        val startIndex = input.indexOf(searchQuery, ignoreCase = true)
        if (startIndex != -1) {
            val endIndex = startIndex + searchQuery.length
            val highlightSpan = ForegroundColorSpan(Color.parseColor("#03DAC6"))
            spannable.setSpan(highlightSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        return spannable
    }

}

interface SearchViewItemClicked {
    fun itemClicked(item :Dish)
}

