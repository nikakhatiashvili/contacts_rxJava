package com.example.contacts.presentation

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class PaginationRecyclerView(
    context: Context, attrs: AttributeSet?
) : RecyclerView(context, attrs) {

    private var scrollListener: EndlessRecyclerViewScrollListener? = null
    private var onPageChangeListener: OnPageChangeListener? = null

    override fun setLayoutManager(layout: LayoutManager?) {
        super.setLayoutManager(layout)
        if (scrollListener == null && layout != null) {
            scrollListener = object : EndlessRecyclerViewScrollListener(layout) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    onPageChangeListener?.onPageChange(page)
                }
            }
        }
        scrollListener?.let { addOnScrollListener(it) }
    }

    fun setOnPageChangeListener(onPageChangeListener: OnPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener
    }

    fun resetPage() {
        this.scrollListener?.reset()
    }

    private abstract class EndlessRecyclerViewScrollListener(
        layoutManager: LayoutManager
    ) : OnScrollListener() {

        private var visibleThreshold = 10
        private var currentPage = 1
        private var previousTotalItemCount = 0
        private var loading = true
        private val startingPageIndex = 1

        private var mLayoutManager: LayoutManager = layoutManager

        init {
            if (layoutManager is StaggeredGridLayoutManager) {
                visibleThreshold *= layoutManager.spanCount
            } else if (layoutManager is GridLayoutManager) {
                visibleThreshold *= layoutManager.spanCount
            }
        }

        fun reset() {
            currentPage = 1
            previousTotalItemCount = 0
        }

        private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
            var maxSize = 0
            for (i in lastVisibleItemPositions.indices) {
                if (i == 0) {
                    maxSize = lastVisibleItemPositions[i]
                } else if (lastVisibleItemPositions[i] > maxSize) {
                    maxSize = lastVisibleItemPositions[i]
                }
            }
            return maxSize
        }

        override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
            var lastVisibleItemPosition = 0
            val totalItemCount = mLayoutManager.itemCount
            when (mLayoutManager) {
                is StaggeredGridLayoutManager -> {
                    val lastVisibleItemPositions =
                        (mLayoutManager as StaggeredGridLayoutManager).findLastVisibleItemPositions(
                            null
                        )
                    lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions)
                }
                is GridLayoutManager -> {
                    lastVisibleItemPosition =
                        (mLayoutManager as GridLayoutManager).findLastVisibleItemPosition()
                }
                is LinearLayoutManager -> {
                    lastVisibleItemPosition =
                        (mLayoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                }
            }
            if (totalItemCount < previousTotalItemCount) {
                this.currentPage = this.startingPageIndex
                this.previousTotalItemCount = totalItemCount
                if (totalItemCount == 0) {
                    this.loading = true
                }
            }
            if (loading && totalItemCount > previousTotalItemCount) {
                loading = false
                previousTotalItemCount = totalItemCount
            }
            if (!loading && lastVisibleItemPosition + visibleThreshold > totalItemCount) {
                currentPage++
                onLoadMore(currentPage, totalItemCount, view)
                loading = true
            }
        }

        abstract fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?)
    }

    interface OnPageChangeListener {
        fun onPageChange(page: Int)
    }

}