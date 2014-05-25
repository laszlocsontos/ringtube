package net.thirdfoot.rto.kernel.spring;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.thirdfoot.rto.kernel.config.PropsUtil;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author lcsontos
 */
@ContextConfiguration(locations = {"/META-INF/kernel-spring.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class JpaTest {

  @Test
  public void testJpa() {
    // TODO Remove this
    PropsUtil.init();

    Assert.assertNotNull(_em);
  }

  @PersistenceContext
  private EntityManager _em;

}