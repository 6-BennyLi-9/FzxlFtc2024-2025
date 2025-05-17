package org.betastudio.ftc.action;

import static org.betastudio.ftc.util.ExceptionsUtil.getOriginException;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.utils.NullptrAction;
import org.jetbrains.annotations.Contract;

public final class ActionPolicies {
	public interface EmptyCollectionResolvePolicy {
		Action resolve();
	}

	public interface ForceHandleExceptionPolicy<T> {
		T resolve(Exception e);
	}

	public static final class CollectionAbortPolicy implements EmptyCollectionResolvePolicy {
		@Override
		public Action resolve() {
			throw new IllegalStateException();
		}
	}

	public static final class CollectionNullptrPolicy implements EmptyCollectionResolvePolicy {
		@NonNull
		@Contract(" -> new")
		@Override
		public Action resolve() {
			return new NullptrAction();
		}
	}

	public static final class RuntimeExceptionPolicy<T> implements ForceHandleExceptionPolicy <T> {
		@Override
		public T resolve(Exception e) {
			throw new RuntimeException(getOriginException(e));
		}
	}
}
