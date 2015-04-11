package io.github.importre.popular.api

public class Item {

    val type: String = "image"
    val link: String = "https://instagram.com/"
    val created_time: Long = 0
    val images: Images = Images(Image(), Image(), Image())
    val videos: Images = Images(Image(), Image(), Image())
    val user: User = User()
    val caption: Caption? = null
    val likes = Likes()
    val comments = Comments()
}

class Images(val low_resolution: Image,
             val thumbnail: Image,
             val standard_resolution: Image)

class Image(val url: String = "",
            val width: Int = 0,
            val height: Int = 0)

class User(val username: String = "",
           val full_name: String = "",
           val profile_picture: String = "",
           val id: Long = 0)

class Caption(val text: String,
              val id: Long)

class Likes(val count: Int = 0,
            val data: Array<User> = array())

class Comments(val count: Int = 0,
               val data: Array<Comment> = array())

class Comment(val created_time: Long,
              val text: String,
              val from: User,
              val id: Long)