package io.github.astrapi69.io.annotations;

/**
 * The class {@link ClassloaderExtensions} provides methods for resolve the current Classloader
 *
 * @author Asterios Raptis
 */
public class ClassloaderExtensions
{

	/**
	 * Gets the current class loader.
	 *
	 * @return 's the current class loader
	 */
	public static ClassLoader getClassLoader()
	{
		return getClassLoader(null);
	}

	/**
	 * Gets the ClassLoader from the given object.
	 *
	 * @param obj
	 *            The object.
	 * @return the ClassLoader from the given object.
	 */
	public static ClassLoader getClassLoader(final Object obj)
	{
		ClassLoader classLoader = null;
		if (null != obj)
		{
			if (isDerivate(Thread.currentThread().getContextClassLoader(),
				obj.getClass().getClassLoader()))
			{
				classLoader = obj.getClass().getClassLoader();
			}
			else
			{
				classLoader = Thread.currentThread().getContextClassLoader();
			}
			if (isDerivate(classLoader, ClassLoader.getSystemClassLoader()))
			{
				classLoader = ClassLoader.getSystemClassLoader();
			}
		}
		else
		{
			if (isDerivate(Thread.currentThread().getContextClassLoader(),
				ClassLoader.getSystemClassLoader()))
			{
				classLoader = ClassLoader.getSystemClassLoader();
			}
			else
			{
				classLoader = Thread.currentThread().getContextClassLoader();
			}
		}
		return classLoader;
	}

	/**
	 * Compares the two given ClassLoader objects and returns true if compare is a derivate of
	 * source.
	 *
	 * @param source
	 *            the source
	 * @param compare
	 *            the compare
	 * @return true, if compare is a derivate of source.
	 */
	public static boolean isDerivate(final ClassLoader source, ClassLoader compare)
	{
		if (source == compare)
		{
			return true;
		}
		if (compare == null)
		{
			return false;
		}
		if (source == null)
		{
			return true;
		}
		while (null != compare)
		{
			compare = compare.getParent();
			if (source == compare)
			{
				return true;
			}
		}
		return false;
	}
}
