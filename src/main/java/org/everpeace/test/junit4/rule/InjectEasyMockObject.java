package org.everpeace.test.junit4.rule;

import org.everpeace.test.easymock.EasyMockInjector;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * {@link Rule} of injecting EasyMock objects to fields annotatedBy
 * {@link org.everpeace.test.junit4.annotation.EasyMockObject} on each test method({@link Before}).
 *
 * @author everpeace _at_ gmail _dot_ com
 *
 */
public class InjectEasyMockObject implements MethodRule {

	@Override
	public Statement apply(final Statement base, final FrameworkMethod method,
			final Object target) {
		assert base != null && method != null & target != null;
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				// mock objects are injected in each test
				// because target is recreated in each test.
				EasyMockInjector.injectEasyMockObjectsOn(target);
				base.evaluate();
			}
		};
	}
}
