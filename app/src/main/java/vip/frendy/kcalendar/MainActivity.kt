package vip.frendy.kcalendar

import android.os.Bundle
import vip.frendy.calendar.fragment.FragmentCalendar

class MainActivity : BaseFragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setDefaultFragment(FragmentCalendar.getInstance())
    }
}
