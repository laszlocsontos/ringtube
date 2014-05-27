package net.thirdfoot.rto.model;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
public class ModelTest {

  @Test
  public void testJpa() {
    Assert.assertNotNull(_em);
  }

  @PersistenceContext
  private EntityManager _em;

}