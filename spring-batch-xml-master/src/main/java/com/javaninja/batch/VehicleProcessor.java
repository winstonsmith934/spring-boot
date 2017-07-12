package com.javaninja.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;

/**
 * @author norris.shelton
 */
@Service
public class VehicleProcessor implements ItemProcessor<Object, Object>{
    /**
     * Process the provided item, returning a potentially modified or new item for continued processing.  If the
     * returned result is null, it is assumed that processing of the item should not continue.
     * @param item to be processed
     * @return potentially modified or new item for continued processing, null if processing of the provided item should
     * not continue.
     * @throws Exception
     */
    @Override
    public Object process(Object item) throws Exception {

        if (item instanceof Truck) {
            Truck truck = (Truck) item;
            System.out.println(((Truck) item).isExtendedCab());
            System.out.println(((Truck) item).getResource());

        } else if (item instanceof Car) {
            Car car = (Car) item;
            System.out.println(car);
        }

        return item;
    }
}
