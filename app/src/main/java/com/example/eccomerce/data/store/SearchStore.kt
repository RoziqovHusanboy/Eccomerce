package com.example.eccomerce.data.store

import javax.inject.Inject

class SearchStore @Inject constructor():BaseStore<Array<String>>("searches", Array<String>::class.java)