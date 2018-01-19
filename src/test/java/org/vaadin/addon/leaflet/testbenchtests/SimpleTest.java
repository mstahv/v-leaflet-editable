package org.vaadin.addon.leaflet.testbenchtests;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import org.vaadin.addonhelpers.TListUi;
import org.vaadin.addonhelpers.TListUi.TestDetails;

public class SimpleTest extends AbstractTestBenchTest {

    @Test
    public void checkAllTestsOpenWithoutErrors() throws IOException, AssertionError {
        startBrowser();

        driver.manage().timeouts().implicitlyWait(2000, TimeUnit.MILLISECONDS);
        
        List<TestDetails> listTestClasses = TListUi.listTestClasses();
        for (TestDetails id : listTestClasses) {
			Class clazz = id.getClazz();
			
			driver.get(BASEURL + clazz.getName() + "?debug");
			try {
				WebElement error = driver.findElement(By.className("v-Notification-error"));
				Assert.fail("Test " + clazz.getName() + " has client side exception");
			} catch (NoSuchElementException e) {
				continue;
			}
			
		}
        
    }

}
