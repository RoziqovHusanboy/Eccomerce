package com.example.eccomerce.domain.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Step (
 @StringRes  val title:Int,
    val date:String?,
  @DrawableRes  val icon:Int

)