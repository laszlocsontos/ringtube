package net.thirdfoot.rto.kernel.config;

import java.io.InputStream;
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
@PrepareForTest(PropsBeanUtil.class)
@RunWith(PowerMockRunner.class)
public class PropsBeanUtilTest {

  @BeforeClass
  public static void setUpClass() throws Exception {
    PowerMockito.spy(PropsBeanUtil.class);

    InputStream mockInputStream = ClassLoaderUtil.getResourceUrl(
      _TEST_PROPERTY_FILE).openStream();

    PowerMockito.doReturn(
      mockInputStream).when(
        PropsBeanUtil.class, "_getExternalPropertyInputStream");

  }

  @Test
  public void testGetInteger() {
  Integer audioBitRateDefault = PropsBeanUtil.getInteger(
    "converter.audio.bit.rate.default");

    Assert.assertNotNull(audioBitRateDefault);
  }

  @Test
  public void testGetSection() {
    Map<String, String> properties = PropsBeanUtil.getSection("db");

    Assert.assertNotNull(properties);

    String driver = properties.get("db.driver");

    Assert.assertNotNull(driver);
  }

  @Test
  public void testGetString() {
    Assert.assertNotNull(PropsBeanUtil.getString("db.driver"));
  }

  private static final String _TEST_PROPERTY_FILE =
    "/META-INF/kernel-test.properties";

}