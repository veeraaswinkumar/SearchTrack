package com.searchtracks.extension

import android.net.Uri
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView

/**
 * loading image in fresco
 */
fun SimpleDraweeView.loadImage(url: String?, clearCache: Boolean = false) {
    if (clearCache) {
        Fresco.getImagePipeline().clearCaches()
    }

    if (url!=null && url.isNotEmpty() && url.isValidUrl())
        this.setImageURI(Uri.parse(url), null)

}