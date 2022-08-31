package ba.etf.rma22.projekat.view


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import ba.etf.rma22.projekat.data.fragmenti.FragmentAnkete
import ba.etf.rma22.projekat.data.fragmenti.FragmentIstrazivanje
import ba.etf.rma22.projekat.data.repositories.KorisnikRepository
import java.util.*


class ViewPagerAdapter(
    fragmentManager: FragmentManager, private var fragments: MutableList<Fragment>, lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return fragments.size
    }
    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    fun add(index: Int, fragment: Fragment) {
        fragments.add(index, fragment)
        notifyItemChanged(index)
    }

    fun refreshFragment(index: Int, fragment: Fragment) {
        fragments[index] = fragment
        notifyItemChanged(index)
    }

    fun remove(index: Int) {
        fragments.removeAt(index)
        notifyItemChanged(index)
    }

    override fun getItemId(position: Int): Long {
        return fragments[position].hashCode().toLong()
    }

    override fun containsItem(itemId: Long): Boolean {
        return fragments.find { it.hashCode().toLong() == itemId } != null
    }
}
