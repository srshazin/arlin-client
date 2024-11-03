package com.shazin.arlin.Utils




fun parseByteArrayToString(barray: ByteArray?): String?{
    if (barray != null){
        return String(barray)
    }else {
        return null
    }
}