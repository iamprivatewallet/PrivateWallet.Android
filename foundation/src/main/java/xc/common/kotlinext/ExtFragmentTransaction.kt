package xc.common.kotlinext

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

fun FragmentTransaction.extShow(@IdRes id:Int,fragment: Fragment){
    if (!fragment.isAdded) {
        this.add(id,fragment)
    }
    this.show(fragment)

}