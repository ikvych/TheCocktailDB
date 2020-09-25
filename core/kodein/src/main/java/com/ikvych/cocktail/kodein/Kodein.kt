@file:Suppress("unused")
package com.ikvych.cocktail.kodein

import org.kodein.di.DKodein
import org.kodein.di.Kodein
import org.kodein.di.TT
import org.kodein.di.TypeToken
import org.kodein.di.bindings.Provider
import org.kodein.di.bindings.RefMaker
import org.kodein.di.bindings.Singleton
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.javaType

inline fun <reified T : Any> Kodein.BindBuilder.WithScope<Any?>.createSingleton(
	ref: RefMaker? = null,
	sync: Boolean = true
): Singleton<Any?, T> = singleton(ref, sync) { dkodein.createInstance(T::class) }

inline fun <reified T : Any> Kodein.BindBuilder.WithContext<Any?>.createProvider(): Provider<Any?, T> =
	provider { dkodein.createInstance(T::class) }

@Suppress("UNCHECKED_CAST")
fun <T : Any> DKodein.createInstance(clazz: KClass<T>): T {

	return if (clazz.constructors.size == 1) {
		val constructor = clazz.constructors.first()

		val paramArray = arrayOfNulls<Any>(constructor.parameters.size)

		constructor.parameters.forEachIndexed { index, kParameter: KParameter ->
			val tt = TT(kParameter.type.javaType) as TypeToken<Any>
			val tag = kParameter.findAnnotation<Tag>()?.tag

			// TODO try to handle Provider and Factory binding
			paramArray[index] = dkodein.Instance(tt, tag)

			// should be tested
//			paramArray[index] = dkodein.InstanceOrNull(tt, tag) ?: dkodein.Provider(tt, tag)
		}

		constructor.call(*paramArray)

	} else throw IllegalStateException("The target class[$clazz] must have exact one constructor!")
}

