package com.example.hmbz.ui.recommend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hmbz.R
import com.example.hmbz.databinding.ActivityRecommendBinding
import com.example.hmbz.logic.network.HuamaoNetwork
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread

class Recommend : AppCompatActivity() {

    private lateinit var binding: ActivityRecommendBinding
    private val dataList = ArrayList<ItemBean>()
    private val maxSize = 8 * 5
    private var refreshCount = 0           // 记录刷新的次数，为推荐
    lateinit var jsonDataBean: JsonDataBean

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecommendBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadData()
        binding.commandView.layoutManager = GridLayoutManager(this,2)

        val adapter = RecommendAdapter(dataList)
        binding.commandView.adapter = adapter
//        binding.recommendRefresh.setColorSchemeResources(R.color.design_default_color_primary)
//        binding.recommendRefresh.setOnRefreshListener {
//            refreshData(adapter)
//        }
    }

    /**
     *
     * 每次加载数据都加载8张图片，最多存储40张图片，如果过多的图片就会自动移除
     */

    private fun loadData() {
        // 最多只保存40条数据
        if (dataList.size == maxSize) {
            for (i in 1..8) {
                dataList.removeLast()
            }
        }

        val mid = runBlocking {
            launch(Dispatchers.IO) {
                var response =
                    HuamaoNetwork.loadData("https://huamaobizhi.com/appApi/wallpaper/getRecomLists/?offset=${refreshCount * 8}&page_size=8").body

                // 如果是null那么就证明请求的数据没有了，所以重新将refreshCount置零确保能够刷新到新的数据
                if (response == null) {
                    refreshCount = 0
                    response =
                        HuamaoNetwork.loadData("https://huamaobizhi.com/appApi/wallpaper/getRecomLists/?offset=${refreshCount * 8}&page_size=8").body
                }
                jsonDataBean = Gson().fromJson(response!!.string(), JsonDataBean::class.java)
            }
        }
        while(true){
            if(!mid.isActive) break
        }
        val list = jsonDataBean.data
        list.forEach {
            dataList.add(ItemBean("花猫壁纸网站", it.img))
        }
        refreshCount++
    }

    private fun refreshData(adapter: RecommendAdapter) {
        thread {
            runOnUiThread {
                loadData()
                adapter.notifyDataSetChanged()
                binding.recommendRefresh.isRefreshing = false
            }
        }
    }
}