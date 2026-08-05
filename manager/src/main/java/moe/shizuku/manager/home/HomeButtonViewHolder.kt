package moe.shizuku.manager.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import moe.shizuku.manager.databinding.HomeItemContainerBinding
import moe.shizuku.manager.databinding.HomeManageAppsItemBinding
import rikka.recyclerview.BaseViewHolder
import rikka.recyclerview.BaseViewHolder.Creator

class HomeButtonViewHolder(private val binding: HomeManageAppsItemBinding, root: View) :
    BaseViewHolder<HomeButtonViewHolder.Data>(root), View.OnClickListener {

    data class Data(
        @DrawableRes val iconRes: Int,
        @StringRes val titleRes: Int,
        @StringRes val summaryRes: Int,
        val onClick: () -> Unit
    )

    companion object {
        val CREATOR = Creator<Data> { inflater: LayoutInflater, parent: ViewGroup? ->
            val outer = HomeItemContainerBinding.inflate(inflater, parent, false)
            val inner = HomeManageAppsItemBinding.inflate(inflater, outer.root, true)
            HomeButtonViewHolder(inner, outer.root)
        }
    }

    init {
        root.setOnClickListener(this)
    }

    override fun onBind() {
        binding.icon.setImageResource(data.iconRes)
        binding.text1.setText(data.titleRes)
        binding.text2.setText(data.summaryRes)
    }

    override fun onClick(v: View) {
        data.onClick()
    }
}
