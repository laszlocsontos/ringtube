package net.thirdfoot.rto.kernel.config;

import java.io.File;
import java.util.Map;

import jodd.util.ClassLoaderUtil;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author lcsontos
 */
@PowerMockIgnore("javax.management.*")
@PrepareForTest(PropsUtil.class)
@RunWith(PowerMockRunner.class)
public class PropsUtilTest {

  @BeforeClass
  public static void setUpClass() throws Exception {
    PowerMockito.spy(PropsUtil.class);

    File mockFile = ClassLoaderUtil.getResourceFile(_TEST_PROPERTY_FILE);

    PowerMockito.doReturn(
      mockFile).when(PropsUtil.class, "_getExternalPropertyFile");

    PropsUtil.init();
  }

  @Test
  public void testGetInteger() {
  Integer audioBitRateDefault = PropsUtil.getInteger(
    "converter.audio.bit.rate.default");

    Assert.assertNotNull(audioBitRateDefault);
  }

  @Test
  public void testGetSection() {
    Map<String, String> properties = PropsUtil.getSection("db");

    Assert.assertNotNull(properties);

    String driver = properties.get("db.driver");

    Assert.assertNotNull(driver);
  }

  @Test
  public void testGetString() {
    Assert.assertNotNull(PropsUtil.getString("db.driver"));
  }

  private static final String _TEST_PROPERTY_FILE =
    "/META-INF/rto-test.properties";

}