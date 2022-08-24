package com.example.hmbz.ui.recommend

class JsonDataBean(
    val code: Int,
    val msg: String,
    val data: List<DataBean>
) {
    inner class DataBean (
        val pid: String,
        val title: String,
        val img: String,
        val width: String,
        val height: String,
        val downloads: String,
        val purity: String,
    )
}