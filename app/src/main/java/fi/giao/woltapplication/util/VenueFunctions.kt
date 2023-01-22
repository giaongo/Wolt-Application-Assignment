package fi.giao.woltapplication.util

object VenueFunctions {
    fun isFavorite(venue_id:String,list:List<String?>):Boolean = list.contains(venue_id)
}