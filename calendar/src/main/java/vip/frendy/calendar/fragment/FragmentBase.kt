package vip.frendy.calendar.fragment

import android.support.v4.app.Fragment

/**
 * Created by iiMedia on 2017/8/2.
 */
open class FragmentBase: Fragment() {

    open fun loadData(year: Int, month: Int, day: Int) { }
}