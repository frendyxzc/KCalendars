package vip.frendy.calendar.fragment

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.calendar_collapsing.*
import kotlinx.android.synthetic.main.calendar_info.*
import kotlinx.android.synthetic.main.calendar_month.*
import kotlinx.android.synthetic.main.calendar_week.*
import kotlinx.android.synthetic.main.fragment_calendar.*
import vip.frendy.calendar.R
import vip.frendy.calendar.entity.DateInfo
import vip.frendy.calendar.extension.AppBarStateChangeListener
import vip.frendy.calendars.CalendarUtils
import vip.frendy.calendars.OnCalendarClickListener
import vip.frendy.tablayout.listener.OnPageSelectListener
import java.util.*

/**
 * 日历内容
 */
class FragmentCalendar : FragmentBase(), View.OnClickListener {
    private var rootView: View? = null
    private val mFragments: ArrayList<FragmentBase> = ArrayList()
    private val mTitles: ArrayList<String> = arrayListOf(
            "要闻推送", "当日新闻", "公告速递"
    )
    private var mIndex: Int = 0
    private var mDate: DateInfo = DateInfo(2017, 7, 14)

    companion object {
        fun getInstance(): FragmentCalendar {
            val fragment = FragmentCalendar()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (rootView == null) {
            rootView = inflater?.inflate(R.layout.fragment_calendar, container, false)
            initData()
        }
        // 缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误
        (rootView?.parent as? ViewGroup)?.removeView(rootView)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initData() {
        mFragments.add(FragmentPush.getInstance())
        mFragments.add(FragmentToday.getInstance())
        mFragments.add(FragmentEvent.getInstance())

        val calendar = Calendar.getInstance()
        mDate = DateInfo(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE))
    }

    private fun initView() {
        bindingMonthAndWeekCalendar()
        content.adapter = CalendarPagerAdapter(childFragmentManager)
        tabs.setViewPager(content)
        tabs.setOnPageSelectListener(object : OnPageSelectListener {
            override fun onPageSelected(position: Int) {
                mIndex = position
                updateInfo(mIndex, mDate.year, mDate.month, mDate.day)
            }
        })
        updateInfo(mIndex, mDate.year, mDate.month, mDate.day)

        dateInfo.setText("${mDate.year}年${mDate.month + 1}月")
    }

    private fun bindingMonthAndWeekCalendar() {
        mcvCalendar.setOnCalendarClickListener(object : OnCalendarClickListener {
            override fun onClickDate(year: Int, month: Int, day: Int) {
                if(tlWeek.visibility == View.VISIBLE) return
                updateWeekCalendar(year, month, day)
                updateDateInfo(year, month, day)
                updateInfo(mIndex, year, month, day)
            }
            override fun onPageChange(year: Int, month: Int, day: Int) { }
        })
        wcvCalendar.setOnCalendarClickListener(object : OnCalendarClickListener {
            override fun onClickDate(year: Int, month: Int, day: Int) {
                if(tlWeek.visibility != View.VISIBLE) return
                updateMonthCalendar(year, month, day)
                updateDateInfo(year, month, day)
                updateInfo(mIndex, year, month, day)
            }
            override fun onPageChange(year: Int, month: Int, day: Int) { }
        })

        appBar.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {
                when(state) {
                    State.EXPANDED -> { }
                    State.COLLAPSED -> tlWeek.visibility = View.VISIBLE
                    else -> tlWeek.visibility = View.INVISIBLE
                }
            }
        })
    }

    override fun onClick(view: View) {

    }

    private fun updateMonthCalendar(year: Int, month: Int, day: Int) {
        val months = CalendarUtils.getMonthsAgo(mDate.year, mDate.month, year, month)
        val position = mcvCalendar.currentItem + months
        if (months != 0) {
            mcvCalendar.setCurrentItem(position, false)
        }
        val monthView = mcvCalendar.currentMonthView
        if (monthView != null) {
            monthView.setSelectYearMonth(year, month, day)
            monthView.invalidate()
        }
    }

    private fun updateWeekCalendar(year: Int, month: Int, day: Int) {
        val weeks = CalendarUtils.getWeeksAgo(mDate.year, mDate.month, mDate.day, year, month, day)
        val position = wcvCalendar.currentItem + weeks
        if (weeks != 0) {
            wcvCalendar.setCurrentItem(position, false)
        }
        val weekView = wcvCalendar.currentWeekView
        if (weekView != null) {
            weekView.setSelectYearMonth(year, month, day)
            weekView.invalidate()
        } else {
            val newWeekView = wcvCalendar.weekAdapter.instanceWeekView(position)
            newWeekView.setSelectYearMonth(year, month, day)
            newWeekView.invalidate()
            wcvCalendar.currentItem = position
        }
    }

    private fun updateDateInfo(year: Int, month: Int, day: Int) {
        dateInfo.setText("${year}年${month + 1}月")
        mDate = DateInfo(year, month, day)
    }

    private fun updateInfo(index: Int, year: Int, month: Int, day: Int) {
        mFragments[index].loadData(year, month, day)
    }

    inner class CalendarPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {
        override fun getCount(): Int = mFragments.size
        override fun getItem(index: Int): Fragment = mFragments[index]
        override fun getPageTitle(index: Int): CharSequence = mTitles[index]
    }
}
