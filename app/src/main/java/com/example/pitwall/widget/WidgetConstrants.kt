package com.example.pitwall.widget

/**
 * Constants for the SharedPreferences used by the home screen widgets.
 *
 * Data is written by the ViewModel after loading from the API and read by
 * [PitWallTop3DriversWidget] and [PitWallTop3ConstructorsWidget],
 * which do not have direct access to the ViewModel.
 */
object WidgetConstants {

    /** Name of the SharedPreferences file used by the widgets. */
    const val PREFS_NAME = "pitwall_widget"

    /** Key for the name of the 1st-place driver. */
    const val KEY_DRIVER1_NAME = "driver1_name"

    /** Key for the points of the 1st-place driver. */
    const val KEY_DRIVER1_POINTS = "driver1_points"

    /** Key for the name of the 2nd-place driver. */
    const val KEY_DRIVER2_NAME = "driver2_name"

    /** Key for the points of the 2nd-place driver. */
    const val KEY_DRIVER2_POINTS = "driver2_points"

    /** Key for the name of the 3rd-place driver. */
    const val KEY_DRIVER3_NAME = "driver3_name"

    /** Key for the points of the 3rd-place driver. */
    const val KEY_DRIVER3_POINTS = "driver3_points"

    /** Key for the name of the 1st-place constructor. */
    const val KEY_CONSTRUCTOR1_NAME = "constructor1_name"

    /** Key for the points of the 1st-place constructor. */
    const val KEY_CONSTRUCTOR1_POINTS = "constructor1_points"

    /** Key for the name of the 2nd-place constructor. */
    const val KEY_CONSTRUCTOR2_NAME = "constructor2_name"

    /** Key for the points of the 2nd-place constructor. */
    const val KEY_CONSTRUCTOR2_POINTS = "constructor2_points"

    /** Key for the name of the 3rd-place constructor. */
    const val KEY_CONSTRUCTOR3_NAME = "constructor3_name"

    /** Key for the points of the 3rd-place constructor. */
    const val KEY_CONSTRUCTOR3_POINTS = "constructor3_points"
}