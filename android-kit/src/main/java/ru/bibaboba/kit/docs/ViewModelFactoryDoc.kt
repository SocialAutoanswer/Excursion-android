package ru.bibaboba.kit.docs

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModel
import ru.bibaboba.kit.dagger.*


/**
 * [ViewModelFactory] - realisation of [ViewModelProvider.Factory] that allows us to inject
 * dependencies into our ViewModel constructor.
 *
 * [ViewModelKey] - annotation that provides our ViewModel Class into Factory
 *
 * Usage:
 *
 * Firstly bind or provide [ViewModelFactory].
 *
 * kotlin
 * ```
 * @Module
 * interface SomeModule {
 *
 *     @Binds
 *     fun bindViewModelFactory(impl: ViewModelFactory): ViewModelProvider.Factory
 *
 * }
 * ```
 *
 * Secondary bind or provide your ViewModel using [ViewModelKey].
 *
 * kotlin
 * ```
 * @Module
 * interface SomeModule {

 *     @Binds
 *     @IntoMap
 *     @ViewModelKey(SomeViewModel::class)
 *     fun bindSomeViewModel(viewModel: SomeViewModel): ViewModel
 *
 * }
 * ```
 *
 * Finally inject factory in your activity or fragment and create [ViewModel] using this factory.
 *
 * kotlin
 * ```
 * class MainActivity: AppCompatActivity() {
 *     ...
 *
 *     @Inject private lateinit var factory: ViewModelFactory
 *     private val viewModel by lazy { ViewModelProvider(this, factory)[SomeViewModel::class.java] }
 *
 *     ...
 * }
 * ```
*/

annotation class ViewModelFactoryDoc
