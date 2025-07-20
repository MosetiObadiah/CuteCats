package com.moseti.cutecats

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * The Application class, annotated with @HiltAndroidApp to trigger Hilt's code generation.
 * This sets up the dependency injection container for the entire application.
 * All manual dependency creation has been moved to the `di.AppModule`.
 */
@HiltAndroidApp
class CatApplication : Application()