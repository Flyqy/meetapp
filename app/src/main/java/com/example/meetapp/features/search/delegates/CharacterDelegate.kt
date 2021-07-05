package com.example.meetapp.features.search.delegates

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.example.meetapp.R
import com.example.meetapp.features.search.CharacterUi
import com.example.meetapp.stuff.recycler.EasyDelegate

class CharacterDelegate : EasyDelegate<Any, CharacterUi, CharacterViewHolder>() {

    override fun isForViewType(item: Any): Boolean = item is CharacterUi

    override fun onCreateViewHolder(
        parent: ViewGroup
    ): CharacterViewHolder = CharacterViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_character,
            parent,
            false
        )
    )

    override fun onBindViewHolder(
        item: CharacterUi,
        holder: CharacterViewHolder,
        payloads: List<Any>
    ) = with(holder) {
        naming.text = if (item.name.isNullOrEmpty()) {
            naming.context.resources.getString(R.string.unknown)
        } else item.name
        Glide.with(heroLogo)
            .asBitmap()
            .load(item.logo)
            .centerCrop()
            .into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    heroLogo.setImageBitmap(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
        description.text = if (item.description.isNullOrEmpty()) {
            naming.context.resources.getString(R.string.empty_description)
        } else item.description
    }
}

class CharacterViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val naming: TextView = view.findViewById(R.id.naming)
    val description: TextView = view.findViewById(R.id.description)
    val heroLogo: ImageView = view.findViewById(R.id.heroLogo)
}