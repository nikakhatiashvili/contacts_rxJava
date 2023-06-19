package com.example.contacts.presentation.contacts.adapter

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.contacts.databinding.ContactItemBinding
import java.util.Random

class ContactsViewHolder(
    private val binding: ContactItemBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ContactsAdapter.ListItem.UiContact) {
        binding.nameTv.text = item.contact.name
        binding.numberTv.text = item.contact.number
        if (item.contact.image.toString().isNullOrEmpty()){
            Glide.with(binding.root.context)
                .load(item.contact.bitmap)
                .circleCrop()
                .into(binding.imageView)
        }else{
            Glide.with(binding.root.context)
                .load(item.contact.image)
                .circleCrop()
                .into(binding.imageView)
        }


    }

    private fun generateRandomColor(): Int {
        val random = Random()
        val r = random.nextInt(256)
        val g = random.nextInt(256)
        val b = random.nextInt(256)
        return Color.rgb(r, g, b)
    }

    private fun generateLetterBitmap(
        letter: Char,
        backgroundColor: Int,
        textColor: Int,
        textSize: Float
    ): Bitmap {
        val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint().apply {
            color = backgroundColor
            style = Paint.Style.FILL
        }
        canvas.drawPaint(paint)

        paint.color = textColor
        paint.textSize = textSize
        paint.textAlign = Paint.Align.CENTER
        val x = canvas.width / 2F
        val y = (canvas.height / 2F) - ((paint.descent() + paint.ascent()) / 2F)
        canvas.drawText(letter.toString(), x, y, paint)

        return bitmap
    }

}