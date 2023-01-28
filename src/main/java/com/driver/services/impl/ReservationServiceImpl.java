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
      ParkingLot parkingLot;
        try {
             parkingLot = parkingLotRepository3.findById(parkingLotId).get();
        }
        catch (Exception e){
            throw new Exception("Cannot make reservation");
        }
        User user;
        try{
            user=userRepository3.findById(userId).get();
        }
        catch(Exception e){
            throw new Exception("Cannot make reservation");
        }


        List<Spot> spotList = parkingLot.getSpotList();
            if(spotList==null) throw new Exception("Cannot make reservation");



        int cheapestSpot = Integer.MAX_VALUE, cheapestSpotId = -1;
        for (Spot spot : spotList) {
            int spotWheels;
            if (spot.getSpotType().equals(SpotType.TWO_WHEELER)) {
                spotWheels = 2;
            } else if (spot.getSpotType().equals(SpotType.FOUR_WHEELER)) {
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
        if(cheapestSpot==Integer.MAX_VALUE) throw new Exception("Cannot make reservation");
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
      /*  try{
            if(!parkingLotRepository3.findById(parkingLotId).isPresent() || !userRepository3.findById(userId).isPresent()){
                throw new Exception("Cannot make reservation");
            }
            User user = userRepository3.findById(userId).get();
            ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).get();
            List<Spot> spots = parkingLot.getSpotList();

            int minCost = Integer.MIN_VALUE;

            Spot bookedSpot = null;

            if(numberOfWheels==2){
                for (Spot spot: spots){
                    int cost = timeInHours*spot.getPricePerHour();
                    if(cost<minCost && spot.getOccupied()==false){
                        bookedSpot = spot;
                    }
                }
            }
            else if(numberOfWheels==4){
                for (Spot spot: spots){
                    if(spot.getSpotType()==SpotType.TWO_WHEELER){
                        continue;
                    }
                    int cost = timeInHours*spot.getPricePerHour();
                    if(cost<minCost && spot.getOccupied()==false){
                        bookedSpot = spot;
                    }
                }
            }
            else{
                for (Spot spot: spots){
                    if(spot.getSpotType()==SpotType.OTHERS) {
                        int cost = timeInHours * spot.getPricePerHour();
                        if (cost < minCost && spot.getOccupied()) {
                            bookedSpot = spot;
                        }
                    }
                }
            }
       if(bookedSpot==null){
            throw new Exception("null");
       }

            Reservation reservation = new Reservation(timeInHours);

            bookedSpot.setOccupied(true);
            reservation.setSpot(bookedSpot);
            reservation.setUser(user);

            bookedSpot.getReservationList().add(reservation);
            user.getReservationList().add(reservation);

            spotRepository3.save(bookedSpot);
            userRepository3.save(user);

            return reservation;
       // }
      //  catch (Exception e){
      //      return null;
      //  }*/

    }
}
