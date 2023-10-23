package com.example.eccomerce.utils

import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.toast(massage:Int){
    Toast.makeText(requireContext(), massage, Toast.LENGTH_SHORT).show()
}