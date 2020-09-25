@file:Suppress("unused")

package com.ikvych.cocktail.prresentation.navigation

import android.content.Intent
import android.os.Bundle

/**
 * Interface which describes general possibility of class to start an activity.
 *
 * Usually implemented by base [androidx.appcompat.app.AppCompatActivity] and [androidx.fragment.app.Fragment]
 * as they both have the same methods signature which start an activity.
 * It allows to omit code duplication and work with with one interface for both.
 */
interface ActivityStarter {

	fun startActivity(intent: Intent)

	fun startActivity(intent: Intent, bundle: Bundle?)

	fun startActivityForResult(intent: Intent, requestCode: Int)

	fun startActivityForResult(intent: Intent, requestCode: Int, bundle: Bundle?)
}