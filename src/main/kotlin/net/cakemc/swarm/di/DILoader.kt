package net.cakemc.swarm.di

import com.oracle.svm.core.annotate.Inject
import kotlin.reflect.KClass
import kotlin.reflect.full.*
import kotlin.reflect.jvm.isAccessible

class DILoader {
    private val bindings = mutableMapOf<KClass<*>, KClass<*>>()
    private val singletons = mutableMapOf<KClass<*>, Any>()

    fun <T : Any, R : T> bind(interfaceClass: KClass<T>, implementationClass: KClass<R>) {
        bindings[interfaceClass] = implementationClass
    }

    fun <T : Any> bindSingleton(clazz: KClass<T>) {
        val instance = create(clazz)
        singletons[clazz] = instance
    }

    fun <T : Any> get(clazz: KClass<T>): T {
        singletons[clazz]?.let { return it as T }
        val implementation = bindings[clazz] ?: clazz
        return create(implementation) as T
    }

    private fun <T : Any> create(clazz: KClass<T>): T {
        val constructor = clazz.primaryConstructor
            ?: clazz.constructors.firstOrNull { it.parameters.isEmpty() }
            ?: throw IllegalArgumentException("No constructor for ${clazz.simpleName}")
        val args = constructor.parameters.map { param ->
            val paramClass = param.type.classifier as? KClass<*>
                ?: throw IllegalArgumentException("Unsupported param type: ${param.type}")
            get(paramClass)
        }
        val instance = constructor.call(*args.toTypedArray())
        injectFields(instance)
        return instance
    }

    private fun injectFields(instance: Any) {
        val kClass = instance::class
        kClass.memberProperties
            .filterIsInstance<kotlin.reflect.KMutableProperty1<Any, Any?>>()
            .filter { it.findAnnotation<Inject>() != null }
            .forEach { prop ->
                val type = prop.returnType.classifier as? KClass<*>
                    ?: throw IllegalArgumentException("Unknown type for ${prop.name}")
                prop.isAccessible = true
                prop.set(instance, get(type))
            }
    }
}
