package xc.common.tool.utils

import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.util.HashMap

object XcClazzUtils {

    fun <T> copyAndCreate(o: Any, toClass: Class<T>): T? {
        try {
            val constructor = toClass.getConstructor()
            val toClassDeclaredFields = toClass.declaredFields
            val hashMap = HashMap<String, Field>()

            for (toClassDeclaredField in toClassDeclaredFields) {
                hashMap[toClassDeclaredField.name] = toClassDeclaredField
            }


            val t = constructor.newInstance()
            val fields = o.javaClass.declaredFields

            for (field in fields) {
                val toClassField = hashMap[field.name]
                toClassField?.isAccessible = true
                field?.isAccessible = true
                toClassField?.set(t, field.get(o))
            }
            return t
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    fun copyAndUpdate(o: Any, t: Any) {
        try {
            val toClassDeclaredFields = t::class.java.declaredFields
            val hashMap = HashMap<String, Field>()

            for (toClassDeclaredField in toClassDeclaredFields) {
                hashMap[toClassDeclaredField.name] = toClassDeclaredField
            }


            val fields = o.javaClass.declaredFields

            for (field in fields) {
                val toClassField = hashMap[field.name]
                toClassField?.isAccessible = true
                field?.isAccessible = true
                toClassField?.set(t, field.get(o))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
