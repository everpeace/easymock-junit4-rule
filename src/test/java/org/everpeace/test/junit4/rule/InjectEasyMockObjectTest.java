package org.everpeace.test.junit4.rule;

import org.everpeace.test.junit4.annotation.EasyMockObject;
import org.junit.Rule;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Test for {@link InjectEasyMockObject}.
 * <p/>
 * User: everpeace _at_ gmail _dot_ com
 * Date: 11/03/22
 * Created by IntelliJ IDEA.
 */
public class InjectEasyMockObjectTest {
    @Rule
    public InjectEasyMockObject injectEasyMockObject = new InjectEasyMockObject();

    @EasyMockObject
    private SampleClass sampleMock;

    @Test
    public void test() {
        expect(sampleMock.returnOne()).andReturn(-1);
        replay(sampleMock);
        assertThat(sampleMock.returnOne(), is(-1));
        verify(sampleMock);
    }

    private static class SampleClass {
        public int returnOne() {
            return 1;
        }
    }
}
