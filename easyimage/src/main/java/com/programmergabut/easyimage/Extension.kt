package id.kjsindonesia.ia.easyimage2

sealed class Extension {
    object PNG : Extension()
    object JPEG : Extension()
    object JPG : Extension()
    object WEBP : Extension()
}

/* enum class Extension(s: String) {
    PNG(".png"),
    JPEG(".jpeg");

    val value: String = s
} */