package com.example.myapp07fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment

class ImageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.imagefragment, container, false)

        // Najít ImageView v layoutu
        val imageView: ImageView = view.findViewById(R.id.image_view)

        // Získat ID obrázku z argumentů
        val imageResId = arguments?.getInt("imageResId") ?: R.drawable.default_image
        imageView.setImageResource(imageResId)

        return view
    }

    companion object {
        // Factory metoda pro vytvoření instance ImageFragment s argumentem obrázku
        fun newInstance(imageResId: Int): ImageFragment {
            val fragment = ImageFragment()
            val args = Bundle()
            args.putInt("imageResId", imageResId)
            fragment.arguments = args
            return fragment
        }
    }
}