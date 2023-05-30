//package com.example.life
package com.example.life

import com.google.gson.*
import java.lang.reflect.Type
import java.sql.Date
import java.text.ParseException
import java.text.SimpleDateFormat

class DateDeserializer : JsonDeserializer<Date> {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Date {
        try {
            val dateString = json?.asString
            if (dateString != null) {
                val parsedDate = dateFormat.parse(dateString)
                return Date(parsedDate.time)
            }
        } catch (e: ParseException) {
            // 파싱 예외 처리
            throw JsonParseException("날짜 변환 실패: ${json?.asString}")
        }
        throw JsonParseException("유효하지 않은 날짜 형식: ${json?.asString}")
    }
}
