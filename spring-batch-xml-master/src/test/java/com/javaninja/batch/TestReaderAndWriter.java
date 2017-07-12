package com.javaninja.batch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestExecutionListener;
import org.springframework.batch.test.StepScopeTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author norris.shelton
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, StepScopeTestExecutionListener.class})
@ContextConfiguration
public class TestReaderAndWriter {

    @Autowired
    private StaxEventItemReader<Object> itemReader;

    @Autowired
    private StaxEventItemWriter<Object> itemWriter;

    @Test
    public void testReader() {
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        int count = 0;
        try {
            count = StepScopeTestUtils.doInStepScope(execution, () -> {
                int numCars = 0;
                int numTrucks = 0;
                itemReader.setResource(new ClassPathResource("cars-input.xml"));
                itemReader.open(execution.getExecutionContext());
                Object vehicle;
                Car car;
                Truck truck;
                try {
                    while ((vehicle = itemReader.read()) != null) {

                        if (vehicle instanceof Truck) {
                            truck = (Truck) vehicle;
                            assertNotNull(truck);
                            assertNotNull(truck.getMake());
                            assertNotNull(truck.getModel());
                            assertNotNull(truck.getColor());
                            assertTrue(truck.isExtendedCab());
                            assertTrue(truck.getDoors() > 0);
                            numTrucks++;

                        } else  if (vehicle instanceof Car) {
                            car = (Car) vehicle;
                            assertNotNull(car);
                            assertNotNull(car.getMake());
                            assertNotNull(car.getModel());
                            assertNotNull(car.getColor());
                            assertTrue(car.getDoors() > 0);
                            numCars++;
                        }
                    }
                    assertEquals(1, numTrucks);
                } finally {
                    try { itemReader.close(); } catch (ItemStreamException e) { fail(e.toString());
                    }
                }
                return numCars;
            });
        } catch (Exception e) {
            fail(e.toString());
        }
        assertEquals(1000, count);
    }

    @Test
    public void testWriter() throws Exception {
        List<Object> cars = new LinkedList<>();
        Truck truck = new Truck();
        truck.setMake("truck make");
        truck.setModel("truck model");
        truck.setColor("truck color");
        truck.setDoors(2);
        truck.setExtendedCab(true);
        cars.add(truck);

        Car car;
        for (int i = 1; i < 1001; i++) {
            car = new Car();
            car.setMake("make" + i);
            car.setModel("model" + i);
            car.setColor("color" + i);
            car.setDoors(i);
            cars.add(car);
        }

        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        StepScopeTestUtils.doInStepScope(execution, () -> {
            itemWriter.open(execution.getExecutionContext());
            itemWriter.write(cars);
            itemWriter.close();
            return null;
        });
    }
}
