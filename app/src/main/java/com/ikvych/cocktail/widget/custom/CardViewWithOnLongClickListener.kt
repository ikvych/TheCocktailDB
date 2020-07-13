package com.ikvych.cocktail.widget.custom

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import androidx.cardview.widget.CardView
import com.ikvych.cocktail.R
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method


class CardViewWithOnLongClickListener(
    context: Context,
    attributeSet: AttributeSet
) : CardView(context, attributeSet){

    init {
        val a: TypedArray = context.obtainStyledAttributes(attributeSet, R.styleable.CardViewWithOnLongClickListener)

        for (i in 0 until a.indexCount) {
            val attr = a.getIndex(i)
            when (attr) {
                R.styleable.CardViewWithOnLongClickListener_onLongPress -> {
                    if (context.isRestricted) {
                        throw IllegalStateException(
                            "The " + javaClass.canonicalName + ":onKeyLongPress attribute cannot "
                                    + "be used within a restricted context"
                        )
                    }
                    val handlerName = a.getString(attr)
                    if (handlerName != null) {
                        setOnLongClickListener(object : OnLongClickListener {
                            private var mHandler: Method? = null
                            override fun onLongClick(p_v: View?): Boolean {
                                var result = false
                                if (mHandler == null) {
                                    try {
                                        mHandler = getContext().javaClass.getMethod(
                                            handlerName,
                                            View::class.java
                                        )
                                    } catch (e: NoSuchMethodException) {
                                        val id = id
                                        val idText = if (id == NO_ID) "" else (" with id '"
                                                + getContext().resources.getResourceEntryName(
                                            id
                                        ) + "'")
                                        throw IllegalStateException(
                                            ("Could not find a method " +
                                                    handlerName + "(View) in the activity "
                                                    + getContext().javaClass + " for onKeyLongPress handler"
                                                    + " on view " + this.javaClass + idText),
                                            e
                                        )
                                    }
                                }
                                try {
                                    mHandler!!.invoke(getContext(), this@CardViewWithOnLongClickListener)
                                    result = true
                                } catch (e: IllegalAccessException) {
                                    throw IllegalStateException(
                                        ("Could not execute non "
                                                + "public method of the activity"), e
                                    )
                                } catch (e: InvocationTargetException) {
                                    throw IllegalStateException(
                                        ("Could not execute "
                                                + "method of the activity"), e
                                    )
                                }
                                return result
                            }
                        })
                    }
                }
                else -> {
                }
            }
        }
        a.recycle()
    }

}