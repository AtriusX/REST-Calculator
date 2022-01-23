package xyz.atrius.demo.data.error

/**
 * @author Atrius
 *
 * App errors are non-exception based messaging objects. Their primary purpose is to
 * raise errors in the scope of the application without needing to rely on try/catch
 * statements. AppError is the root for all errors within this application. Other
 * error scopes such as [ValidationError] also exist for scoping issues to better
 * specialized contexts.
 *
 * @suppress MemberVisibilityCanBePrivate Message may be used outside the error class.
 *
 * @property message The message applied to the application error.
 */
@Suppress("MemberVisibilityCanBePrivate")
sealed class AppError(
    val message: String
) {

    /**
     * Allows for the creation of custom error messages for when the need arises.
     * This is especially useful for when your errors depend on dynamic data.
     *
     * @param message The custom error message.
     */
    class Custom(message: String) : AppError(message)
}
