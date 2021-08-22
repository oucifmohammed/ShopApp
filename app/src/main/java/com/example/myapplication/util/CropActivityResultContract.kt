package com.example.myapplication.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import com.theartofdev.edmodo.cropper.CropImage

class CropActivityResultContract(private val ratioX: Int = 16, private val ratioY: Int = 9) :
    ActivityResultContract<Any?, Uri>() {

    override fun createIntent(context: Context, input: Any?): Intent {
        return CropImage.activity()
            .setAspectRatio(ratioX,ratioY)
            .getIntent(context)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri {
        return CropImage.getActivityResult(intent).uri
    }
}