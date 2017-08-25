package vip.frendy.calendar.fragment

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iimedia.appbase.extension.postDelayedToUI
import kotlinx.android.synthetic.main.fragment_push.*
import vip.frendy.calendar.R

/**
 * 要闻推送
 */
class FragmentPush : FragmentBase(), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private var rootView: View? = null

    companion object {
        fun getInstance(): FragmentPush {
            val fragment = FragmentPush()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (rootView == null) {
            rootView = inflater?.inflate(R.layout.fragment_push, container, false)
            initData()
            initView()
        }
        // 缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误
        (rootView?.parent as? ViewGroup)?.removeView(rootView)
        return rootView
    }

    private fun initData() {

    }

    private fun initView() {

    }

    override fun onClick(view: View) {

    }

    override fun loadData(year: Int, month: Int, day: Int) {
        super.loadData(year, month, day)
    }

    override fun onRefresh() {
        postDelayedToUI({ swipe?.isRefreshing = false }, 1000)
    }
}
