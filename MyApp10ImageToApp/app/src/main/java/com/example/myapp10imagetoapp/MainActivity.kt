package com.example.myapp10imagetoapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapp10imagetoapp.databinding.ActivityMainBinding

import android.net.Uri
import android.graphics.Bitmap
import android.provider.MediaStore
import android.widget.Toast
import java.io.IOException
import android.graphics.Matrix
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //inicializace pomocí viewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                binding.ivImage.setImageURI(uri)
                applyImageEnhancements(uri)
            }
        }

        binding.btnTakeImage.setOnClickListener {
            getContent.launch("image/*")
        }
        binding.btnSaveImage.setOnClickListener {
            saveImageToGallery()
        }
        binding.btnShareImage.setOnClickListener {
            shareImage()
        }
        binding.btnApplyGrayscale.setOnClickListener {
            applyGrayscaleFilter()
        }
        binding.btnRotateImage.setOnClickListener {
            rotateImage(90f)
        }
        binding.btnAddSticker.setOnClickListener {
            addStickerToImage()
        }
    }

    private fun applyImageEnhancements(uri: Uri) {
        try {
            val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            // Vložení vlastních úprava obrázků, např. otočení nebo překlopení
            val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height)
            // Nastavení vylepšeného obrázku na ImageView
            binding.ivImage.setImageBitmap(rotatedBitmap)
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Nelze nahrát obrázek", Toast.LENGTH_SHORT).show()
        }
    }

    private fun rotateImage(angle: Float) {
        val drawable = binding.ivImage.drawable as? BitmapDrawable ?: return
        val bitmap = drawable.bitmap
        val matrix = Matrix().apply { postRotate(angle) }
        val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        binding.ivImage.setImageBitmap(rotatedBitmap)
    }

    private fun applyGrayscaleFilter() {
        val drawable = binding.ivImage.drawable as? BitmapDrawable ?: return
        val bitmap = drawable.bitmap
        val colorMatrix = ColorMatrix().apply { setSaturation(0f) }
        val paint = Paint().apply { colorFilter = ColorMatrixColorFilter(colorMatrix) }
        val grayscaleBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(grayscaleBitmap)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        binding.ivImage.setImageBitmap(grayscaleBitmap)
    }

    private fun saveImageToGallery() {
        val drawable = binding.ivImage.drawable as? BitmapDrawable ?: return
        val bitmap = drawable.bitmap
        val savedImageURL = MediaStore.Images.Media.insertImage(
            contentResolver,
            bitmap,
            "Modified Image",
            "Image modified by MyApp10ImageToApp"
        )
        if (savedImageURL != null) {
            Toast.makeText(this, "Obrázek byl uložen do galerie", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Nepodařilo se uložit obrázek", Toast.LENGTH_SHORT).show()
        }
    }

    private fun shareImage() {
        val drawable = binding.ivImage.drawable as? BitmapDrawable ?: return
        val bitmap = drawable.bitmap
        val path = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "Shared Image", null)
        val uri = Uri.parse(path)
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "image/*"
        }
        startActivity(Intent.createChooser(shareIntent, "Sdílet obrázek pomocí"))
    }

    private fun addStickerToImage() {
        val drawable = binding.ivImage.drawable as? BitmapDrawable ?: return
        val bitmap = drawable.bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(bitmap)
        val sticker = BitmapFactory.decodeResource(resources, R.drawable.sticker) // Přidat vlastní samolepku do res/drawable

        // Určení pozice, kde bude samolepka umístěna
        val left = (bitmap.width - sticker.width) / 2
        val top = (bitmap.height - sticker.height) / 2

        // Přidání samolepky na obrázek
        canvas.drawBitmap(sticker, left.toFloat(), top.toFloat(), null)

        // Nastavení upraveného obrázku s přidanou samolepkou na ImageView
        binding.ivImage.setImageBitmap(bitmap)
    }
}