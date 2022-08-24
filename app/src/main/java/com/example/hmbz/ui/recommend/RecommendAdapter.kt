package com.example.hmbz.ui.recommend

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hmbz.databinding.PictureBeanBinding

class RecommendAdapter(private val dataList: List<ItemBean>): RecyclerView.Adapter<RecommendAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: PictureBeanBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PictureBeanBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.binding.author.text = item.author
        Glide.with(holder.binding.root).load("https://huamaobizhi.com/upload/bizhi/images-wallpaper/280h/202208231753271656.jpg").into(holder.binding.picture)
        Log.d("adapter","数据已被加载完毕")
    }

    override fun getItemCount() = dataList.size
}