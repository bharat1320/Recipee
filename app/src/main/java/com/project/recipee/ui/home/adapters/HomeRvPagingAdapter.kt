package com.project.recipee.ui.home.adapters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.project.recipee.R
import com.project.recipee.data.Dish
import com.project.recipee.databinding.RvDishItemBinding

class HomeRvPagingAdapter(val context: Context, val rv: RecyclerView, private val listener: HomeRvItemClicked):
    PagingDataAdapter<Dish, HomeRvPagingAdapter.HomeRvPagingViewHolder>(Diff) {

//    inner class HomeRvPagingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val image = itemView.findViewById<ImageView>(R.id.home_rv_item_image)
//        val title = itemView.findViewById<TextView>(R.id.home_rv_item_title)
//    }
//
//    override fun onBindViewHolder(holder: HomeRvPagingViewHolder, position: Int) {
//        val item = getItem(position)
//        item?.let {
//            Glide.with(context).load(item.image).into(holder.image)
//            holder.title.text = item.title
//        }
//
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeRvPagingViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_dish_item,parent,false)
//        return HomeRvPagingViewHolder(view)
//    }


//    inner class HomeRvPagingViewHolder(var binding : RvDishItemBinding) : RecyclerView.ViewHolder(binding.root)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeRvPagingViewHolder {
//        val view = RvDishItemBinding.inflate(LayoutInflater.from(context))
//        view.root.layoutParams = rv.layoutManager?.let {
//            RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//        }
//        return HomeRvPagingViewHolder(RvDishItemBinding.inflate(LayoutInflater.from(context)))
//    }
//
//    override fun onBindViewHolder(holder: HomeRvPagingViewHolder, position: Int) {
//        val item = getItem(position)
//        item?.let {
//            holder.binding.let {
//
//                Glide.with(context).load(item).into(it.homeRvItemImage)
//                it.homeRvItemTitle.text = item.title
//
//                it.homeRvItemLike.setOnClickListener {
//                    listener.itemCLicked(item)
//                }
//
//                it.homeRvItemTitle.setOnClickListener {
//                    listener.itemCLicked(item)
//                }
//
//                it.homeRvItemShare.setOnClickListener {
//                    listener.itemShareCLicked(item)
//                }
//
//                it.homeRvItemLike.setOnClickListener { view ->
////                if(isBookmarkPage) {
////                If it is from bookmark page then give warning
//                    val builder: AlertDialog.Builder = AlertDialog.Builder(context)
//                    builder.setMessage("Are you sure you want to remove this from bookmark.." )
//                    builder.setCancelable(true)
//                    builder.setPositiveButton("Yes") { dialog, id ->
//                        setLike(it.homeRvItemLike,false)
//                        listener.itemRemoveFromCartCLicked(item)
//                        dialog.cancel()
//                    }
//                    builder.setNegativeButton("No") { dialog, id ->
//                        dialog.cancel()
//                    }
//                    val alert: AlertDialog = builder.create()
//                    alert.show()
////                } else {
////                    setLike(view.newsLike,false)
////                    listener.removeFromBookmarkClicked(item)
////                }
//                }
//
//                it.homeRvItemLikeBackground.setOnClickListener { view->
//                    setLike(it.homeRvItemLike,true)
//                    listener.itemAddToCartCLicked(item)
//                }
//
//            }
//        }
//    }


    inner class HomeRvPagingViewHolder(var binding : RvDishItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeRvPagingViewHolder {
        val view = RvDishItemBinding.inflate(LayoutInflater.from(context))
        view.root.layoutParams = rv.layoutManager?.let {
            RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
        return HomeRvPagingViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeRvPagingViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.binding.let {

                Glide.with(context).load(item).into(it.homeRvItemImage)
                it.homeRvItemTitle.text = item.title

                it.homeRvItemLike.setOnClickListener {
                    listener.itemCLicked(item)
                }

                it.homeRvItemTitle.setOnClickListener {
                    listener.itemCLicked(item)
                }

                it.homeRvItemShare.setOnClickListener {
                    listener.itemShareCLicked(item)
                }

                it.homeRvItemLike.setOnClickListener { view ->
//                if(isBookmarkPage) {
//                If it is from bookmark page then give warning
                    val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                    builder.setMessage("Are you sure you want to remove this from bookmark.." )
                    builder.setCancelable(true)
                    builder.setPositiveButton("Yes") { dialog, id ->
                        setLike(it.homeRvItemLike,false)
                        listener.itemRemoveFromCartCLicked(item)
                        dialog.cancel()
                    }
                    builder.setNegativeButton("No") { dialog, id ->
                        dialog.cancel()
                    }
                    val alert: AlertDialog = builder.create()
                    alert.show()
//                } else {
//                    setLike(view.newsLike,false)
//                    listener.removeFromBookmarkClicked(item)
//                }
                }

                it.homeRvItemLikeBackground.setOnClickListener { view->
                    setLike(it.homeRvItemLike,true)
                    listener.itemAddToCartCLicked(item)
                }

            }
        }
    }


    fun setLike(likedAnimationView: LottieAnimationView, liked: Boolean) {
        if(liked) {
            likedAnimationView.visibility = View.VISIBLE
            likedAnimationView.playAnimation()
        } else {
            likedAnimationView.visibility = View.GONE
        }
    }

    object Diff : DiffUtil.ItemCallback<Dish>() {
        override fun areItemsTheSame(oldItem: Dish, newItem: Dish): Boolean =
            oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Dish, newItem: Dish): Boolean =
            oldItem == newItem
    }
}

interface HomeRvItemClicked{
    fun itemCLicked(item: Dish)
    fun itemShareCLicked(item: Dish)
    fun itemAddToCartCLicked(item: Dish)
    fun itemRemoveFromCartCLicked(item: Dish)
}
