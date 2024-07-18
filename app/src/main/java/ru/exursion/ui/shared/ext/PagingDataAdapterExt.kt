package ru.exursion.ui.shared.ext

import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.exursion.R
import ru.exursion.data.CanNotGetDataException
import ru.exursion.data.ForbiddenException
import ru.exursion.data.InternalServerException
import ru.exursion.data.UnauthorizedException
import ru.exursion.ui.auth.AuthActivity
import kotlin.reflect.KClass

typealias ErrorHandler = Fragment.() -> Unit

fun <T: Any, VH: ViewHolder> PagingDataAdapter<T, VH>.addErrorHandlers(
    fragment: Fragment,
    onUnauthorizedError: ErrorHandler = defaultUnauthorizedHandler,
    onInternalServerError: ErrorHandler = defaultInternalErrorHandler,
    onNetworkError: ErrorHandler = defaultNetworkErrorHandler,
    onForbiddenError: ErrorHandler = defaultForbiddenErrorHandler,
    otherErrorHandlers: Map<KClass<Throwable>, ErrorHandler> = mapOf()
) = addLoadStateListener { loadState ->
    val errorState = when {
        loadState.append is LoadState.Error -> loadState.append as LoadState.Error
        loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
        loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
        else -> return@addLoadStateListener
    }

    when(errorState.error) {
        is InternalServerException -> fragment.onInternalServerError()
        is CanNotGetDataException -> fragment.onNetworkError()
        is UnauthorizedException -> fragment.onUnauthorizedError()
        is ForbiddenException -> fragment.onForbiddenError()
        else -> otherErrorHandlers[errorState.error::class]
            ?.invoke(fragment)
            ?: fragment.onInternalServerError()
    }
}

private val defaultUnauthorizedHandler: ErrorHandler = {
    findNavController().navigate(R.id.choose_auth_method_fragment)
}

// TODO: мб нужно будут переписать под заглушки в списке
private val defaultNetworkErrorHandler: ErrorHandler = {
    networkErrorDialog {
        onDismiss { activity?.finish() }
        onNeutralClick { activity?.finish() }
    }
}

private val defaultInternalErrorHandler: ErrorHandler = lambda@{
    val context = context ?: return@lambda
    Toast.makeText(context, R.string.undefined_error, Toast.LENGTH_LONG).show()
}

private val defaultForbiddenErrorHandler: ErrorHandler = lambda@{
    val context = context ?: return@lambda
    Toast.makeText(context, R.string.error_forbidden_text, Toast.LENGTH_LONG).show()

    val activity = activity ?: return@lambda
    activity.startActivity(Intent(activity, AuthActivity::class.java))
    activity.finish()
}