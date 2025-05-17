package net.cakemc.swarm.units

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path

/**
 * A utility object for serializing and deserializing objects using Gson.
 *
 * Supports:
 * - Pretty printing
 * - Generic types and type tokens
 * - Null handling
 * - File I/O using java.nio.file.Path
 * - Deserialization safety
 */
object GsonHelper {

    /**
     * A compact (non-pretty) Gson instance.
     */
    val compactGson: Gson = GsonBuilder()
        .serializeNulls()
        .disableHtmlEscaping()
        .create()

    /**
     * A pretty-printing Gson instance.
     */
    val prettyGson: Gson = GsonBuilder()
        .setPrettyPrinting()
        .serializeNulls()
        .disableHtmlEscaping()
        .create()

    /**
     * Serializes an object to a JSON string.
     *
     * @param T The type of the object.
     * @param obj The object to serialize.
     * @param pretty Whether to use pretty-printing.
     * @return The resulting JSON string.
     */
    fun <T> toJson(obj: T, pretty: Boolean = false): String {
        val gson = if (pretty) prettyGson else compactGson
        return gson.toJson(obj)
    }

    /**
     * Deserializes a JSON string into an object of the specified class.
     *
     * @param T The expected object type.
     * @param json The JSON string.
     * @param clazz The class type.
     * @return The deserialized object, or null if parsing failed.
     */
    fun <T> fromJson(json: String, clazz: Class<T>): T? {
        return try {
            compactGson.fromJson(json, clazz)
        } catch (e: JsonSyntaxException) {
            null
        }
    }

    /**
     * Deserializes a JSON string using a Type (supports generics).
     *
     * @param T The expected object type.
     * @param json The JSON string.
     * @param type The type token or generic type.
     * @return The deserialized object, or null if parsing failed.
     */
    fun <T> fromJson(json: String, type: Type): T? {
        return try {
            compactGson.fromJson(json, type)
        } catch (e: JsonSyntaxException) {
            null
        }
    }

    /**
     * Writes a Kotlin object to a file as JSON using a Path.
     *
     * @param T The type of the object.
     * @param path The target file path.
     * @param obj The object to serialize.
     * @param pretty Whether to pretty-print the output.
     */
    fun <T> writeToFile(path: Path, obj: T, pretty: Boolean = true) {
        val json = toJson(obj, pretty)
        Files.createDirectories(path.parent) // Ensure directory exists
        Files.writeString(path, json, StandardCharsets.UTF_8)
    }

    /**
     * Reads and deserializes a JSON file using a Path into a specific class.
     *
     * @param T The target object type.
     * @param path The JSON file path.
     * @param clazz The class type to deserialize to.
     * @return The deserialized object, or null if parsing failed or file was empty.
     */
    fun <T> readFromFile(path: Path, clazz: Class<T>): T? {
        if (!Files.exists(path)) return null
        val content = Files.readString(path, StandardCharsets.UTF_8).takeIf { it.isNotBlank() } ?: return null
        return fromJson(content, clazz)
    }

    /**
     * Reads and deserializes a JSON file using a Path into a generic type.
     *
     * @param T The target object type.
     * @param path The JSON file path.
     * @param type The type token or generic type.
     * @return The deserialized object, or null.
     */
    fun <T> readFromFile(path: Path, type: Type): T? {
        if (!Files.exists(path)) return null
        val content = Files.readString(path, StandardCharsets.UTF_8).takeIf { it.isNotBlank() } ?: return null
        return fromJson(content, type)
    }

    /**
     * Creates a TypeToken for a generic type, for use in deserialization.
     *
     * Example:
     * ```
     * val type = GsonHelper.typeToken<List<MyType>>()
     * ```
     */
    inline fun <reified T> typeToken(): Type = object : TypeToken<T>() {}.type
}
