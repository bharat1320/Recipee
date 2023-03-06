package com.project.recipee.ui.home.adapters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.project.recipee.data.Dish
import com.project.recipee.data.getNutritionString
import com.project.recipee.databinding.RvDishItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeRvPagingAdapter(val context: Context, val rv: RecyclerView, private val listener: HomeRvItemClicked):
    PagingDataAdapter<Dish, HomeRvPagingAdapter.HomeRvPagingViewHolder>(Diff) {

    inner class HomeRvPagingViewHolder(var binding : RvDishItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeRvPagingViewHolder {
        return HomeRvPagingViewHolder(RvDishItemBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: HomeRvPagingViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.binding.let {
//                val imageWidthPixels = 1024
//                val imageHeightPixels = 768
//
//                val sizePass = RequestOptions().override(100,100)
//
//                CoroutineScope(Dispatchers.Main).launch {
//                    it.homeRvItemImage.layout(0,0,0,0)
//                    Glide.with(context)
//                        .load(item.image)
//                        .override(it.homeRvItemImage.width,it.homeRvItemImage.height)
//                        .apply(sizePass)
//                        .into(it.homeRvItemImage)
//                }
                Glide.with(context).load(item.image).override(it.homeRvItemImage.width,it.homeRvItemImage.height).into(it.homeRvItemImage)
                it.homeRvItemTitle.text = item.title
                it.homeRvItemChip.chipLayout.visibility = View.GONE
                if(item.nutrition?.nutrients?.isNotEmpty() == true) {
                    it.homeRvItemChip.chipLayout.visibility = View.VISIBLE
                    it.homeRvItemChip.chipText.text = item.nutrition.nutrients[0].getNutritionString()
                }
                it.homeRvItemTitle.setOnClickListener {
                    listener.itemCLicked(item)
                }
                it.homeRvItemImage.setOnClickListener {
                    listener.itemCLicked(item)
                }

//                it.homeRvItemLike.setOnClickListener {
//                    listener.itemCLicked(item)
//                }
//
//                it.homeRvItemTitle.setOnClickListener {
//                    listener.itemCLicked(item)
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

            }
        }
    }


//    fun setLike(likedAnimationView: LottieAnimationView, liked: Boolean) {
//        if(liked) {
//            likedAnimationView.visibility = View.VISIBLE
//            likedAnimationView.playAnimation()
//        } else {
//            likedAnimationView.visibility = View.GONE
//        }
//    }

    object Diff : DiffUtil.ItemCallback<Dish>() {
        override fun areItemsTheSame(oldItem: Dish, newItem: Dish): Boolean =
            oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Dish, newItem: Dish): Boolean =
            oldItem == newItem
    }
}

interface HomeRvItemClicked{
    fun itemCLicked(item: Dish)
//    fun itemAddToCartCLicked(item: Dish)
//    fun itemRemoveFromCartCLicked(item: Dish)
}
