package io.github.importre.popular.api

public class PopularResult {

    val meta: Meta = Meta()
    val data: Array<Item> = array()
}

class Meta(val code: Int = 0)
