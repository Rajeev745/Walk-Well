package com.example.walkwell.utilities

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.PictureDrawable
import androidx.annotation.DrawableRes
import androidx.compose.ui.platform.LocalDensity
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest

object UtilitiesFunctions {

    suspend fun convertSvgToBitmap(@DrawableRes svgResId: Int, context: Context): Bitmap? {
        // Create a Coil ImageRequest to load the SVG
        val imageLoader = ImageLoader.Builder(context).build()

        // Load the SVG into a PictureDrawable
        val request = ImageRequest.Builder(context)
            .data(svgResId) // Use your SVG resource ID here
            .build()

        val drawable = imageLoader.execute(request).drawable
        return if (drawable is PictureDrawable) {
            // Convert PictureDrawable to Bitmap
            val picture = drawable.picture
            val width = picture.width
            val height = picture.height
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = android.graphics.Canvas(bitmap)
            drawable.draw(canvas)
            bitmap
        } else {
            null
        }
    }

    /**
     * Converts a drawable PNG image to a Bitmap and scales it to a specified size of 18dp.
     *
     * This method first converts the drawable resource into a Bitmap and then resizes it to
     * the appropriate size based on the screen's pixel density (18dp).
     *
     * @param context The context used to access the drawable resource.
     * @param drawableRes The resource ID of the drawable PNG image to be converted.
     * @param size The desired size of the Bitmap in dp.
     *
     * @return A Bitmap of the drawable, scaled to 18dp size.
     */
    fun convertDrawableToBitmap(context: Context, drawableRes: Int, size: Int): Bitmap {
        // Get screen density to convert 18dp to pixels
        val density = context.resources.displayMetrics.density

        // Convert 18dp to pixels
        val markerSizePx = (size * density).toInt()

        // Get the drawable resource
        val drawable: Drawable = context.getDrawable(drawableRes)!!

        // Convert Drawable to Bitmap
        var bitmap = drawable.toBitmap()

        // Scale the Bitmap to the desired size (18dp)
        bitmap = Bitmap.createScaledBitmap(bitmap, markerSizePx, markerSizePx, false)

        return bitmap
    }
}