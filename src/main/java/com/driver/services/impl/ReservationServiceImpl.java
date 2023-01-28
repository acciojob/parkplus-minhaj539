package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {

        ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).get();
        User user = userRepository3.findById(userId).get();
        if(parkingLot==null||user==null) throw new Exception("Cannot make reservation");
        List<Spot> spotList = parkingLot.getSpotList();
        int cheapestSpot = Integer.MAX_VALUE, cheapestSpotId = -1;
        for (Spot spot : spotList) {
            int spotWheels;
            if (spot.getSpotType() == SpotType.TWO_WHEELER) {
                spotWheels = 2;
            } else if (spot.getSpotType() == SpotType.FOUR_WHEELER) {
                spotWheels = 4;
            } else {
                spotWheels = Integer.MAX_VALUE;
            }
            if (spot.getOccupied()==false) {
            if(numberOfWheels<=spotWheels&&cheapestSpot<spot.getPricePerHour()){
                cheapestSpot=spot.getPricePerHour();
                cheapestSpotId=spot.getId();
            }

            }

        }
        if(cheapestSpotId==-1) throw new Exception("Cannot make reservation");
        Reservation reservation=new Reservation();
        reservation.setNumberOfHours(timeInHours);


        Spot spot=spotRepository3.findById(cheapestSpotId).get();
        List<Reservation> spotReservationList=spot.getReservationList();
        reservation.setSpot(spot);
        spotReservationList.add(reservation);
        spot.setOccupied(true);
        spotRepository3.save(spot);



        List<Reservation> userReservationList=user.getReservationList();
        reservation.setUser(user);
        userReservationList.add(reservation);
        userRepository3.save(user);







        return reservation;
    }
}
