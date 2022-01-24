package test.java;

import main.java.gusthavo.utils.SRTUtils;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SRTUtilsTest {

    @Test
    public void shouldConvertTextTimeToMillis() throws Exception {

        // setup
        String textTime = "01:00:00,360";
        long expectedTimeInMillis = 3600360L;

        //given
        long textInMills = SRTUtils.textTimeToMillis(textTime);

        // assert
        assertEquals(expectedTimeInMillis, textInMills);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrownExceptionWhenGivenTimeIsNull() throws Exception {

        // setup

        //given
        SRTUtils.textTimeToMillis(null);
        // assert
    }

    @Test(expected = Exception.class)
    public void shouldThrownExceptionWhenGivenTimeIsInvalid() throws Exception {

        // setup

        //given
        SRTUtils.textTimeToMillis("00:ab:12");
        // assert
    }
}
